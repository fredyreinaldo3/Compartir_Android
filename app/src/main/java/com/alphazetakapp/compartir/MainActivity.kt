package com.alphazetakapp.compartir

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val boton_comparte_imagen = findViewById<Button>(R.id.compartir)
        val boton_comparte_texto = findViewById<Button>(R.id.compartir_texto)
        val boton_comparte_mulimagen = findViewById<Button>(R.id.compartir_multi)

        boton_comparte_imagen.setOnClickListener {
            if (allPermissionsGranted()) {
                compartir_imagen()
            } else {
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }

        }
        boton_comparte_texto.setOnClickListener{
            compartir()
        }
        boton_comparte_mulimagen.setOnClickListener{
            if (allPermissionsGranted()) {
                compartir_mulimagenes()
            } else {
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }
        }

    }

    private fun compartir() {
        val intento = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,"Hola Perro")
            type ="text/plain"
            putExtra(Intent.EXTRA_TITLE,"Compartame Este")
        }
        val intento_compartir = Intent.createChooser(intento,null)
        startActivity(intento_compartir)
    }
    private fun compartir_imagen(){
        val intento = Intent().apply {
            val b = BitmapFactory.decodeResource(resources,R.drawable.aplausos)
            action = Intent.ACTION_SEND
            val imagenbyte = ByteArrayOutputStream()
            b.compress(Bitmap.CompressFormat.JPEG,100, imagenbyte)
            val ruta = MediaStore.Images.Media.insertImage(contentResolver,b,"Title",null)
            val uriimagen = Uri.parse(ruta)
            putExtra(Intent.EXTRA_STREAM, uriimagen)
            type = "image/jpeg"
        }
        startActivity(Intent.createChooser(intento,null))
    }
    private fun compartir_mulimagenes(){
        val intento = Intent().apply {
            val c = BitmapFactory.decodeResource(resources,R.drawable.adobe)
            val d = BitmapFactory.decodeResource(resources,R.drawable.tercera)
            action = Intent.ACTION_SEND_MULTIPLE
            val imagenbyte1 = ByteArrayOutputStream()
            val imagenbyte2 = ByteArrayOutputStream()
            c.compress(Bitmap.CompressFormat.JPEG,100, imagenbyte1)
            d.compress(Bitmap.CompressFormat.JPEG,100, imagenbyte2)
            val ruta1 = MediaStore.Images.Media.insertImage(contentResolver,c,"Title",null)
            val ruta2 = MediaStore.Images.Media.insertImage(contentResolver,d,"Title",null)
            val uriimagen1 = Uri.parse(ruta1)
            val uriimagen2 = Uri.parse(ruta2)
            val uriArray:ArrayList<Uri> = arrayListOf(uriimagen1,uriimagen2)
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArray)
            type = "image/*"
        }
        startActivity(Intent.createChooser(intento,null))
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {

            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

}
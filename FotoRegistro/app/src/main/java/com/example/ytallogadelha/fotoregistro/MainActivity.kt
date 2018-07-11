package com.example.ytallogadelha.fotoregistro

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import android.widget.ImageView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BitmapFactory
import android.view.View
import android.widget.Button
import android.widget.Toast
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_TAKE_PHOTO = 1
    lateinit var mImageView: ImageView
    lateinit var mCurrentPhotoPath: String
    lateinit var botaoCaptura: Button
    lateinit var photoURI: Uri
    var photoFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mImageView = findViewById(R.id.image_foto)
        botaoCaptura = findViewById(R.id.button_foto)

        botaoCaptura.setOnClickListener(View.OnClickListener {

            dispatchTakePictureIntent()
        })

    }

    private fun dispatchTakePictureIntent() {

        val takePictureIntent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(packageManager) != null) {

            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            println("${requestCode} e ${resultCode} e ${Activity.RESULT_OK} e ${data}")

            val extras = data?.extras

            val imageBitmap = extras!!.get("data") as Bitmap
            mImageView.setImageBitmap(imageBitmap)

            salvarBitmap(imageBitmap)
            Toast.makeText(this, "Imagem salva!!!", Toast.LENGTH_LONG).show()
        }
    }

    private fun salvarBitmap( bitmap: Bitmap){

        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName: String = "JPG_" + timeStamp + ".png"
        val dir: File = Environment.getExternalStorageDirectory()
        val destino = File(dir, imageFileName)

        try {

            val out = FileOutputStream(destino)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()

        }catch ( ex: IOException){
            println("Erro ao salvar a imagem -> ${ex}")
        }
    }
}
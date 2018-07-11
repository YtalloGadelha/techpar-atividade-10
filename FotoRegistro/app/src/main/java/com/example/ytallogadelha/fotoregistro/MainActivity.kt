package com.example.ytallogadelha.fotoregistro

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.widget.ImageView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Matrix
import android.view.View
import android.widget.Button
import android.widget.Toast
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_TAKE_PHOTO = 1
    lateinit var mImageView: ImageView
    lateinit var botaoCaptura: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //referencinando os componentes da interface
        mImageView = findViewById(R.id.image_foto)
        botaoCaptura = findViewById(R.id.button_foto)

        botaoCaptura.setOnClickListener(View.OnClickListener {

            dispatchTakePictureIntent()
        })
    }

    //método que delega a atividade de tirar foto para o aplicativo da câmera
    private fun dispatchTakePictureIntent() {

        val takePictureIntent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(packageManager) != null) {

            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
        }
    }

    //método chamado quando o aplicativo manda a foto de volta
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            //recuperando a foto por meio da intent
            val extras = data?.extras

            val imageBitmap = extras!!.get("data") as Bitmap

            //rotacionando a imagem
            var imagemRotacionada = rotateBitmap(imageBitmap, 90)

            //setando a imagem na imageView
            mImageView.setImageBitmap(imagemRotacionada)

            salvarBitmap(imagemRotacionada)
            Toast.makeText(this, "Imagem salva!!!", Toast.LENGTH_LONG).show()
        }
    }

    //método que salva o imagem como JPEG
    private fun salvarBitmap( bitmap: Bitmap){

        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName: String = "JPEG" + timeStamp + ".jpeg"
        val dir: File = Environment.getExternalStorageDirectory()
        val destino = File(dir, imageFileName)

        try {

            val out = FileOutputStream(destino)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()

        }catch ( ex: IOException){
            println("Erro ao salvar a imagem -> ${ex}")
        }
    }

    //método para rotacionar o bitmap
    private fun rotateBitmap(original: Bitmap, degrees: Int): Bitmap {
        val width = original.width
        val height = original.height

        val matrix = Matrix()
        matrix.preRotate(degrees.toFloat())

        val rotatedBitmap = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true)

        return rotatedBitmap
    }
}
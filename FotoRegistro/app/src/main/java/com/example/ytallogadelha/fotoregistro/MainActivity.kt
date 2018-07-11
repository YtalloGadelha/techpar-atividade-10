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
    val REQUEST_PICK_IMAGE = 1234
    lateinit var imageView: ImageView
    lateinit var botaoCaptura: Button
    lateinit var botaoSelecionar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //referencinando os componentes da interface
        imageView = findViewById(R.id.image_foto)
        botaoCaptura = findViewById(R.id.button_captura)
        botaoSelecionar = findViewById(R.id.button_selecionar)

        //configurando o botão capturar(foto com a câmera)
        botaoCaptura.setOnClickListener(View.OnClickListener {

            capturarFoto()
        })

        //configurando o botão selecionar(foto da galeria)
        botaoSelecionar.setOnClickListener(View.OnClickListener {

            recuperarFoto()
        })
    }

    //método que delega a atividade de tirar foto para o aplicativo da câmera
    private fun capturarFoto() {

        //intent que delega a atividade para a câmera
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (cameraIntent.resolveActivity(packageManager) != null) {

            startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO)
        }
    }

    private fun recuperarFoto(){

        //intent que acessa a galeria
        val galeriaIntent  =  Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI)

        startActivityForResult(Intent.createChooser(galeriaIntent, "Selecione uma imagem"), REQUEST_PICK_IMAGE)

    }

    //método chamado quando o aplicativo manda a foto de volta
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            //recuperando a foto por meio da intent
            val extras = data?.extras

            val imageBitmap = extras!!.get("data") as Bitmap

            //rotacionando a imagem
            val imagemRotacionada = rotacionarBitmap(imageBitmap, 90)

            //setando a imagem na imageView
            imageView.setImageBitmap(imagemRotacionada)

            salvarBitmap(imagemRotacionada)
            Toast.makeText(this, "Imagem salva!!!", Toast.LENGTH_LONG).show()
        }

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK){

            //recuperando a foto selecionada por meio da intent
            val imagemSelecionada = data!!.data

            //setando a imagem na imageView
            imageView.setImageURI(imagemSelecionada)
        }
    }

    //método que salva o imagem como JPEG
    private fun salvarBitmap( bitmap: Bitmap){

        //criando um filename
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName: String = "PNG" + timeStamp + ".png"

        //criando o diretório
        val diretorio: File = Environment.getExternalStorageDirectory()
        val destino = File(diretorio, imageFileName)

        try {

            //criando o outputstream para salvar a imagem
            val saida = FileOutputStream(destino)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, saida)
            saida.flush()
            saida.close()

        }catch ( ex: IOException){
            println("Erro ao salvar a imagem -> ${ex}")
        }
    }

    //método para rotacionar o bitmap
    private fun rotacionarBitmap(original: Bitmap, degrees: Int): Bitmap {
        val largura = original.width
        val altura = original.height

        val matrix = Matrix()
        matrix.preRotate(degrees.toFloat())

        val bitmapRotacionado = Bitmap.createBitmap(original, 0, 0, largura, altura, matrix, true)

        return bitmapRotacionado
    }
}
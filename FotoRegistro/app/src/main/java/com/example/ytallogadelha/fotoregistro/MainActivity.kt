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

//            // Create the File where the photo should go
//
//
//            try {
//                photoFile = createImageFile()
//
//            } catch (ex: IOException) {
//                // Error occurred while creating the File
//
//            }
//
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//
//                photoURI = FileProvider.getUriForFile(applicationContext,
//                        "com.example.ytallogadelha.fotoregistro.fileprovider",
//                        photoFile!!)
//
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//
//                //println("photoFile -> ${photoFile!!.absolutePath} ")
//
//                //println("photoURI -> ${photoURI}")
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
//
//            }

            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            println("${requestCode} e ${resultCode} e ${Activity.RESULT_OK} e ${data}")

            //parâmetro data está vindo nulo!!!
            val extras = data?.extras

           //println("Passa aqui?")
            val imageBitmap = extras!!.get("data") as Bitmap
            mImageView.setImageBitmap(imageBitmap)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {

        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath()
        println("mCurrentPhotoPath -> ${mCurrentPhotoPath}")
        return image
    }

    private fun galleryAddPic() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
    }

    private fun setPic() {
        // Get the dimensions of the View
        val targetW = mImageView.width
        val targetH = mImageView.height

        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        // Determine how much to scale down the image
        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true

        val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
        mImageView.setImageBitmap(bitmap)
    }

}

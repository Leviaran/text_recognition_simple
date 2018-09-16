package com.example.ran.scantext

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import com.bumptech.glide.request.transition.Transition
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*

const val CAMERA_REQUEST = 1
const val PICK_FROM_GALLERY = 2


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val TAG = "MainActivity"
    private var mImageView: ImageView? = null
    private var mButton: Button? = null
    private var mCloudButton: Button? = null
    private var mSelectedImage: Bitmap? = null
    private var mGraphicOverlay: GraphicOverlay? = null
    private var mImageMaxWidth: Int? = null
    private var mImageMaxHeight: Int? = null

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        Log.e("permission", "granted")
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {

                    }

                })
                .withErrorListener { error -> Log.e("Dexter", "There was an error: " + error.toString()) }.check()
        button.visibility = View.GONE

        button.setOnClickListener {
            runTextRecognizer()
        }

        imageView2.setOnClickListener {
            val option = arrayOf("Ambil dari Kamera", "Ambil dari Gallery")
            val adapter = ArrayAdapter(this,
                    android.R.layout.select_dialog_item, option)
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Option")
            builder.setAdapter(adapter) { _, which ->
                if (which == 0) {
                    callCamera()
                }
                if (which == 1) {
                    callGallery()
                }
            }
            val dialog = builder.create()
            dialog.show()
        }

    }

    fun runTextRecognizer(){
        var image = FirebaseVisionImage.fromBitmap(mSelectedImage!!)
        var detector = FirebaseVision.getInstance()
                .onDeviceTextRecognizer
        button.isEnabled = false
        detector.processImage(image)
                .addOnSuccessListener {
                    button.isEnabled = true
                    processTextRecognizeResult(it)

                }.addOnFailureListener {
                    button.isEnabled = true
                    Log.e("Failed",it.message)
                }
    }

    fun processTextRecognizeResult(text : FirebaseVisionText){
        var block = text.textBlocks
        when (block.size){
            0 -> Toast.makeText(this, "No Text", Toast.LENGTH_SHORT).show()
            else -> textView.text = text.text
        }
    }

    fun callCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST)
    }

    /**
     * open gallery method
     */

    fun callGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
                Intent.createChooser(intent, "Lanjutkan Aksi dengan"),
                PICK_FROM_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
        Log.e("Result Code", resultCode.toString())

        if (resultCode != RESULT_OK)
            return

        when (requestCode) {
            CAMERA_REQUEST -> {

                val extras = data?.getData()
                Log.e("string data", "goinng")
                if (extras != null) {

                    val imageUri = data.getData()

                    Glide.with(this)
                            .load(imageUri)
                            .apply(RequestOptions.centerCropTransform())
                            .into(imageView)

                    Glide.with(this).asBitmap().load(imageUri).into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            mSelectedImage = resource
                            button.visibility = View.VISIBLE
                        }

                    })

                }
            }

            PICK_FROM_GALLERY -> {

                val extras2 = data?.data

                if (extras2 != null) {

                    val imageUri = data.data
                    Glide.with(this)
                            .load(imageUri)
                            .apply(RequestOptions.centerCropTransform())
                            .into(imageView)


                    try {
                        Glide.with(this).asBitmap().load(imageUri).into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                mSelectedImage = resource
                                button.visibility = View.VISIBLE
                            }
                        })

                    } catch (e: Exception) {
                        Log.e("Failed", e.message)
                    }

                }
            }
        }
    }
}

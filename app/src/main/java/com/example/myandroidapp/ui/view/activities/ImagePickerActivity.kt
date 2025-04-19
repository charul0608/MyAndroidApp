package com.example.myandroidapp.ui.view.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import com.example.myandroidapp.R

class ImagePickerActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private val REQUEST_CAMERA = 100
    private val REQUEST_GALLERY = 200
    private val REQUEST_PERMISSIONS = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)

        imageView = findViewById(R.id.imageView)
        val btnCamera = findViewById<Button>(R.id.btnCamera)
        val btnGallery = findViewById<Button>(R.id.btnGallery)

        btnCamera.setOnClickListener {
            if (checkPermissions()) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_CAMERA)
            }
        }

        btnGallery.setOnClickListener {
            if (checkPermissions()) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_GALLERY)
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val cameraPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePerm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return if (cameraPerm != PackageManager.PERMISSION_GRANTED || storagePerm != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSIONS
            )
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    val bitmap = data.extras?.get("data") as? Bitmap
                    imageView.setImageBitmap(bitmap)
                }
                REQUEST_GALLERY -> {
                    val selectedImage: Uri? = data.data
                    imageView.setImageURI(selectedImage)
                }
            }
        }
    }
}
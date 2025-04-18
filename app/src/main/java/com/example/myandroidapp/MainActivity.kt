package com.example.myandroidapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myandroidapp.ui.view.activities.ApiListActivity
import com.example.myandroidapp.ui.view.activities.ImagePickerActivity
import com.example.myandroidapp.ui.view.activities.PdfViewerActivity
import com.example.myandroidapp.ui.view.activities.SignInActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()


        findViewById<Button>(R.id.imagePicker).setOnClickListener {
            startActivity(Intent(this, ImagePickerActivity::class.java))
        }

        findViewById<Button>(R.id.pdfView).setOnClickListener {
            startActivity(Intent(this, PdfViewerActivity::class.java))
        }


        findViewById<Button>(R.id.roomDb).setOnClickListener {
            startActivity(Intent(this, ApiListActivity::class.java))
        }


        findViewById<Button>(R.id.signOutBtn).setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}
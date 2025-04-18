package com.example.myandroidapp

//import android.app.Activity
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Button
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.tasks.Task
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var auth : FirebaseAuth
//    private lateinit var googleSignInClient : GoogleSignInClient
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        auth = FirebaseAuth.getInstance()
//
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(this , gso)
//
//        findViewById<Button>(R.id.gSignInBtn).setOnClickListener {
//            signInGoogle()
//        }
//    }
//
//    private fun signInGoogle(){
//        val signInIntent = googleSignInClient.signInIntent
//        launcher.launch(signInIntent)
//    }
//
//    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//            result ->
//        if (result.resultCode == Activity.RESULT_OK){
//
//            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            handleResults(task)
//        }
//    }
//
//    private fun handleResults(task: Task<GoogleSignInAccount>) {
//        if (task.isSuccessful){
//            val account : GoogleSignInAccount? = task.result
//            if (account != null){
//                updateUI(account)
//            }
//        }else{
//            Toast.makeText(this, task.exception.toString() , Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun updateUI(account: GoogleSignInAccount) {
//        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
//        auth.signInWithCredential(credential).addOnCompleteListener {
//            if (it.isSuccessful){
//                val intent : Intent = Intent(this , HomeActivity::class.java)
//                intent.putExtra("email" , account.email)
//                intent.putExtra("name" , account.displayName)
//                startActivity(intent)
//            }else{
//                Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()
//
//            }
//        }
//    }
//
//
//}

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myandroidapp.ui.theme.MyAndroidAppTheme
import com.example.myandroidapp.ui.view.activities.ApiListActivity
import com.example.myandroidapp.ui.view.activities.ImagePickerActivity
import com.example.myandroidapp.ui.view.activities.LoginActivity
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
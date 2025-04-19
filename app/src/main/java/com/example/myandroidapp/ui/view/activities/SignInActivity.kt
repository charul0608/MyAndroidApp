package com.example.myandroidapp.ui.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myandroidapp.MainActivity
import com.example.myandroidapp.R

import com.example.myandroidapp.databinding.SignInActivityBinding
import com.example.myandroidapp.ui.view.User
import com.example.myandroidapp.ui.view.FireStoreClass
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : BaseActivity() {
    private var binding:SignInActivityBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignInActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        Log.d("WebClientID", getString(R.string.default_web_client_id))


        binding?.btnSignInWithGoogle?.setOnClickListener {
            sinInWithGoogle()
        }
    }


    private fun sinInWithGoogle()
    {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d("SignIn", "Result code: ${result.resultCode}, data: ${result.data}")
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        } else {
            Log.d("SignIn", "Google Sign-In intent failed or cancelled")
            Toast.makeText(this, "Google Sign-In cancelled", Toast.LENGTH_SHORT).show()
        }
    }


    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Log.e("SignIn", "Google sign-in failed", task.exception)
            Toast.makeText(this, "Google sign-in failed: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }


    private fun updateUI(account: GoogleSignInAccount) {
        showProgressBar()
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful)
            {
                val id = FireStoreClass().getCurrentUserId()
                val name = account.displayName.toString()
                val email = account.email.toString()
                val userInfo = User(id, name, email)
                FireStoreClass().registerUser(userInfo)
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            else
            {
                Toast.makeText(this,"Sign In Failed, try again.",Toast.LENGTH_SHORT).show()
            }
            hideProgressBar()
        }
    }

}



package com.example.myandroidapp.ui.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myandroidapp.FirebaseAuthHelper
import com.example.myandroidapp.MainActivity
import com.example.myandroidapp.R
import com.example.myandroidapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            FirebaseAuthHelper.signInWithGoogle(
                context = this,
                account = account,
                onSuccess = { user ->
                    // Save to Room and go to MainActivity
                },
                onError = { error ->
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                }
            )

        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Sign-in error", e)
            Log.d("CLIENT_ID", getString(R.string.default_web_client_id))
            Toast.makeText(this, "Google sign-in failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

//    private fun signInWithFirebase(credential: AuthCredential) {
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = auth.currentUser
//                    user?.let {
//                        val userEntity = UserEntity(
//                            uid = it.uid,
//                            name = it.displayName ?: "No Name",
//                            email = it.email ?: "No Email"
//                        )
//
//                        CoroutineScope(Dispatchers.IO).launch {
//                            AppDatabase.getDatabase(applicationContext).userDao().insertUser(userEntity)
//                        }
//
//                        Toast.makeText(this, "Signed in as ${it.displayName}", Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this, MainActivity::class.java))
//                        finish()
//                    }
//                } else {
//                    Toast.makeText(this, "Firebase Auth failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            // User already signed in, go straight to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnGoogleSignIn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = auth.currentUser
//                    user?.let {
//                        val userEntity = UserEntity(
//                            uid = it.uid,
//                            name = it.displayName ?: "No Name",
//                            email = it.email ?: "No Email"
//                        )
//
//                        CoroutineScope(Dispatchers.IO).launch {
//                            AppDatabase.getDatabase(applicationContext).userDao().insertUser(userEntity)
//                        }
//
//                        Toast.makeText(this, "Signed in as ${it.displayName}", Toast.LENGTH_SHORT).show()
//
//                        // Proceed to MainActivity or next screen
//                        startActivity(Intent(this, MainActivity::class.java))
//                        finish()
//                    }
//                } else {
//                    Toast.makeText(this, "Firebase Auth failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
}

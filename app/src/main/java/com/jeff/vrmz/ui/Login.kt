package com.jeff.vrmz.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.jeff.vrmz.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null){
            startActivity(Intent(this@Login, Verification::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, Sign_up::class.java))
            finish()
        }
    }

    private fun checkIfUserIsLogged(){

        if (auth.currentUser != null){
            startActivity(Intent(this, Registration::class.java))
            finish()
        }
    }

    private fun login() {

        val email = binding.edEmail.text.toString()
        val password = binding.edPass.text.toString()

        auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this@Login,"login successful",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Login, Verification::class.java))
                    finish()
                } else{
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }

    }
}
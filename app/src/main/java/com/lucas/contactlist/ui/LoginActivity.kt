package com.lucas.contactlist.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lucas.contactlist.R
import com.lucas.contactlist.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val alb: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val signInCoroutineScope = CoroutineScope(Dispatchers.IO)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(alb.root)

        setSupportActionBar(alb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.login)

        alb.signUpBt.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        alb.signInBt.setOnClickListener {
            signInCoroutineScope.launch {
                Firebase.auth.signInWithEmailAndPassword(
                    alb.emailLoginEt.text.toString(),
                    alb.passwordLoginEt.text.toString()
                ).addOnFailureListener {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login failed. Cause ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnSuccessListener {
                    openMainActivity()
                }
            }
        }

        alb.resetPasswordBt.setOnClickListener {
            signInCoroutineScope.launch {
                val email = alb.emailLoginEt.text.toString()
                if (email.isNotEmpty()) {
                    Firebase.auth.sendPasswordResetEmail(email)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null) {
            openMainActivity()
        }
    }

    private fun openMainActivity() {
        startActivity(
            Intent(this@LoginActivity, MainActivity::class.java)
        )
        finish()
    }
}
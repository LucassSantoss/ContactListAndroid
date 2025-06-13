package com.lucas.contactlist.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.GoogleAuthProvider
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

        alb.googleSignInBt.setOnClickListener {
            // Cria um googleOptionId
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(true)
                .build()

            // Cria uma requisição que vai ser usada num Gerenciador de Credenciais usando as opções
            // do GoogleIdOption
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            // Cria o gerenciador de requisições associado ao contexto que vai fazer a requisição
            // ao servidor de autenticação
            val credentialManager = CredentialManager.create(this)
            signInCoroutineScope.launch {
                try {
                    // Enviar a requisição usando o gerenciador de credenciais
                    val result = credentialManager.getCredential(this@LoginActivity, request)
                    handleSignIn(result.credential)
                } catch (e: GetCredentialException) {
                    runOnUiThread {
                        Toast.makeText(
                            this@LoginActivity,
                            "Authentication failed. Cause: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun handleSignIn(credential: Credential) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Cria um token a partir dos dados da credencial
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            // Extrai credenciais para autenticação no Firebase
            val credential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
            Firebase.auth.signInWithCredential(credential).addOnFailureListener {
                Toast.makeText(
                    this@LoginActivity,
                    "Login failed. Cause ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnSuccessListener {
                openMainActivity()
            }
        } else {
            Toast.makeText(
                this,
                "Credential is not Google ID!",
                Toast.LENGTH_SHORT
            ).show()

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
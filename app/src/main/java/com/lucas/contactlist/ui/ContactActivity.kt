package com.lucas.contactlist.ui

import com.lucas.contactlist.model.Constant.EXTRA_CONTACT
import com.lucas.contactlist.model.Contact
import com.lucas.contactlist.databinding.ActivityContactBinding

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lucas.contactlist.model.Constant.EXTRA_VIEW_CONTACT

class ContactActivity : AppCompatActivity() {
    private val acb: ActivityContactBinding by lazy {
        ActivityContactBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)

        setSupportActionBar(acb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "New contact"

        val receveidContact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_CONTACT, Contact::class.java)
        }
        else {
            intent.getParcelableExtra<Contact>(EXTRA_CONTACT)
        }
        receveidContact?.let{
            supportActionBar?.subtitle = "Edit contact"
            with(acb) {
                nameEt.setText(it.name)
                addressEt.setText(it.address)
                phoneEt.setText(it.phone)
                emailEt.setText(it.email)

                // Verificando se é só uma visualização de contato
                val viewContact = intent.getBooleanExtra(EXTRA_VIEW_CONTACT, false)
                if (viewContact) {
                    supportActionBar?.subtitle = "View contact"
                    nameEt.isEnabled = false
                    addressEt.isEnabled = false
                    phoneEt.isEnabled = false
                    emailEt.isEnabled = false
                    saveBt.visibility = View.GONE
                }
            }
        }

        with(acb) {
            saveBt.setOnClickListener {
                Contact(
                    receveidContact?.id?:hashCode(),
                    nameEt.text.toString(),
                    addressEt.text.toString(),
                    phoneEt.text.toString(),
                    emailEt.text.toString()
                ).let { contact ->
                    Intent().apply {
                        putExtra(EXTRA_CONTACT, contact)
                        setResult(RESULT_OK, this)
                    }
                }
                finish()
            }
        }
    }
}
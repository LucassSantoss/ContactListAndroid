package com.lucas.contactlist.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.lucas.contactlist.R
import com.lucas.contactlist.adapter.ContactAdapter
import com.lucas.contactlist.databinding.ActivityMainBinding
import com.lucas.contactlist.model.Constant.EXTRA_CONTACT
import com.lucas.contactlist.model.Constant.EXTRA_VIEW_CONTACT
import com.lucas.contactlist.model.Contact
import com.lucas.contactlist.ui.ContactActivity

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val contactList: MutableList<Contact> = mutableListOf()

    // Adapter
    private val contactAdapter: ContactAdapter by lazy {
        ContactAdapter(this, contactList)
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)
        supportActionBar?.subtitle = getString(R.string.contact_list)

        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val contact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(EXTRA_CONTACT, Contact::class.java)
                }
                else {
                    result.data?.getParcelableExtra<Contact>(EXTRA_CONTACT)
                }
                contact?.let{ receivedContact ->
                    // Verificar se é um novo contato ou se é um contato editado.
                    val position = contactList.indexOfFirst { it.id == receivedContact.id }
                    if (position == -1) {
                        contactList.add(receivedContact)
                    }
                    else {
                        contactList[position] = receivedContact
                    }
                    contactAdapter.notifyDataSetChanged()
                }
            }
        }

        registerForContextMenu(amb.contactLv)

        amb.contactLv.onItemClickListener = object: AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Intent(this@MainActivity, ContactActivity::class.java).apply {
                    putExtra(EXTRA_CONTACT, contactList[position])
                    putExtra(EXTRA_VIEW_CONTACT, true)
                    startActivity(this)
                }
            }
        }

        amb.contactLv.adapter = contactAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.add_contact_mi -> {
                carl.launch(Intent(this, ContactActivity::class.java))
                true
            }
            else -> { false }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position

        return when(item.itemId) {
            R.id.remove_contact_mi -> {
                contactList.removeAt(position)
                contactAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Contact removed!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.edit_contact_mi -> {
                Intent(this, ContactActivity::class.java).apply {
                    putExtra(EXTRA_CONTACT, contactList[position])
                    carl.launch(this)
                }
                true
            }
            else -> { false }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterForContextMenu(amb.contactLv)
    }
}
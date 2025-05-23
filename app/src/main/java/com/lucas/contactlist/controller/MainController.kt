package com.lucas.contactlist.controller

import android.os.Message
import androidx.room.Room
import com.lucas.contactlist.model.Constant.EXTRA_CONTACT_ARRAY
import com.lucas.contactlist.model.Contact
import com.lucas.contactlist.model.ContactDao
import com.lucas.contactlist.model.ContactFirebaseDatabase
import com.lucas.contactlist.model.ContactRoomDb
import com.lucas.contactlist.model.ContactSqlite
import com.lucas.contactlist.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainController(private val mainActivity: MainActivity) {
//    private val contactDao: ContactDao = Room.databaseBuilder(
//        mainActivity, ContactRoomDb::class.java, "contact-database"
//    ).build().contactDao()
    private val contactDao: ContactDao = ContactFirebaseDatabase()
    private val databaseCoroutineScope = CoroutineScope(Dispatchers.IO)

    fun insertContact(contact: Contact) {
        databaseCoroutineScope.launch{
            contactDao.createContact(contact)
        }
    }

    fun getContact(id: Int) = contactDao.retrieveContact(id)

    fun getContacts() {
        databaseCoroutineScope.launch {
            val contactList = contactDao.retrieveContacts()
            mainActivity.getContactsHandler.sendMessage(Message().apply {
                data.putParcelableArray(EXTRA_CONTACT_ARRAY, contactList.toTypedArray())
            })
        }
    }

    fun modifyContact(contact: Contact) {
        databaseCoroutineScope.launch {
            contactDao.updateContact(contact)
        }
    }

    fun removeContact(contact: Contact) {
        databaseCoroutineScope.launch {
            contactDao.deleteContact(contact)
        }
    }
}
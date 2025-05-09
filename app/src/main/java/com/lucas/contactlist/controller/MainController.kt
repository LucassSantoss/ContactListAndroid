package com.lucas.contactlist.controller

import androidx.room.Room
import com.lucas.contactlist.model.Contact
import com.lucas.contactlist.model.ContactDao
import com.lucas.contactlist.model.ContactRoomDb
import com.lucas.contactlist.model.ContactSqlite
import com.lucas.contactlist.ui.MainActivity

class MainController(mainActivity: MainActivity) {
    // private val contactDao: ContactDao = ContactSqlite(mainActivity)
    private val contactDao: ContactDao = Room.databaseBuilder(
        mainActivity, ContactRoomDb::class.java, "contact-database"
    ).build().contactDao()

    fun insertContact(contact: Contact) {
        Thread{
            contactDao.createContact(contact)
        }.start()
    }
    fun getContact(id: Int) = contactDao.retrieveContact(id)
    fun getContacts() =  contactDao.retrieveContacts()
    fun modifyContact(contact: Contact) = contactDao.updateContact(contact)
    fun removeContact(contact: Contact) = contactDao.deleteContact(contact)
}
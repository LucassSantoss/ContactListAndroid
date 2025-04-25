package com.lucas.contactlist.controller

import com.lucas.contactlist.model.Contact
import com.lucas.contactlist.model.ContactDao
import com.lucas.contactlist.model.ContactSqlite
import com.lucas.contactlist.ui.MainActivity

class MainController(mainActivity: MainActivity) {
    private val contactDao: ContactDao = ContactSqlite(mainActivity)

    fun insertContact(contact: Contact) = contactDao.createContact(contact)
    fun getContact(id: Int) = contactDao.retrieveContact(id)
    fun getContacts() =  contactDao.retrieveContacts()
    fun modifyContact(contact: Contact) = contactDao.updateContact(contact)
    fun removeContact(id: Int) = contactDao.deleteContact(id)
}
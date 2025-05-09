package com.lucas.contactlist.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.lucas.contactlist.R
import java.sql.SQLException

class ContactSqlite(context: Context): ContactDao {
    companion object {
        private val CONTACT_DATABASE_FILE = "contactList"
        private val CONTACT_TABLE = "contact"
        private val ID_COLUMN = "id"
        private val NAME_COLUMN = "name"
        private val ADDRESS_COLUMN = "address"
        private val PHONE_COLUMN = "phone"
        private val EMAIL_COLUMN = "email"

        val CREATE_CONTACT_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS $CONTACT_TABLE ( " +
                "$ID_COLUMN INTEGER NOT NULL PRIMARY KEY, " +
                "$NAME_COLUMN TEXT NOT NULL, " +
                "$ADDRESS_COLUMN TEXT NOT NULL, " +
                "$PHONE_COLUMN TEXT NOT NULL, " +
                "$EMAIL_COLUMN TEXT NOT NULL );"
    }

    private val contactDatabase: SQLiteDatabase = context.openOrCreateDatabase(
        CONTACT_DATABASE_FILE,
        MODE_PRIVATE,
        null
    )

    init {
        try {
            contactDatabase.execSQL(CREATE_CONTACT_TABLE_STATEMENT)
        }
        catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.message.toString())
        }
    }

    override fun createContact(contact: Contact) =
        contactDatabase.insert(CONTACT_TABLE, null, contact.toContentValues())

    override fun retrieveContact(id: Int): Contact {
        val cursor = contactDatabase.query(
            true,
            CONTACT_TABLE,
            null,
            "$ID_COLUMN = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            cursor.toContact()
        } else {
            Contact()
        }
    }

    override fun retrieveContacts(): MutableList<Contact> {
        val contactList: MutableList<Contact> = mutableListOf()

        val cursor = contactDatabase.rawQuery("SELECT * FROM $CONTACT_TABLE", null)

        while (cursor.moveToNext()) {
            contactList.add(cursor.toContact())
        }

        return contactList
    }

    override fun updateContact(contact: Contact): Int = contactDatabase.update(
        CONTACT_TABLE,
        contact.toContentValues(),
        "$ID_COLUMN = ?",
        arrayOf(contact.id.toString())
    )

    override fun deleteContact(contact: Contact): Int = contactDatabase.delete(
        CONTACT_TABLE,
        "$ID_COLUMN = ?",
        arrayOf(contact.id.toString())
    )

    private fun Contact.toContentValues() = ContentValues().apply {
        put(ID_COLUMN, id)
        put(NAME_COLUMN, name)
        put(ADDRESS_COLUMN, address)
        put(PHONE_COLUMN, phone)
        put(EMAIL_COLUMN, email)
    }

    private fun Cursor.toContact() = Contact(
        getInt(getColumnIndexOrThrow(ID_COLUMN)),
        getString(getColumnIndexOrThrow(NAME_COLUMN)),
        getString(getColumnIndexOrThrow(ADDRESS_COLUMN)),
        getString(getColumnIndexOrThrow(PHONE_COLUMN)),
        getString(getColumnIndexOrThrow(EMAIL_COLUMN))
    )
}
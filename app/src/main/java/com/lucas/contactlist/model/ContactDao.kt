package com.lucas.contactlist.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContactDao {
    @Insert
    fun createContact(contact: Contact): Long
    @Query("SELECT * FROM Contact WHERE id = :id")
    fun retrieveContact(id: Int): Contact
    @Query("SELECT * FROM Contact")
    fun retrieveContacts(): MutableList<Contact>
    @Update
    fun updateContact(contact: Contact): Int
    @Delete
    fun deleteContact(contact: Contact): Int
}
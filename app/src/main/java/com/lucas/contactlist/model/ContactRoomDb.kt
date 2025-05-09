package com.lucas.contactlist.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Contact::class], version = 1)
abstract class ContactRoomDb: RoomDatabase() {
    abstract fun contactDao(): ContactDao
}
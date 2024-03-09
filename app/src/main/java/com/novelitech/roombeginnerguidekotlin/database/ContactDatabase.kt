package com.novelitech.roombeginnerguidekotlin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.novelitech.roombeginnerguidekotlin.daos.contact.ContactDao
import com.novelitech.roombeginnerguidekotlin.entities.ContactEntity

@Database(
    entities = [
        ContactEntity::class
    ],
    version = 1,
)
abstract class ContactDatabase: RoomDatabase() {

    // Since this is abstract the Room will define it for me
    abstract val dao: ContactDao
}
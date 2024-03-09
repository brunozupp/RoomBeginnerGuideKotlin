package com.novelitech.roombeginnerguidekotlin.daos.contact

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.novelitech.roombeginnerguidekotlin.entities.ContactEntity
import kotlinx.coroutines.flow.Flow

// Because of the annotations Room will define the functions for me

@Dao
interface ContactDao {

    @Upsert
    suspend fun upsert(contact: ContactEntity)

    @Delete
    suspend fun delete(contact: ContactEntity)

    // Using Flow when the table changes it will emit a new list of contacts
    @Query("SELECT * FROM contacts ORDER BY firstName ASC")
    fun getOrderedByFirstName(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts ORDER BY lastName ASC")
    fun getOrderedByLastName(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts ORDER BY phoneNumber ASC")
    fun getOrderedByPhoneNumber(): Flow<List<ContactEntity>>
}
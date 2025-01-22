package com.example.campussafetyapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactDao {
    //@Query("SELECT * FROM contacts")
    @Query("SELECT * FROM contacts ORDER BY name ASC") // Sort by name in ascending order
    fun getAllContacts(): List<Contact>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertContact(contact: List<Contact>)

    @Query("DELETE FROM contacts WHERE id = :id")
    fun deleteContactById(id: Int)

    @Delete
    fun deleteContact(contact: Contact) // Add this method to delete a contact
}

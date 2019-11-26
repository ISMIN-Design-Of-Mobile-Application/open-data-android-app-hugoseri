package com.ismin.opendataapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Item)

    @Query("SELECT * FROM item")
    fun getAll(): List<Item>

    @Query("DELETE FROM item")
    fun deleteAllInDatabase()
}
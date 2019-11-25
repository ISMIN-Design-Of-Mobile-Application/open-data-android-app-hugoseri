package com.ismin.opendataapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {
    @Insert
    fun insert(item: Item)

    @Query("SELECT * FROM item")
    fun getAll(): ArrayList<Item>
}
package com.ismin.opendataapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1)
abstract class AppDataBase : RoomDatabase(){
    abstract fun getItemDao(): ItemDao

    companion object {
        private var INSTANCE: AppDataBase? = null

        fun getAppDatabase(context: Context): AppDataBase {
            if (INSTANCE == null) {
                synchronized(AppDataBase::class) {
                    INSTANCE = Room
                        .databaseBuilder(context.applicationContext,
                            AppDataBase::class.java,
                            "item")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as AppDataBase
        }
    }

}
package com.example.pencatatansembako

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Sembako::class], version = 1)
abstract class SembakoDatabase : RoomDatabase() {

    abstract fun sembakoDao(): SembakoDao

    companion object {
        private var INSTANCE: SembakoDatabase? = null

        fun getInstance(context: Context): SembakoDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SembakoDatabase::class.java, "sembako_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}

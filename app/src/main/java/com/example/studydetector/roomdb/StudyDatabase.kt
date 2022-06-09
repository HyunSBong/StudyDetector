package com.example.studydetector.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StudyTable::class], version = 1)
abstract class StudyDatabase:RoomDatabase() {
    abstract fun studyDAO(): StudyDAO

    companion object {
        private var instance: StudyDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): StudyDatabase? {
            if (instance == null) {
                synchronized(StudyDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        StudyDatabase::class.java,
                        "study-database"
                    ).build()
                }
            }
            return instance
        }
    }
}
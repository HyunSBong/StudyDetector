package com.example.studydetector.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "studyTable")
class StudyTable(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "goalTime") val goalTime: String?,
    @ColumnInfo(name = "studyTime") val studyTime: String?,
)
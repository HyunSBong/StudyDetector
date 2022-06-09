package com.example.studydetector.roomdb

import androidx.room.*

@Dao
interface StudyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(studyTable: StudyTable)

    @Update
    fun update(studyTable: StudyTable)

    @Delete
    fun delete(studyTable: StudyTable)

    @Query("SELECT * From StudyTable")
    fun selectAll(): MutableList<StudyTable>

    @Query("DELETE FROM StudyTable WHERE name = :name")
    fun deleteByName(name: String)

    @Query("SELECT * FROM StudyTable WHERE date = :input_date")
    fun selectByDate(input_date: String): MutableList<StudyTable>

    @Query("SELECT name,studyTime FROM StudyTable WHERE date = :input_date")
    fun selectTimeByDate(input_date: String): MutableList<StudyTable>

    @Query("SELECT * FROM StudyTable ORDER BY date ASC")
    fun getOrderedList(): MutableList<StudyTable>
}
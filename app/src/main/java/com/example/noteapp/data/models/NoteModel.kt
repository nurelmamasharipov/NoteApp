package com.example.noteapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "noteModel")
data class NoteModel(
    val title: String,
    val description: String,
    val date: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM HH:mm"))
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
package com.example.notes.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val time: Long = System.currentTimeMillis()
) {
    fun getFormattedTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        val date = Date(time)
        return dateFormat.format(date)
    }
}
package com.example.digitaldiary

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "diary_entries") // The "Sticker" for the Database
data class DiaryEntry(
    @PrimaryKey val id: String = UUID.randomUUID().toString(), // The Unique ID
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val mood: String? = null
)
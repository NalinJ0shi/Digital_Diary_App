package com.example.digitaldiary

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "diary_entries")
data class DiaryEntry(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(), //
    val title: String, //
    val content: String, //
    val timestamp: Long = System.currentTimeMillis(), //
    val mood: String? = null, //
    val moodEmoji: String = "😊" // NEW: This is the only line we're adding!
)
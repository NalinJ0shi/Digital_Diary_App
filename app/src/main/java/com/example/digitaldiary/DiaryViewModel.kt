package com.example.digitaldiary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = DiaryDatabase.getDatabase(application).diaryDao()
    val allEntries: Flow<List<DiaryEntry>> = dao.getAllEntries()

    val latestEntry: Flow<DiaryEntry?> = allEntries.map { list ->
        list.maxByOrNull { it.timestamp }
    }

    fun saveEntry(
        title: String,
        content: String,
        date: Long,
        existingEntry: DiaryEntry? = null,
        dayRating: Int
    ) {
        viewModelScope.launch {
            // FIX: dayRating is now actually being saved to your database!
            val entry = existingEntry?.copy(
                title = title,
                content = content,
                timestamp = date,
                dayRating = dayRating
            ) ?: DiaryEntry(
                title = title,
                content = content,
                timestamp = date,
                dayRating = dayRating
            )
            dao.insertEntry(entry)
        }
    }

    fun delete(entry: DiaryEntry) {
        viewModelScope.launch { dao.deleteEntry(entry) }
    }

    fun getEntryForDate(dateInMillis: Long, entries: List<DiaryEntry>): DiaryEntry? {
        return entries.find { isSameDay(it.timestamp, dateInMillis) }
    }

    private fun isSameDay(t1: Long, t2: Long): Boolean {
        val c1 = Calendar.getInstance().apply { timeInMillis = t1 }
        val c2 = Calendar.getInstance().apply { timeInMillis = t2 }
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }
}
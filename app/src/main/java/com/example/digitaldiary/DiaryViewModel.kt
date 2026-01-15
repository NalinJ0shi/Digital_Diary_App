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

    // NEW: We expose the latest entry to help the UI calculate the "1-minute" rule
    val latestEntry: Flow<DiaryEntry?> = allEntries.map { list ->
        list.maxByOrNull { it.timestamp }
    }

    // UPDATED: Now accepts a specific timestamp (default is NOW)
    fun saveEntry(title: String, content: String, date: Long = System.currentTimeMillis(), existingEntry: DiaryEntry? = null) {
        viewModelScope.launch {
            // Use the provided date, but keep the ID if we are editing
            val entry = existingEntry?.copy(title = title, content = content)
                ?: DiaryEntry(title = title, content = content, timestamp = date)
            dao.insertEntry(entry)
        }
    }

    // NEW: Helper to find an entry for a specific date (for the Calendar tap)
    fun getEntryForDate(dateInMillis: Long, entries: List<DiaryEntry>): DiaryEntry? {
        return entries.find { isSameDay(it.timestamp, dateInMillis) }
    }

    private fun isSameDay(t1: Long, t2: Long): Boolean {
        val c1 = Calendar.getInstance().apply { timeInMillis = t1 }
        val c2 = Calendar.getInstance().apply { timeInMillis = t2 }
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }

    fun delete(entry: DiaryEntry) { viewModelScope.launch { dao.deleteEntry(entry) } }
}
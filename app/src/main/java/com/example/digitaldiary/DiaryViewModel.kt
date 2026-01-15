package com.example.digitaldiary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = DiaryDatabase.getDatabase(application).diaryDao()
    val allEntries: Flow<List<DiaryEntry>> = dao.getAllEntries()

    // LATEST ENTRY: Helps calculate the 1-minute rule
    val latestEntry: Flow<DiaryEntry?> = allEntries.map { list ->
        list.maxByOrNull { it.timestamp }
    }

    // SAVE: Handles both adding new and updating existing entries
    fun saveEntry(title: String, content: String, date: Long, existingEntry: DiaryEntry? = null) {
        viewModelScope.launch {
            val entry = existingEntry?.copy(title = title, content = content, timestamp = date)
                ?: DiaryEntry(title = title, content = content, timestamp = date)
            dao.insertEntry(entry)
        }
    }

    // DELETE
    fun delete(entry: DiaryEntry) {
        viewModelScope.launch { dao.deleteEntry(entry) }
    }

    // HELPER: Get entry by date
    fun getEntryForDate(dateInMillis: Long, entries: List<DiaryEntry>): DiaryEntry? {
        return entries.find { isSameDay(it.timestamp, dateInMillis) }
    }

    private fun isSameDay(t1: Long, t2: Long): Boolean {
        val c1 = Calendar.getInstance().apply { timeInMillis = t1 }
        val c2 = Calendar.getInstance().apply { timeInMillis = t2 }
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }

    // --- BACKUP FEATURES (The "Digital Moving Van") ---

    fun exportToDocuments(entries: List<DiaryEntry>) {
        viewModelScope.launch {
            try {
                // Saves to: Android/data/com.example.digitaldiary/files/diary_backup.json
                val file = File(getApplication<Application>().getExternalFilesDir(null), "diary_backup.json")

                val json = StringBuilder().apply {
                    append("[\n")
                    entries.forEachIndexed { index, entry ->
                        append("  {")
                        append("\"id\":\"${entry.id}\",")
                        append("\"title\":\"${entry.title}\",")
                        // Escape newlines so JSON doesn't break
                        append("\"content\":\"${entry.content.replace("\n", "\\n")}\",")
                        append("\"timestamp\":${entry.timestamp},")
                        append("\"moodEmoji\":\"${entry.moodEmoji}\"")
                        append("}${if (index < entries.size - 1) "," else ""}\n")
                    }
                    append("]")
                }.toString()

                file.writeText(json)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun importFromDocuments() {
        viewModelScope.launch {
            try {
                val file = File(getApplication<Application>().getExternalFilesDir(null), "diary_backup.json")
                if (!file.exists()) return@launch

                val content = file.readText()
                // Simple parsing logic splitting by object boundaries
                val entryObjects = content.split("},{", "}, {", "},\n{")

                entryObjects.forEach { obj ->
                    val id = obj.substringAfter("\"id\":\"").substringBefore("\"")
                    val title = obj.substringAfter("\"title\":\"").substringBefore("\"")
                    val body = obj.substringAfter("\"content\":\"").substringBefore("\"").replace("\\n", "\n")
                    val timeString = obj.substringAfter("\"timestamp\":").substringBefore(",")
                    val emoji = obj.substringAfter("\"moodEmoji\":\"").substringBefore("\"")

                    // Safety check for valid numbers
                    val time = timeString.trim().toLongOrNull() ?: System.currentTimeMillis()

                    val entry = DiaryEntry(id, title, body, time, moodEmoji = emoji)
                    dao.insertEntry(entry) // REPLACES existing entries with same ID
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}
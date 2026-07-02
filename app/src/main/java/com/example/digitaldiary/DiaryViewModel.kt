package com.example.digitaldiary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = DiaryDatabase.getDatabase(application).diaryDao()
    val allEntries: Flow<List<DiaryEntry>> = dao.getAllEntries()

    val latestEntry: Flow<DiaryEntry?> = allEntries.map { list -> list.maxByOrNull { it.timestamp } }
    val unlockedPlants: Flow<List<UnlockedPlant>> = dao.getAllUnlockedPlants()

    fun saveEntry(title: String, content: String, date: Long, existingEntry: DiaryEntry? = null, dayRating: Int) {
        viewModelScope.launch {
            val entry = existingEntry?.copy(title = title, content = content, timestamp = date, dayRating = dayRating)
                ?: DiaryEntry(title = title, content = content, timestamp = date, dayRating = dayRating)
            dao.insertEntry(entry)
        }
    }

    fun delete(entry: DiaryEntry) { viewModelScope.launch { dao.deleteEntry(entry) } }

    fun getEntryForDate(dateInMillis: Long, entries: List<DiaryEntry>): DiaryEntry? { return entries.find { isSameDay(it.timestamp, dateInMillis) } }

    // Replace your old getStreak function with this Milestone calculator
    fun getStreak(entries: List<DiaryEntry>): Int {
        if (entries.isEmpty()) return 0

        // Group all entries by their calendar date (stripping away hours/minutes/seconds)
        // This gives us a permanent count of total unique days logged.
        val uniqueDaysCount = entries.map { entry ->
            val calendar = Calendar.getInstance().apply { timeInMillis = entry.timestamp }
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.timeInMillis
        }.distinct().size

        return uniqueDaysCount
    }

    private fun isSameDay(t1: Long, t2: Long): Boolean {
        val c1 = Calendar.getInstance().apply { timeInMillis = t1 }
        val c2 = Calendar.getInstance().apply { timeInMillis = t2 }
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }

    fun getCurrentPlantTier(unlocked: List<UnlockedPlant>): Int {
        if (unlocked.isEmpty()) return 1
        val maxUnlocked = unlocked.maxOf { it.plantTier }
        return maxUnlocked.coerceIn(1, 8)
    }

    fun unlockNewPlant(tier: Int) {
        viewModelScope.launch {
            dao.insertUnlockedPlant(
                UnlockedPlant(
                    plantTier = tier,
                    unlockDate = System.currentTimeMillis(),
                    isViewed = false
                )
            )
        }
    }

    fun resetPlantLibrary() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearAllUnlockedPlants()
        }
    }
}
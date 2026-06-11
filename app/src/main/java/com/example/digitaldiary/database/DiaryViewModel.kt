package com.example.digitaldiary.database

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

    fun getStreak(entries: List<DiaryEntry>): Int {
        if (entries.isEmpty()) return 0

        val calendar = Calendar.getInstance()
        val todayStart = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val yesterdayStart = calendar.apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }.timeInMillis

        // Extract and normalize all unique entry dates down to midnight boundaries
        val entryDates = entries.map {
            val c = Calendar.getInstance().apply { timeInMillis = it.timestamp }
            c.set(Calendar.HOUR_OF_DAY, 0)
            c.set(Calendar.MINUTE, 0)
            c.set(Calendar.SECOND, 0)
            c.set(Calendar.MILLISECOND, 0)
            c.timeInMillis
        }.distinct()

        // CRITICAL UPDATE: If there is no entry for today AND no entry for yesterday, the streak is broken
        if (!entryDates.contains(todayStart) && !entryDates.contains(yesterdayStart)) {
            return 0
        }

        // Reset tracking pointer back to today to begin counting backwards consecutive matches
        var streak = 0
        var checkDate = todayStart

        // If today is empty but yesterday has an entry, start counting backwards from yesterday directly
        if (!entryDates.contains(todayStart) && entryDates.contains(yesterdayStart)) {
            checkDate = yesterdayStart
        }

        while (true) {
            if (entryDates.contains(checkDate)) {
                streak++
                val c = Calendar.getInstance().apply { timeInMillis = checkDate }
                c.add(Calendar.DAY_OF_YEAR, -1)
                checkDate = c.timeInMillis
            } else {
                break
            }
        }
        return streak
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
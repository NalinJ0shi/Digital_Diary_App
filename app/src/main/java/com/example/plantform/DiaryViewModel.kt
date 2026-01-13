package com.example.plantform

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = DiaryDatabase.getDatabase(application).diaryDao()

    // This "Flow" automatically updates the UI whenever the database changes!
    val allEntries: Flow<List<DiaryEntry>> = dao.getAllEntries()

    fun addEntry(title: String, content: String) {
        viewModelScope.launch {
            dao.insertEntry(DiaryEntry(title = title, content = content))
        }
    }

    fun delete(entry: DiaryEntry) {
        viewModelScope.launch {
            dao.deleteEntry(entry)
        }
    }
}
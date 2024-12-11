package com.example.nutripal.ui.screen.profile.history

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.data.model.HistoryItem
import com.example.nutripal.data.repository.HistoryRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryScanViewModel( private val historyRepository: HistoryRepository) : ViewModel() {
    private val _historyItems = mutableStateOf<List<HistoryItem>>(emptyList())
    val historyItems: State<List<HistoryItem>> = _historyItems

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    init {
        fetchHistoryItems()
    }

    fun fetchHistoryItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                historyRepository.fetchAndSaveHistoryItems()
                historyRepository.getHistoryItems().collect { items ->
                    _historyItems.value = items
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }
}

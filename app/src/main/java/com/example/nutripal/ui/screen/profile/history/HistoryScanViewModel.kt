package com.example.nutripal.ui.screen.profile.history

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.data.model.HistoryItem
import com.example.nutripal.data.repository.HistoryRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HistoryScanViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
    private val _historyItems = mutableStateOf<List<HistoryItem>>(emptyList())
    val historyItems: State<List<HistoryItem>> = _historyItems

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    init {
        fetchHistoryItems()
    }

    fun fetchHistoryItems() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                historyRepository.fetchAndSaveHistoryItems()
                historyRepository.getHistoryItems()
                    .catch { e ->
                        _error.value = "Gagal memuat riwayat: ${e.localizedMessage}"
                        _isLoading.value = false
                    }
                    .collect { items ->
                        _historyItems.value = items
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _error.value = "Gagal memuat riwayat: ${e.localizedMessage}"
                _isLoading.value = false
            }
        }
    }

    fun retryFetch() {
        fetchHistoryItems()
    }
}
package com.example.nutripal.viewmodel

import android.util.Base64
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.data.remote.response.ArticlesItem
import com.example.nutripal.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _newsDetail = MutableStateFlow<ArticlesItem?>(null)
    val newsDetail = _newsDetail.asStateFlow()

    init {
        savedStateHandle.get<String>("encodedUrl")?.let { encodedUrl ->
            try {
                val decodedUrl = String(Base64.decode(encodedUrl, Base64.URL_SAFE), Charsets.UTF_8)
                loadNewsDetail(decodedUrl)
            } catch (e: Exception) {
                Log.e("DetailNewsViewModel", "Error decoding URL", e)
            }
        }
    }

    private fun loadNewsDetail(url: String) {
        viewModelScope.launch {
            try {
                val article = newsRepository.getNewsById(url)
                _newsDetail.value = article
            } catch (e: Exception) {
                Log.e("DetailNewsViewModel", "Error loading news detail", e)
            }
        }
    }
}

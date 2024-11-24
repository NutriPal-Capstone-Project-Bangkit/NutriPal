package com.example.nutripal.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.data.repository.NewsRepository
import com.example.nutripal.ui.component.home.news.NewsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    var newsList = mutableStateOf<List<NewsItem>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun loadNews() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val result = newsRepository.fetchTopHeadlines("us", "health")
                if (result.isSuccess) {
                    newsList.value = result.getOrNull()?.mapNotNull { article ->
                        article?.let {
                            NewsItem(
                                id = it.url ?: return@let null,
                                imageUrl = it.urlToImage ?: "",
                                title = it.title ?: "No Title",
                                subtitle = it.description ?: "No Description",
                                url = it.url
                            )
                        }
                    } ?: emptyList()
                    errorMessage.value = null
                } else {
                    errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to fetch news"
                }
            } catch (e: Exception) {
                errorMessage.value = "Unexpected error occurred: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}

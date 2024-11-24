package com.example.nutripal.data.repository

import com.example.nutripal.data.remote.response.ArticlesItem
import com.example.nutripal.data.remote.retrofit.NewsApiService
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsApiService: NewsApiService) {
    private var cachedArticles = mutableListOf<ArticlesItem>()

    suspend fun fetchTopHeadlines(country: String = "us", category: String = "health"): Result<List<ArticlesItem>> {
        return try {
            val response = newsApiService.getTopHeadlines(country, category)
            cachedArticles.clear()
            response.articles?.filterNotNull()?.let { articles ->
                cachedArticles.addAll(articles)
            }
            Result.success(cachedArticles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun getNewsById(newsId: String): ArticlesItem? {
        return cachedArticles?.find { it.url == newsId }
    }
}

package com.example.nutripal.viewmodel

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import com.example.nutripal.data.model.PageData
import com.example.nutripal.ui.theme.Primary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OnboardingViewModel : ViewModel() {
    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage

    fun updatePage(page: Int) {
        _currentPage.value = page
    }

    fun getPageData(page: Int): PageData {
        return when (page) {
            0 -> PageData("Asupan harian selalu terpantau", "Kamu jadi tahu apa yang kamu makan dan seberapa banyak yang kamu butuhkan. Biar kesehatan tetap terjaga!")
            1 -> PageData("Cepat tinggal scan!", "Sekali jepret, info nutrisi lengkap langsung di tanganmu. Gak perlu ribet!")
            else -> PageData("Punya pertanyaan? NutriAI aja!", "NutriAI siap bantu kamu dengan rekomendasi sehat dan tips nutrisi. Sambil belajar, tetap fun!")
        }
    }

    fun processTitleText(pageData: PageData, pageIndex: Int): AnnotatedString {
        return buildAnnotatedString {
            val words = pageData.title.split(" ")
            when (pageIndex) {
                0 -> {
                    append(words[0])
                    append(" ")
                    append(words[1])
                    append(" ")
                    append(words[2])
                    append(" ")
                    withStyle(style = SpanStyle(color = Primary)) {
                        append(words[3])
                    }
                    append("")
                    append(words.drop(4).joinToString(""))
                    append("!")
                }
                1 -> {
                    withStyle(style = SpanStyle(color = Primary)) {
                        append(words[0])
                    }
                    append(" ")
                    append(words[1])
                    append(" ")
                    append(words[2])
                    append(" ")
                    append(words.drop(3).joinToString(" "))
                }
                else -> {
                    append(words.take(2).joinToString(" "))
                    append(" ")
                    withStyle(style = SpanStyle(color = Primary)) {
                        append("NutriAI")
                    }
                    append(" ")
                    append(words.drop(3).joinToString(" "))
                }
            }
        }
    }
}

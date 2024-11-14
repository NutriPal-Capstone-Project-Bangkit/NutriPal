package com.example.nutripal.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PersonalDetailsViewModel : ViewModel() {
    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage

    val name = mutableStateOf("")
    val selectedGender = mutableStateOf("")
    val isDropdownExpanded = mutableStateOf(false)
    val isError = mutableStateOf(false)
    val isFormValid = mutableStateOf(false)

    fun updatePage(page: Int) {
        _currentPage.value = page
    }

    fun updateName(newName: String) {
        val isValid = newName.all { it.isLetter() || it.isWhitespace() }
        name.value = newName
        isError.value = !isValid
        validateForm()
    }

    fun updateGender(gender: String) {
        selectedGender.value = gender
        validateForm()
    }

    fun toggleDropdown() {
        isDropdownExpanded.value = !isDropdownExpanded.value
    }

    fun dismissDropdown() {
        isDropdownExpanded.value = false
    }

    private fun validateForm() {
        isFormValid.value = name.value.isNotEmpty() && selectedGender.value.isNotEmpty() && !isError.value
    }
}

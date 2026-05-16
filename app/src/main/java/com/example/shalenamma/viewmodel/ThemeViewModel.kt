package com.example.shalenamma.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    var isDarkMode by mutableStateOf(false)
        private set

    fun toggleTheme() {
        isDarkMode = !isDarkMode
    }
}
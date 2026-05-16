package com.example.shalenamma.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class LanguageViewModel : ViewModel() {

    var isKannada by mutableStateOf(false)
        private set

    fun toggleLanguage() {
        isKannada = !isKannada
    }

    fun text(en: String, kn: String): String {
        return if (isKannada) kn else en
    }
}
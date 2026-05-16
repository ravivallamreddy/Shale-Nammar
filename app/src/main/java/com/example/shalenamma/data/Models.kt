package com.example.shalenamma.data

enum class UserRole {
    PARENT, ADMIN, UNDEFINED
}

enum class Language(val code: String, val label: String) {
    ENGLISH("en", "English"),
    KANNADA("kn", "ಕನ್ನಡ")
}

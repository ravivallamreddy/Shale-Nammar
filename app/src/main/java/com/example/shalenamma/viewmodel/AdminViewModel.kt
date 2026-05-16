package com.example.shalenammapride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun uploadMeal(title: String, description: String, imageUrl: String) {

        val data = hashMapOf(
            "title" to title,
            "description" to description,
            "imageUrl" to imageUrl,
            "timestamp" to System.currentTimeMillis()
        )

        viewModelScope.launch {
            db.collection("meals").add(data)
        }
    }

    fun postAnnouncement(title: String, type: String) {
        val data = hashMapOf(
            "title" to title,
            "type" to type,
            "timestamp" to System.currentTimeMillis()
        )

        viewModelScope.launch {
            db.collection("announcements").add(data)
        }
    }
}
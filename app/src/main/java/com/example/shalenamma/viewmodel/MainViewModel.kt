package com.example.shalenamma.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // 🔹 STATES
    var meal by mutableStateOf<Map<String, Any>?>(null)
        private set

    var students by mutableStateOf<List<Map<String, Any>>>(emptyList())
        private set

    var announcements by mutableStateOf<List<Map<String, Any>>>(emptyList())
        private set

    init {
        loadData()
        listenRealtime()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                // Fetch latest meal
                val mealQuery = db.collection("meals")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()
                meal = mealQuery.documents.firstOrNull()?.data

                // Fetch students
                val studentQuery = db.collection("students").get().await()
                students = studentQuery.documents.mapNotNull { it.data }

                // Fetch announcements
                val announcementQuery = db.collection("announcements")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()
                announcements = announcementQuery.documents.mapNotNull { it.data }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun listenRealtime() {
        db.collection("announcements")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                if (snapshot != null) {
                    announcements = snapshot.documents.mapNotNull { it.data }
                }
            }
    }
}

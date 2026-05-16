package com.example.shalenammapride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class Announcement(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val date: Long = System.currentTimeMillis(),
    val type: String = "General"
)

data class Feedback(
    val id: String = "",
    val category: String = "",
    val message: String = "",
    val isAnonymous: Boolean = false,
    val userName: String = "Anonymous",
    val timestamp: Long = System.currentTimeMillis()
)

class DataViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements

    private val _feedbackList = MutableStateFlow<List<Feedback>>(emptyList())
    val feedbackList: StateFlow<List<Feedback>> = _feedbackList

    init {
        fetchAnnouncements()
        fetchFeedback()
    }

    fun fetchAnnouncements() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("announcements")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .get().await()
                _announcements.value = snapshot.toObjects(Announcement::class.java)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchFeedback() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("feedback")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get().await()
                _feedbackList.value = snapshot.toObjects(Feedback::class.java)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun postAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            db.collection("announcements").add(announcement).await()
            fetchAnnouncements()
        }
    }

    fun submitFeedback(feedback: Feedback) {
        viewModelScope.launch {
            db.collection("feedback").add(feedback).await()
            fetchFeedback()
        }
    }
}

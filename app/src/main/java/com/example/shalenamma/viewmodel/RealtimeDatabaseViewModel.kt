package com.example.shalenamma.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MealUpdate(
    val name: String = "",
    val nutritionalInfo: String = "",
    val timestamp: Long = 0,
    val imageUrl: String? = null
)

data class AnnouncementUpdate(
    val tag: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = ""
)

data class FeedbackUpdate(
    val category: String = "",
    val message: String = "",
    val status: String = "PENDING",
    val timestamp: Long = 0
)

data class StudentStarUpdate(
    val name: String = "",
    val grade: String = "",
    val category: String = "",
    val achievement: String = "",
    val timestamp: Long = 0
)

data class ActivityItem(val type: String, val title: String, val timestamp: Long)

class RealtimeDatabaseViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().reference

    // --- Centralized Data States ---
    var liveMeal by mutableStateOf<MealUpdate?>(null)
        private set
    var liveAnnouncements by mutableStateOf<List<AnnouncementUpdate>>(emptyList())
        private set
    var liveFeedback by mutableStateOf<List<FeedbackUpdate>>(emptyList())
        private set
    var liveStudentStars by mutableStateOf<List<StudentStarUpdate>>(emptyList())
        private set

    // --- Dynamic Aggregated Activity Feed ---
    var recentActivity by mutableStateOf<List<ActivityItem>>(emptyList())
        private set

    // --- Admin Statistics ---
    var activeFeedbackCount by mutableStateOf(0)
        private set

    var isFetching by mutableStateOf(true)
        private set

    var isMealFetching by mutableStateOf(true)
        private set
    var isAnnouncementsFetching by mutableStateOf(true)
        private set
    var isFeedbackFetching by mutableStateOf(true)
        private set
    var isStarsFetching by mutableStateOf(true)
        private set

    init {
        try { 
            FirebaseDatabase.getInstance().setPersistenceEnabled(true) 
        } catch (e: Exception) {
            // Persistence can only be set once
        }
        startSync()

        // 🛡️ Safety Timeout: If Firebase takes > 5s, stop the loading spinners
        // This ensures the app shows offline/dummy data instead of hanging forever
        viewModelScope.launch {
            kotlinx.coroutines.delay(5000)
            isFetching = false
            isMealFetching = false
            isAnnouncementsFetching = false
            isFeedbackFetching = false
            isStarsFetching = false
        }
    }

    private fun startSync() {
        listenForMealUpdates()
        listenForAnnouncements()
        listenForFeedback()
        listenForStudentStars()
    }

    private fun updateActivityFeed() {
        val items = mutableListOf<ActivityItem>()
        
        liveMeal?.let { 
            items.add(ActivityItem("Meal Update", "🍲 Today's lunch: ${it.name}", it.timestamp)) 
        }
        liveAnnouncements.forEach { 
            items.add(ActivityItem("Notice", "📢 ${it.title}", it.date.toTimestamp())) 
        }
        liveStudentStars.forEach { 
            items.add(ActivityItem("Star", "✨ New Star: ${it.name}", it.timestamp)) 
        }
        liveFeedback.forEach { 
            items.add(ActivityItem("Feedback", "💬 New parent feedback", it.timestamp)) 
        }

        recentActivity = items.sortedByDescending { it.timestamp }.take(4)
        activeFeedbackCount = liveFeedback.filter { it.status == "PENDING" }.size
    }

    // --- Helper for date conversion ---
    private fun String.toTimestamp(): Long {
        return try {
            java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.US).parse(this)?.time ?: 0L
        } catch (e: Exception) { 0L }
    }

    // --- Collection Listeners ---
    private fun listenForMealUpdates() {
        database.child("daily_meal").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                liveMeal = snapshot.getValue(MealUpdate::class.java)
                updateActivityFeed()
                isMealFetching = false
                isFetching = false
            }
            override fun onCancelled(error: DatabaseError) {
                isMealFetching = false
                isFetching = false
            }
        })
    }

    private fun listenForAnnouncements() {
        database.child("announcements").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<AnnouncementUpdate>()
                snapshot.children.forEach { child -> child.getValue(AnnouncementUpdate::class.java)?.let { list.add(it) } }
                liveAnnouncements = list.reversed()
                updateActivityFeed()
                isAnnouncementsFetching = false
            }
            override fun onCancelled(error: DatabaseError) {
                isAnnouncementsFetching = false
            }
        })
    }

    private fun listenForFeedback() {
        database.child("feedback").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<FeedbackUpdate>()
                snapshot.children.forEach { child -> child.getValue(FeedbackUpdate::class.java)?.let { list.add(it) } }
                liveFeedback = list.reversed()
                updateActivityFeed()
                isFeedbackFetching = false
            }
            override fun onCancelled(error: DatabaseError) {
                isFeedbackFetching = false
            }
        })
    }

    private fun listenForStudentStars() {
        database.child("student_stars").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<StudentStarUpdate>()
                snapshot.children.forEach { child -> child.getValue(StudentStarUpdate::class.java)?.let { list.add(it) } }
                liveStudentStars = list.reversed()
                updateActivityFeed()
                isStarsFetching = false
            }
            override fun onCancelled(error: DatabaseError) {
                isStarsFetching = false
            }
        })
    }

    // --- Singleton Mutation Methods ---
    fun updateMeal(name: String, nutrition: String) {
        database.child("daily_meal").setValue(MealUpdate(name, nutrition, System.currentTimeMillis()))
    }

    fun postAnnouncement(tag: String, title: String, description: String) {
        val dateFormat = java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.US)
        database.child("announcements").push().setValue(AnnouncementUpdate(tag, title, description, dateFormat.format(java.util.Date())))
    }

    fun postStudentStar(name: String, grade: String, category: String, achievement: String) {
        database.child("student_stars").push().setValue(StudentStarUpdate(name, grade, category, achievement, System.currentTimeMillis()))
    }

    fun submitFeedback(category: String, message: String) {
        database.child("feedback").push().setValue(FeedbackUpdate(category, message, "PENDING", System.currentTimeMillis()))
    }

    private fun triggerNotification(topic: String, title: String, body: String) {
        val trigger = mapOf(
            "topic" to topic,
            "title" to title,
            "body" to body,
            "timestamp" to System.currentTimeMillis()
        )
        database.child("notification_triggers").push().setValue(trigger)
    }
}

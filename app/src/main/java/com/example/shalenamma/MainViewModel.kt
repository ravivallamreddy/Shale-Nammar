package com.example.shalenamma

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class UserRole { PARENT, ADMIN }

class MainViewModel : ViewModel() {
    // Role State
    private val _userRole = mutableStateOf(UserRole.PARENT)
    val userRole: State<UserRole> = _userRole

    // Dark Mode State
    private val _isDarkMode = mutableStateOf(false)
    val isDarkMode: State<Boolean> = _isDarkMode

    // Demo Sync State (Toggles every 5 seconds)
    private val _lastSyncTime = mutableStateOf("Just now")
    val lastSyncTime: State<String> = _lastSyncTime
    
    private val _isSyncing = mutableStateOf(false)
    val isSyncing: State<Boolean> = _isSyncing

    // User Info
    private val _userName = mutableStateOf("User Name")
    val userName: State<String> = _userName

    private val _userEmail = mutableStateOf("user@example.com")
    val userEmail: State<String> = _userEmail

    init {
        startDemoSync()
    }

    fun setUserInfo(name: String, email: String) {
        _userName.value = name
        _userEmail.value = email
    }

    fun updateUserName(name: String) {
        _userName.value = name
    }

    fun setRole(role: UserRole) {
        _userRole.value = role
    }

    fun reset() {
        _userName.value = "User Name"
        _userEmail.value = "user@example.com"
        _userRole.value = UserRole.PARENT
    }

    fun toggleTheme() {
        _isDarkMode.value = !_isDarkMode.value
    }

    private fun startDemoSync() {
        viewModelScope.launch {
            while (true) {
                delay(5000)
                _isSyncing.value = true
                delay(800) // Visual sync simulation
                _isSyncing.value = false
                val time = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
                _lastSyncTime.value = "Synced at $time"
            }
        }
    }
}

package com.example.shalenamma.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class UserProfile(
    val uid: String = "",
    val displayName: String = "",
    val username: String = "",
    val email: String = "",
    val role: String = "Parent" // "Parent" or "Admin"
)

class AuthViewModel : ViewModel() {
    private val auth by lazy { 
        try {
            com.google.firebase.auth.FirebaseAuth.getInstance()
        } catch (e: Exception) {
            null
        }
    }
    private val db by lazy { 
        try {
            com.google.firebase.firestore.FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    private val _isLoggedIn = MutableStateFlow(auth?.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    init {
        if (auth == null) {
            error = "Firebase not initialized. Check google-services.json"
        }
        auth?.currentUser?.let { 
            viewModelScope.launch {
                fetchUserProfile(it.uid)
            }
        }
    }

    private suspend fun fetchUserProfile(uid: String) {
        val database = db ?: return
        try {
            val doc = database.collection("users").document(uid).get().await()
            _userProfile.value = doc.toObject(UserProfile::class.java)
        } catch (e: Exception) {
            error = e.message
        }
    }

    fun login(identifier: String, password: String) {
        val authenticator = auth ?: run {
            error = "Firebase not initialized"
            return
        }
        val database = db ?: run {
            error = "Firestore not initialized"
            return
        }
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                var loginEmail = identifier
                
                // If the identifier doesn't look like an email, try to find the email by username
                if (!identifier.contains("@")) {
                    val userQuery = database.collection("users")
                        .whereEqualTo("username", identifier)
                        .get()
                        .await()
                    
                    val userDoc = userQuery.documents.firstOrNull()
                    if (userDoc != null) {
                        loginEmail = userDoc.getString("email") ?: identifier
                    } else {
                        throw Exception("Username not found")
                    }
                }

                authenticator.signInWithEmailAndPassword(loginEmail, password).await()
                authenticator.currentUser?.let { 
                    fetchUserProfile(it.uid)
                    _isLoggedIn.value = true
                }
            } catch (e: com.google.firebase.FirebaseException) {
                error = "Firebase Error: ${e.localizedMessage}"
            } catch (e: com.google.firebase.auth.FirebaseAuthInvalidUserException) {
                error = "No account found with this email/username."
            } catch (e: com.google.firebase.auth.FirebaseAuthInvalidCredentialsException) {
                error = "Incorrect password."
            } catch (e: Exception) {
                error = e.localizedMessage ?: "Login failed"
            } finally {
                isLoading = false
            }
        }
    }

    fun signup(email: String, password: String, displayName: String, username: String, role: String) {
        val authenticator = auth ?: run {
            error = "Firebase not initialized"
            return
        }
        val database = db ?: run {
            error = "Firestore not initialized"
            return
        }
        
        if (email.isBlank() || password.length < 6 || displayName.isBlank() || username.isBlank()) {
            error = "Please fill all fields. Password must be at least 6 characters."
            return
        }

        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                // 1. Create the Auth Account first
                val result = authenticator.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: throw Exception("Failed to get User ID")

                // 2. Now that we are authenticated, we can check/write to Firestore
                val profile = UserProfile(uid, displayName, username, email, role)
                database.collection("users").document(uid).set(profile).await()
                
                _userProfile.value = profile
                _isLoggedIn.value = true
            } catch (e: com.google.firebase.FirebaseException) {
                error = "Firebase Error: ${e.localizedMessage}"
            } catch (e: com.google.firebase.auth.FirebaseAuthUserCollisionException) {
                error = "This email is already registered."
            } catch (e: Exception) {
                error = e.localizedMessage ?: "Signup failed"
            } finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        auth?.signOut()
        _isLoggedIn.value = false
        _userProfile.value = null
    }
}

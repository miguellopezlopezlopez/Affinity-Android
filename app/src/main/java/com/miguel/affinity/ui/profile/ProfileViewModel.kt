package com.miguel.affinity.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miguel.affinity.data.model.UserProfile
import com.miguel.affinity.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadProfile(username: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                android.util.Log.d("ProfileViewModel", "Loading profile for username: $username")
                
                val result = userRepository.getUserProfile(username)
                _profile.value = result
                android.util.Log.d("ProfileViewModel", "Profile loaded successfully: $result")
            } catch (e: Exception) {
                android.util.Log.e("ProfileViewModel", "Error loading profile", e)
                _error.value = "Error al cargar el perfil: ${e.message}"
                _profile.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfile(profile: UserProfile, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                android.util.Log.d("ProfileViewModel", "Updating profile for user: ${profile.user}")
                
                val result = userRepository.updateUserProfile(profile, password)
                _message.value = result
                loadProfile(profile.user)
                android.util.Log.d("ProfileViewModel", "Profile updated successfully")
            } catch (e: Exception) {
                android.util.Log.e("ProfileViewModel", "Error updating profile", e)
                _error.value = "Error al actualizar el perfil: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAccount(userId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                android.util.Log.d("ProfileViewModel", "Deleting account for userId: $userId")
                
                val result = userRepository.deleteUser(userId)
                _message.value = result
                android.util.Log.d("ProfileViewModel", "Account deleted successfully")
            } catch (e: Exception) {
                android.util.Log.e("ProfileViewModel", "Error deleting account", e)
                _error.value = "Error al eliminar la cuenta: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearMessage() {
        _message.value = null
    }
}
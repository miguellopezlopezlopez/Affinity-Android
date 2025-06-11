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

    fun loadProfile(username: String) {
        viewModelScope.launch {
            val result = userRepository.getUserProfile(username)
            _profile.value = result
        }
    }

    fun updateProfile(profile: UserProfile, password: String) {
        viewModelScope.launch {
            val result = userRepository.updateUserProfile(profile, password)
            _message.value = result
            loadProfile(profile.user)
        }
    }

    fun deleteAccount(userId: Int) {
        viewModelScope.launch {
            val result = userRepository.deleteUser(userId)
            _message.value = result
        }
    }
}
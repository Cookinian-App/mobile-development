package com.bangkitcapstone.cookinian.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: Repository) : ViewModel() {

    fun getSession() = repository.getSession().asLiveData()

    fun saveThemeMode(themeMode: String) {
        viewModelScope.launch {
            repository.saveThemeMode(themeMode)
        }
    }

    fun getThemeMode() = repository.getThemeMode().asLiveData()

    fun logout() {
        viewModelScope.launch {
            repository.clearSession()
        }
    }

}
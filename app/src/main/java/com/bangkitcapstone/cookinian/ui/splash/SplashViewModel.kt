package com.bangkitcapstone.cookinian.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkitcapstone.cookinian.data.Repository

class SplashViewModel(private val repository: Repository) : ViewModel() {
    fun getSession() = repository.getSession().asLiveData()

    fun getThemeMode() = repository.getThemeMode().asLiveData()
}
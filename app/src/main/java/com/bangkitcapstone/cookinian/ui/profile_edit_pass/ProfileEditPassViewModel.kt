package com.bangkitcapstone.cookinian.ui.profile_edit_pass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.api.response.RegisterResponse
import com.bangkitcapstone.cookinian.helper.Event
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileEditPassViewModel(private val repository: Repository): ViewModel() {
    private val _result = MutableLiveData<Event<Result<RegisterResponse>>>()
    val result: LiveData<Event<Result<RegisterResponse>>> = _result

    fun getSession() = repository.getSession().asLiveData()

    fun changePassword(email: String, currentPassword: String, newPassword: String, confirmNewPassword: String) {
        viewModelScope.launch {
            try {
                _result.value = Event(Result.Loading)
                val response = repository.changePassword(email, currentPassword, newPassword, confirmNewPassword)
                if (!response.error) {
                    _result.value = Event(Result.Success(response))
                } else {
                    _result.value = Event(Result.Error(response.message))
                }
            } catch (e: HttpException) {
                val error = e.response()?.errorBody()?.string()
                val message = Gson().fromJson(error, RegisterResponse::class.java)
                _result.value = Event(Result.Error(message.message))
            }
        }
    }
}
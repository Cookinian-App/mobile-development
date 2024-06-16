package com.bangkitcapstone.cookinian.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.api.response.LoginResponse
import com.bangkitcapstone.cookinian.data.preference.UserModel
import com.bangkitcapstone.cookinian.helper.Event
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _result = MutableLiveData<Event<Result<LoginResponse>>>()
    val result: LiveData<Event<Result<LoginResponse>>> = _result

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _result.value = Event(Result.Loading)
                val response = repository.login(email, password)
                if (!response.error) {
                    repository.saveSession(
                        UserModel(
                            userId = response.loginResult.userId,
                            name = response.loginResult.name,
                            avatarUrl = response.loginResult.avatarUrl,
                            email = email,
                            token = response.loginResult.token
                        )
                    )
                    _result.value = Event(Result.Success(response))
                } else {
                    _result.value = Event(Result.Error(response.message))
                }
            } catch (e: HttpException) {
                val error = e.response()?.errorBody()?.string()
                val message = Gson().fromJson(error, LoginResponse::class.java)
                _result.value = Event(Result.Error(message.message))
            } catch (e: Exception) {
                _result.value = Event(Result.Error("Tidak dapat terhubung ke server!"))
            } catch (e: UnknownHostException) {
                _result.value = Event(Result.Error("Tidak ada koneks internet, silahkan coba lagi."))
            }
        }
    }
}
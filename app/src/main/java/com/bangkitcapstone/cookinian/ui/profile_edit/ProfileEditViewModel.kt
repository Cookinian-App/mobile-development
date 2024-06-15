package com.bangkitcapstone.cookinian.ui.profile_edit

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
import java.net.UnknownHostException

class ProfileEditViewModel(private val repository: Repository): ViewModel() {
    private val _result = MutableLiveData<Event<Result<RegisterResponse>>>()
    val result: LiveData<Event<Result<RegisterResponse>>> = _result

    fun getSession() = repository.getSession().asLiveData()

    fun editProfile(email: String, newName: String) {
        viewModelScope.launch {
            try {
                _result.value = Event(Result.Loading)
                val response = repository.editProfile(email, newName)
                if (!response.error) {
                    repository.editSession(newName)
                    _result.value = Event(Result.Success(response))
                } else {
                    _result.value = Event(Result.Error(response.message))
                }
            } catch (e: HttpException) {
                val error = e.response()?.errorBody()?.string()
                val message = Gson().fromJson(error, RegisterResponse::class.java)
                _result.value = Event(Result.Error(message.message))
            } catch (e: Exception) {
                _result.value = Event(Result.Error("Tidak dapat menghubungkan ke server!"))
            } catch (e: UnknownHostException) {
                _result.value = Event(Result.Error("Tidak ada koneksi internet, silahkan coba lagi."))
            }
        }
    }
}
package com.bangkitcapstone.cookinian.ui.saved_recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.api.response.RegisterResponse
import com.bangkitcapstone.cookinian.helper.Event
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class SavedRecipeViewModel(private val repository: Repository): ViewModel() {
    private val _result = MutableLiveData<Event<Result<RegisterResponse>>>()
    val result: LiveData<Event<Result<RegisterResponse>>> = _result

    fun getSession() = repository.getSession().asLiveData()
    fun getSavedRecipe() = repository.getBookmarkedRecipe()

    fun deleteAllSavedRecipes() {
        viewModelScope.launch {
            try {
                repository.getSession().collect {
                    val response = repository.deleteAllSavedRecipeFromApi(it.userId)
                    _result.value = Event(Result.Success(response))
                    repository.deleteAllSavedRecipeFromLocal()
                }
            } catch (e: Exception) {
                _result.value = Event(Result.Error("Tidak dapat menghubungkan ke server!"))
            } catch (e: UnknownHostException) {
                _result.value = Event(Result.Error("Tidak ada koneksi internet, silahkan coba lagi."))
            } catch (e: HttpException) {
                _result.value = Event(Result.Error(e.message.toString()))
            }
        }
    }
}
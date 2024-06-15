package com.bangkitcapstone.cookinian.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.data.api.response.RegisterResponse
import com.bangkitcapstone.cookinian.helper.handleHttpException
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class ArticleViewModel(private val repository: Repository) : ViewModel() {
    private val _categories = MutableLiveData<Result<List<CategoryItem>>>()
    val categories: LiveData<Result<List<CategoryItem>>> = _categories

    init {
        getArticleCategories()
    }

    private fun getArticleCategories() {
        viewModelScope.launch {
            try {
                _categories.value = Result.Loading
                val result = repository.getArticleCategory().results
                _categories.value = Result.Success(result)
            } catch (e: HttpException) {
                handleHttpException(e, _categories)
            } catch (e: UnknownHostException) {
                _categories.value = Result.Error("Kesalahan jaringan: Tidak dapat menghubungkan ke server")
            } catch (e: Exception) {
                _categories.value = e.message?.let { Result.Error(it) }
            }
        }
    }
}

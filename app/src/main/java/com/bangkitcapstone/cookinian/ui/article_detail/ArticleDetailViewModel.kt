package com.bangkitcapstone.cookinian.ui.article_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.api.response.ArticleDetailResults
import com.bangkitcapstone.cookinian.helper.handleHttpException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class ArticleDetailViewModel(private val repository: Repository) : ViewModel() {
    private val _articleDetail = MutableLiveData<Result<ArticleDetailResults>>()
    val articleDetail: LiveData<Result<ArticleDetailResults>> = _articleDetail

    fun getArticleDetail(tagKey: String) {
        val tag = tagKey.split("/")[0]
        val key = tagKey.split("/")[1]
        viewModelScope.launch {
            try {
                _articleDetail.value = Result.Loading
                val result = repository.getArticleDetail(tag, key).results
                _articleDetail.value = Result.Success(result)
            } catch (e: HttpException) {
                handleHttpException(e, _articleDetail)
            } catch (e: UnknownHostException) {
                _articleDetail.value = Result.Error("Tidak ada koneksi internet, silahkan coba lagi.")
            } catch (e: Exception) {
                _articleDetail.value = e.message?.let { Result.Error(it) }
            }
        }
    }
}
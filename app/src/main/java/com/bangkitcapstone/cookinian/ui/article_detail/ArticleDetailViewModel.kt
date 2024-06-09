package com.bangkitcapstone.cookinian.ui.article_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkitcapstone.cookinian.data.Repository
import com.bangkitcapstone.cookinian.data.api.response.ArticleDetailResults
import kotlinx.coroutines.launch

class ArticleDetailViewModel(private val repository: Repository) : ViewModel() {
    private val _articleDetail = MutableLiveData<ArticleDetailResults>()
    val articleDetail: LiveData<ArticleDetailResults> = _articleDetail

    fun getArticleDetail(tagKey: String) {
        val tag = tagKey.split("/")[0]
        val key = tagKey.split("/")[1]
        viewModelScope.launch {
            val result = repository.getArticleDetail(tag, key).results
            _articleDetail.value = result
        }
    }
}
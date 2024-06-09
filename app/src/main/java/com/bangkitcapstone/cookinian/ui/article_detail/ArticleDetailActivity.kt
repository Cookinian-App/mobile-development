package com.bangkitcapstone.cookinian.ui.article_detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkitcapstone.cookinian.data.api.response.ArticleDetailResults
import com.bangkitcapstone.cookinian.databinding.ActivityArticleDetailBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.ui.recipe_detail.RecipeDetailViewModel
import com.bumptech.glide.Glide

class ArticleDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleDetailBinding

    private val viewModel by viewModels<ArticleDetailViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tagKey = intent.getStringExtra("tagKey")!!

        viewModel.getArticleDetail(tagKey)

        viewModel.articleDetail.observe(this) { article ->
            setupArticleDetailData(article)
        }
    }

    private fun setupArticleDetailData(article: ArticleDetailResults) {
        with(binding) {
            Glide.with(this@ArticleDetailActivity)
                .load(article.thumb)
                .into(ivDetailArticleImage)
            tvDetailArticleTitle.text = article.title
            tvDetailArticleAuthor.text = article.author
            tvDetailArticleDate.text = article.datePublished
            tvDetailRecipeDescription.text = article.description
        }
    }
}
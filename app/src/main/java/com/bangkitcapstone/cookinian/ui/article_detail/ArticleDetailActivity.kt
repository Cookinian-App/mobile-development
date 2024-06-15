package com.bangkitcapstone.cookinian.ui.article_detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.api.response.ArticleDetailResults
import com.bangkitcapstone.cookinian.databinding.ActivityArticleDetailBinding
import com.bangkitcapstone.cookinian.helper.Event
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.helper.showAlert
import com.bumptech.glide.Glide

class ArticleDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleDetailBinding

    private val viewModel by viewModels<ArticleDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tagKey = intent.getStringExtra("tagKey")!!

        viewModel.getArticleDetail(tagKey)

        viewModel.articleDetail.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.pbArticleDetail.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.pbArticleDetail.visibility = View.GONE
                    setupArticleDetailData(result.data)
                }
                is Result.Error -> {
                    Event(result.error).getContentIfNotHandled()?.let {
                        binding.pbArticleDetail.visibility = View.GONE
                        showAlert(this, "Terjadi kesalahan", it)
                    }
                }
            }
        }

        setupToolbar()
    }

    private fun setupArticleDetailData(article: ArticleDetailResults) {
        with(binding) {
            Glide.with(this@ArticleDetailActivity)
                .load(article.thumb)
                .into(ivDetailArticleImage)
            tvDetailArticleTitle.text = article.title
            tvDetailArticleAuthor.text = article.author
            tvDetailArticleDate.text = article.datePublished
            tvDetailArticleDescription.text = article.description
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_recipe_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
package com.bangkitcapstone.cookinian.ui.article_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkitcapstone.cookinian.databinding.ActivityArticleDetailBinding

class ArticleDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
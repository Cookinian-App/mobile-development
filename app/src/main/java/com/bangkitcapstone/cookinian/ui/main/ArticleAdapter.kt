package com.bangkitcapstone.cookinian.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.data.api.response.ArticleItem
import com.bangkitcapstone.cookinian.databinding.ItemArticleBinding
import com.bumptech.glide.Glide

class ArticleAdapter(private val articleList: List<ArticleItem>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticleBinding.inflate(inflater, parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articleList[position]
        Glide.with(holder.itemView.context)
            .load(article.thumb)
            .into(holder.binding.ivItemArticleImage)
        holder.binding.tvItemRecipeName.text = article.title
    }

    override fun getItemCount(): Int = articleList.size
}

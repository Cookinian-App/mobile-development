package com.bangkitcapstone.cookinian.ui.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.data.local.entity.ArticleItem
import com.bangkitcapstone.cookinian.databinding.ItemArticleBinding
import com.bumptech.glide.Glide

class ArticleListAdapter : PagingDataAdapter<ArticleItem, ArticleListAdapter.ArticleListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticleBinding.inflate(inflater, parent, false)
        return ArticleListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleListViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article)
        }
    }

    class ArticleListViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleItem) {
            binding.tvItemArticleName.text = article.title
            Glide.with(binding.root.context).load(article.thumb).into(binding.ivItemArticleImage)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticleItem>() {
            override fun areItemsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

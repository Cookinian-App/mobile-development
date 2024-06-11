package com.bangkitcapstone.cookinian.ui.article

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.databinding.ItemArticleCategoryBinding
import com.bangkitcapstone.cookinian.helper.capitalizeWords
import com.bangkitcapstone.cookinian.ui.category_search.CategorySearchActivity

class ArticleCategoryAdapter(private val articleList: List<CategoryItem>) :
    RecyclerView.Adapter<ArticleCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemArticleCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryItem) {
            binding.tvItemArticleCategoryName.text = capitalizeWords(category.category)
            binding.root.setOnClickListener {
                val intent = Intent(it.context, CategorySearchActivity::class.java).apply {
                    putExtra("category", category.key)
                }
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticleCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articleList[position])
    }

    override fun getItemCount(): Int = articleList.size
}
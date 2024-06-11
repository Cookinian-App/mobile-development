package com.bangkitcapstone.cookinian.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.databinding.ItemRecipeCategoryBinding
import com.bangkitcapstone.cookinian.ui.category_search.CategorySearchActivity
import com.bumptech.glide.Glide

class RecipeCategoryAdapter(private val recipeCategory: List<CategoryItem>) :
    RecyclerView.Adapter<RecipeCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRecipeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryItem) {
            binding.tvItemRecipeCategoryName.text = category.category
            Glide.with(binding.root.context)
                .load(category.thumb)
                .into(binding.ivItemRecipeCategoryImage)

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
        val binding = ItemRecipeCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeCategory[position])
    }

    override fun getItemCount(): Int = recipeCategory.size
}

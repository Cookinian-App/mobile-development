package com.bangkitcapstone.cookinian.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.databinding.ItemCategoryBinding
import com.bangkitcapstone.cookinian.databinding.ItemRecipeBinding
import com.bumptech.glide.Glide

class CategoryAdapter(private val recipeList: List<CategoryItem>) :
    RecyclerView.Adapter<CategoryAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val category = recipeList[position]
        holder.binding.tvItemCategoryName.text = category.category
    }

    override fun getItemCount(): Int = recipeList.size
}

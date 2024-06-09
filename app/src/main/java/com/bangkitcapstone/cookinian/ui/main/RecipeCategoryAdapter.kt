package com.bangkitcapstone.cookinian.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.databinding.ItemRecipeCategoryBinding
import com.bangkitcapstone.cookinian.helper.capitalizeWords
import com.bangkitcapstone.cookinian.ui.search_category.SearchCategoryActivity

class RecipeCategoryAdapter(private val recipeList: List<CategoryItem>) :
    RecyclerView.Adapter<RecipeCategoryAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(val binding: ItemRecipeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeCategoryBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val category = recipeList[position]
        holder.binding.tvItemRecipeCategoryName.text = capitalizeWords(category.category)

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, SearchCategoryActivity::class.java).apply {
                putExtra("category", category.key)
            }
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = recipeList.size
}

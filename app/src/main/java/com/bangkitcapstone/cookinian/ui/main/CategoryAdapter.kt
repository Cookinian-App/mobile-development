package com.bangkitcapstone.cookinian.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.databinding.ItemCategoryBinding
import com.bangkitcapstone.cookinian.helper.capitalizeWords
import com.bangkitcapstone.cookinian.ui.recipe.RecipeFragment

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
        holder.binding.tvItemCategoryName.text = capitalizeWords(category.category)

        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToNavRecipe()
            action.category = category.key
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = recipeList.size
}

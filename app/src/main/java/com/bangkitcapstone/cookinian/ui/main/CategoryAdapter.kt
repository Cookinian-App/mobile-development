package com.bangkitcapstone.cookinian.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.databinding.ItemCategoryBinding
import com.bangkitcapstone.cookinian.helper.capitalizeWords
import com.bangkitcapstone.cookinian.ui.recipe.RecipeFragment
import com.bangkitcapstone.cookinian.ui.recipe_detail.RecipeDetailActivity

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

        // TODO: Fix Adapter navigation to FragmentRecipe
//        holder.itemView.setOnClickListener {
//            val bundle = Bundle().apply {
//                putString("category", category.key)
//            }
//            val fragment = RecipeFragment().apply {
//                arguments = bundle
//            }
//            fragmentManager.beginTransaction()
//                .replace(containerId, fragment)
//                .addToBackStack(null)
//                .commit()
//        }
    }

    override fun getItemCount(): Int = recipeList.size
}

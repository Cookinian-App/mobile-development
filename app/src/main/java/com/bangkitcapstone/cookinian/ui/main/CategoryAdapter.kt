package com.bangkitcapstone.cookinian.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.databinding.ItemCategoryBinding
import com.bangkitcapstone.cookinian.helper.capitalizeWords

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

            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(R.id.nav_home, false)
                .build()

            it.findNavController().navigate(action, navOptions)
        }
    }

    override fun getItemCount(): Int = recipeList.size
}

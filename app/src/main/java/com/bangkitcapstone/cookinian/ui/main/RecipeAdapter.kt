package com.bangkitcapstone.cookinian.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem
import com.bangkitcapstone.cookinian.databinding.ItemRecipeBinding
import com.bumptech.glide.Glide

class RecipeAdapter(private val recipeList: List<RecipeItem>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]
        Glide.with(holder.itemView.context)
            .load(recipe.thumb)
            .into(holder.binding.ivItemRecipeImage)
        holder.binding.tvItemRecipeName.text = recipe.title
        holder.binding.tvItemRecipeTime.text = recipe.times
    }

    override fun getItemCount(): Int = recipeList.size
}

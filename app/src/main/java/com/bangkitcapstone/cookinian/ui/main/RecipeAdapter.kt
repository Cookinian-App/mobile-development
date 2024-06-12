package com.bangkitcapstone.cookinian.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem
import com.bangkitcapstone.cookinian.databinding.ItemRecipeBinding
import com.bangkitcapstone.cookinian.ui.recipe_detail.RecipeDetailActivity
import com.bumptech.glide.Glide

class RecipeAdapter(private val recipeList: List<RecipeItem>) :
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipeItem) {
            Glide.with(binding.root.context)
                .load(recipe.thumb)
                .into(binding.ivItemRecipeImage)
            binding.tvItemRecipeName.text = recipe.title
            binding.tvItemRecipeTime.text = recipe.times
                .replace("jam", " Jam")
                .replace("mnt", " Mnt")
                .replace("j", " J")
            binding.root.setOnClickListener {
                val intent = Intent(it.context, RecipeDetailActivity::class.java).apply {
                    putExtra("key", recipe.key)
                    putExtra("thumb", recipe.thumb)
                    putExtra("serving", recipe.serving)
                    putExtra("calories", recipe.calories)
                }
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeList[position])
    }

    override fun getItemCount(): Int = recipeList.size
}

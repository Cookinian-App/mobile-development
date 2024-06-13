package com.bangkitcapstone.cookinian.ui.bookmark

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.data.local.entity.RecipeItem
import com.bangkitcapstone.cookinian.databinding.ItemRecipeListBinding
import com.bangkitcapstone.cookinian.ui.recipe_detail.RecipeDetailActivity
import com.bumptech.glide.Glide

class SavedRecipeAdapter(private val recipeList: List<RecipeItem>) :
    RecyclerView.Adapter<SavedRecipeAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRecipeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipeItem) {
            Glide.with(binding.root.context)
                .load(recipe.thumb)
                .into(binding.ivItemRecipeListImage)
            binding.tvItemRecipeListName.text = recipe.title
            binding.tvItemRecipeListTime.text = recipe.times
                .replace("jam", " Jam")
                .replace("mnt", " Mnt")
                .replace("j", " J")
            binding.root.setOnClickListener {
                val intent = Intent(it.context, RecipeDetailActivity::class.java).apply {
                    putExtra("key", recipe.key)
                    putExtra("thumb", recipe.thumb)
                    putExtra("serving", "")
                    putExtra("calories", "")
                }
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeList[position])
    }

    override fun getItemCount(): Int = recipeList.size
}
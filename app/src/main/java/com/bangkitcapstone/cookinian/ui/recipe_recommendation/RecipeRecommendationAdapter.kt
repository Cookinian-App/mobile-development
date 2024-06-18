package com.bangkitcapstone.cookinian.ui.recipe_recommendation

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.data.local.entity.RecipesRecommendationItem
import com.bangkitcapstone.cookinian.databinding.ItemRecipeListBinding
import com.bangkitcapstone.cookinian.helper.formatRecipeTimes
import com.bangkitcapstone.cookinian.ui.recipe_detail.RecipeDetailActivity
import com.bumptech.glide.Glide

class RecipeRecommendationAdapter : PagingDataAdapter<RecipesRecommendationItem, RecipeRecommendationAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = getItem(position)
        if (recipe != null) {
            holder.bind(recipe)
        }
    }

    inner class ViewHolder(private val binding: ItemRecipeListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipesRecommendationItem) {
            binding.tvItemRecipeListName.text = recipe.title
            binding.tvItemRecipeListTime.text = formatRecipeTimes(recipe.times)
            binding.tvItemRecipeListLevel.text = recipe.difficulty
            Glide.with(binding.root.context).load(recipe.thumb).into(binding.ivItemRecipeListImage)

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, RecipeDetailActivity::class.java).apply {
                    putExtra("key", recipe.key)
                    putExtra("thumb", recipe.thumb)
                    putExtra("serving", "")
                    putExtra("calories", "")
                }
                context.startActivity(intent)
            }
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecipesRecommendationItem>() {
            override fun areItemsTheSame(oldItem: RecipesRecommendationItem, newItem: RecipesRecommendationItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: RecipesRecommendationItem, newItem: RecipesRecommendationItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
package com.bangkitcapstone.cookinian.ui.recipe_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.databinding.ItemRecipeIngredientBinding

class RecipeIngredientAdapter(private val ingredientsList: List<String>) :
    RecyclerView.Adapter<RecipeIngredientAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRecipeIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: String) {
            binding.tvItemIngredient.text = "• $ingredient"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeIngredientBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredientsList[position])
    }

    override fun getItemCount(): Int = ingredientsList.size
}
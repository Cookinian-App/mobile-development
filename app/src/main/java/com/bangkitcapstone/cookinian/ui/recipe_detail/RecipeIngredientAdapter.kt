package com.bangkitcapstone.cookinian.ui.recipe_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.databinding.ItemRecipeIngredientBinding

class RecipeIngredientAdapter(private val ingredientsList: List<String>) : RecyclerView.Adapter<RecipeIngredientAdapter.IngredientsViewHolder>(){
    inner class IngredientsViewHolder(val binding: ItemRecipeIngredientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeIngredientBinding.inflate(inflater, parent, false)
        return IngredientsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        val ingredient = ingredientsList[position]
        holder.binding.tvItemIngredient.text = "• $ingredient"
    }

    override fun getItemCount(): Int = ingredientsList.size
}
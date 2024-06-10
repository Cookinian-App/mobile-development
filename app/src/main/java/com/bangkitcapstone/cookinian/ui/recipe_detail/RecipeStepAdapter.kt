package com.bangkitcapstone.cookinian.ui.recipe_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.databinding.ItemRecipeStepBinding

class RecipeStepAdapter(private val stepsList: List<String>) : RecyclerView.Adapter<RecipeStepAdapter.StepsViewHolder>(){
    inner class StepsViewHolder(val binding: ItemRecipeStepBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeStepBinding.inflate(inflater, parent, false)
        return StepsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StepsViewHolder, position: Int) {
        val step = stepsList[position]
        holder.binding.tvItemStep.text = step
    }

    override fun getItemCount(): Int = stepsList.size
}
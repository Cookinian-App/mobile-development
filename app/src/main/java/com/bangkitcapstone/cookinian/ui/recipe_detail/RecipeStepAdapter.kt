package com.bangkitcapstone.cookinian.ui.recipe_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.databinding.ItemRecipeStepBinding

class RecipeStepAdapter(private val stepsList: List<String>) :
    RecyclerView.Adapter<RecipeStepAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRecipeStepBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(step: String) {
            binding.tvItemStep.text = step
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeStepBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stepsList[position])
    }

    override fun getItemCount(): Int = stepsList.size
}
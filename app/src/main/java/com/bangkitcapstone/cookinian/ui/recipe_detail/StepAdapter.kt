package com.bangkitcapstone.cookinian.ui.recipe_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.databinding.ItemStepBinding

class StepAdapter(private val stepsList: List<String>) : RecyclerView.Adapter<StepAdapter.StepsViewHolder>(){
    inner class StepsViewHolder(val binding: ItemStepBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStepBinding.inflate(inflater, parent, false)
        return StepsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StepsViewHolder, position: Int) {
        val step = stepsList[position]
        holder.binding.tvStep.text = step
    }

    override fun getItemCount(): Int = stepsList.size
}
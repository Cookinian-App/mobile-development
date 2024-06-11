package com.bangkitcapstone.cookinian.ui.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.databinding.ItemArticleCategoryBinding
import com.bangkitcapstone.cookinian.helper.capitalizeWords

class ArticleCategoryAdapter(
    private val recipeList: List<CategoryItem>,
    private val selectedItem: Int,
    private val onItemClick: (String, Int) -> Unit
) : RecyclerView.Adapter<ArticleCategoryAdapter.RecipeViewHolder>() {

    private var currentSelectedItem = selectedItem

    inner class RecipeViewHolder(val binding: ItemArticleCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticleCategoryBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val category = recipeList[position]
        holder.binding.tvItemArticleCategoryName.text = capitalizeWords(category.category)
        val textColorStateList = ContextCompat.getColorStateList(holder.itemView.context, R.drawable.selector_text)
        holder.binding.tvItemArticleCategoryName.setTextColor(textColorStateList)
        holder.itemView.isSelected = position == currentSelectedItem

        holder.itemView.setOnClickListener {
            val previousSelectedItem = currentSelectedItem
            currentSelectedItem = holder.adapterPosition
            notifyItemChanged(previousSelectedItem)
            notifyItemChanged(currentSelectedItem)
            onItemClick(category.key, currentSelectedItem)
        }
    }

    override fun getItemCount(): Int = recipeList.size
}
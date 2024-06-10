package com.bangkitcapstone.cookinian.ui.article

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkitcapstone.cookinian.data.api.response.CategoryItem
import com.bangkitcapstone.cookinian.databinding.ItemArticleCategoryBinding
import com.bangkitcapstone.cookinian.helper.capitalizeWords
import com.bangkitcapstone.cookinian.ui.category_search.CategorySearchActivity

class ArticleCategoryAdapter(private val recipeList: List<CategoryItem>) :
    RecyclerView.Adapter<ArticleCategoryAdapter.RecipeViewHolder>() {

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

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, CategorySearchActivity::class.java).apply {
                putExtra("category", category.key)
            }
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = recipeList.size
}

package com.bangkitcapstone.cookinian.ui.recipe_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.data.api.response.RecipeDetailResults
import com.bangkitcapstone.cookinian.databinding.FragmentDetailRecipeBinding
import com.bangkitcapstone.cookinian.databinding.ItemRecipeBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bumptech.glide.Glide

class RecipeDetailFragment: Fragment() {
    private var _binding: FragmentDetailRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RecipeDetailViewModel>() {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            val dataKey = RecipeDetailFragmentArgs.fromBundle(arguments as Bundle).key
            val dataThumb = RecipeDetailFragmentArgs.fromBundle(arguments as Bundle).thumb

            viewModel.getRecipeDetail(dataKey)

            viewModel.recipeDetail.observe(viewLifecycleOwner) { recipe ->
                setRecipeDetailData(recipe, dataThumb)
            }
    }

    private fun setRecipeDetailData(recipe: RecipeDetailResults, dataThumb: String) {
        binding.apply {
            Glide.with(this@RecipeDetailFragment)
                .load(dataThumb)
                .into(binding.ivDetailRecipeImage)
            tvDetailRecipeTitle.text = recipe.title
            tvDetailRecipeUser.text = recipe.author.user
            tvDetailRecipeDate.text = recipe.author.datePublished
            tvDetailRecipeTimes.text = recipe.times
            tvDetailRecipeDifficulty.text = recipe.difficulty
            tvDetailRecipeDescription.text = recipe.desc

            rvDetailRecipeIngredient.layoutManager = LinearLayoutManager(context)
            rvDetailRecipeIngredient.adapter = IngredientsAdapter(recipe.ingredient)

            rvDetailRecipeStep.layoutManager = LinearLayoutManager(context)
            rvDetailRecipeStep.adapter = StepsAdapter(recipe.step)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
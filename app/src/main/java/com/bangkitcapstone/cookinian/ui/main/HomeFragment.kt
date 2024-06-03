package com.bangkitcapstone.cookinian.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.databinding.FragmentHomeBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategoryRecyclerView()
        setupRecipeRecyclerView()
        setupArticleRecyclerView()
    }

    private fun setupRecipeRecyclerView() {
        mainViewModel.recipes.observe(viewLifecycleOwner) { recipe ->
            recipeAdapter = RecipeAdapter(recipe)
            binding.rvRecipe.adapter = recipeAdapter
        }

        binding.rvRecipe.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecipe.isNestedScrollingEnabled = false
    }

    private fun setupCategoryRecyclerView() {
        mainViewModel.categories.observe(viewLifecycleOwner) { category ->
            categoryAdapter = CategoryAdapter(category)
            binding.rvCategory.adapter = categoryAdapter
        }

        binding.rvCategory.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.isNestedScrollingEnabled = false
    }

    private fun setupArticleRecyclerView() {
        mainViewModel.articles.observe(viewLifecycleOwner) { article ->
            articleAdapter = ArticleAdapter(article)
            binding.rvArticle.adapter = articleAdapter

        }

        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvArticle.isNestedScrollingEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
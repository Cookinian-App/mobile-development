package com.bangkitcapstone.cookinian.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.databinding.FragmentHomeBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.ui.search_recipe.SearchRecipeActivity

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

        setName()
        setupCategoryRecyclerView()
        setupRecipeRecyclerView()
        setupArticleRecyclerView()

        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    val intent = Intent(requireContext(), SearchRecipeActivity::class.java)
                    intent.putExtra("searchQuery", query)
                    startActivity(intent)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        binding.tvHomeSeeAllRecipe.setOnClickListener { seeAllRecipe() }
    }

    private fun seeAllRecipe() {
        startActivity(Intent(requireContext(), SearchRecipeActivity::class.java))
    }

    private fun setupRecipeRecyclerView() {
        mainViewModel.recipes.observe(viewLifecycleOwner) { recipe ->
            recipeAdapter = RecipeAdapter(recipe)
            binding.rvHomeRecipe.adapter = recipeAdapter
        }

        binding.rvHomeRecipe.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupCategoryRecyclerView() {
        mainViewModel.categories.observe(viewLifecycleOwner) { category ->
            categoryAdapter = CategoryAdapter(category)
            binding.rvHomeCategory.adapter = categoryAdapter
        }

        binding.rvHomeCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupArticleRecyclerView() {
        mainViewModel.articles.observe(viewLifecycleOwner) { article ->
            articleAdapter = ArticleAdapter(article)
            binding.rvHomeArticle.adapter = articleAdapter

        }

        binding.rvHomeArticle.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setName() {
        mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvHomeUsername.text = getString(R.string.greeting, user.name)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
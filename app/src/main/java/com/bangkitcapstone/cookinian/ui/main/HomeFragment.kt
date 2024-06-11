package com.bangkitcapstone.cookinian.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.databinding.FragmentHomeBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.ui.recipe_search.RecipeSearchActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipeCategoryAdapter: RecipeCategoryAdapter

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
        setupRecipeRecyclerView()
        setupCategoryRecyclerView()
        setupBanner()

        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    val intent = Intent(requireContext(), RecipeSearchActivity::class.java)
                    intent.putExtra("searchQuery", query)
                    startActivity(intent)
                }
                closeKeyboard()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        binding.tvHomeSeeAllRecipe.setOnClickListener { seeAllRecipe() }
    }

    private fun setupBanner() {
        val imageUrls = listOf(
            "https://www.masakapahariini.com/wp-content/uploads/2021/04/shutterstock_1890524233-510x306.jpg",
            "https://www.masakapahariini.com/wp-content/uploads/2021/04/shutterstock_1890524233-510x306.jpg",
            "https://www.masakapahariini.com/wp-content/uploads/2021/04/shutterstock_1890524233-510x306.jpg"
        )

        val adapter = HomeBannerAdapter(imageUrls)
        binding.vpHomeBanner.adapter = adapter

        val dotsIndicator: DotsIndicator = binding.dotsHomeBanner
        dotsIndicator.setViewPager2(binding.vpHomeBanner)
    }

    private fun seeAllRecipe() {
        startActivity(Intent(requireContext(), RecipeSearchActivity::class.java))
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
            recipeCategoryAdapter = RecipeCategoryAdapter(category)
            binding.rvHomeCategory.adapter = recipeCategoryAdapter
        }

        binding.rvHomeCategory.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun setName() {
        mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvHomeUsername.text = getString(R.string.greeting, user.name)
        }
    }

    private fun closeKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
        binding.searchView.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
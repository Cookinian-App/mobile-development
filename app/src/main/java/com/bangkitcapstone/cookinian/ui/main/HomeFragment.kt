package com.bangkitcapstone.cookinian.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    private lateinit var autoSlideHandler: Handler
    private lateinit var autoSlideRunnable: Runnable

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
        setupAutoSlide()

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
        val imageResIds = listOf(
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3
        )
        val adapter = HomeBannerAdapter(imageResIds)
        binding.vpHomeBanner.adapter = adapter
        binding.dotsHomeBanner.attachTo(binding.vpHomeBanner)
    }

    private fun setupAutoSlide() {
        autoSlideHandler = Handler(Looper.getMainLooper())
        autoSlideRunnable = object : Runnable {
            override fun run() {
                val itemCount = binding.vpHomeBanner.adapter?.itemCount ?: 0
                if (itemCount > 1) {
                    val nextItem = (binding.vpHomeBanner.currentItem + 1) % itemCount
                    binding.vpHomeBanner.setCurrentItem(nextItem, true)
                    autoSlideHandler.postDelayed(this, 5000)
                }
            }
        }
        autoSlideHandler.postDelayed(autoSlideRunnable, 5000)
    }

    private fun seeAllRecipe() {
        startActivity(Intent(requireContext(), RecipeSearchActivity::class.java))
    }

    private fun setupRecipeRecyclerView() {
        mainViewModel.recipes.observe(viewLifecycleOwner) { recipe ->
            recipeAdapter = RecipeAdapter(recipe)
            binding.rvHomeRecipe.adapter = recipeAdapter
        }

        binding.rvHomeRecipe.setHasFixedSize(true)
        binding.rvHomeRecipe.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupCategoryRecyclerView() {
        mainViewModel.categories.observe(viewLifecycleOwner) { category ->
            recipeCategoryAdapter = RecipeCategoryAdapter(category)
            binding.rvHomeCategory.adapter = recipeCategoryAdapter
        }

        binding.rvHomeCategory.setHasFixedSize(true)
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
        autoSlideHandler.removeCallbacks(autoSlideRunnable)
        _binding = null
    }
}
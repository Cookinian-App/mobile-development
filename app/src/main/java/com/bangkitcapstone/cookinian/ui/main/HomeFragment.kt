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
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.databinding.FragmentHomeBinding
import com.bangkitcapstone.cookinian.helper.ConnectivityHelper
import com.bangkitcapstone.cookinian.helper.Event
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.helper.showAlert
import com.bangkitcapstone.cookinian.ui.recipe_search.RecipeSearchActivity
import com.bumptech.glide.Glide

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

    private var isErrorOccurred = Event(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfile()
        setupBanner()
        setupAutoSlide()

        if (!ConnectivityHelper.isOnline(requireContext())) {
            if (!isErrorOccurred.peekContent()) {
                showAlert(requireContext(),
                    getString(R.string.error_title), getString(R.string.error_no_internet))
                binding.tvHomeEmptyRecipe.visibility = View.VISIBLE
                binding.tvHomeEmptyCategory.visibility = View.VISIBLE
                isErrorOccurred = Event(true)
            }
        } else {
            try {
                fetchData()
            } catch (e: Exception) {
                showAlert(requireContext(), getString(R.string.error_title), e.message.toString())
            }
        }

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

    private fun fetchData() {
        mainViewModel.getSavedRecipe()
        mainViewModel.getRecipes()
        mainViewModel.getCategories()
        setSavedRecipe()
        setupRecipeRecyclerView()
        setupCategoryRecyclerView()
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
        autoSlideHandler.postDelayed(autoSlideRunnable, 4000)
    }

    private fun seeAllRecipe() {
        startActivity(Intent(requireContext(), RecipeSearchActivity::class.java))
    }

    private fun setupRecipeRecyclerView() {
        mainViewModel.recipes.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.pbHomeRecipe.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.pbHomeRecipe.visibility = View.GONE
                    recipeAdapter = RecipeAdapter(result.data)
                    binding.rvHomeRecipe.adapter = recipeAdapter
                }
                is Result.Error -> {
                    Event(result.error).getContentIfNotHandled()?.let {
                        binding.pbHomeRecipe.visibility = View.GONE
                        binding.tvHomeEmptyRecipe.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.rvHomeRecipe.setHasFixedSize(true)
        binding.rvHomeRecipe.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupCategoryRecyclerView() {
        mainViewModel.categories.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.pbHomeArticle.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.pbHomeArticle.visibility = View.GONE
                    recipeCategoryAdapter = RecipeCategoryAdapter(result.data)
                    binding.rvHomeCategory.adapter = recipeCategoryAdapter
                }
                is Result.Error -> {
                    Event(result.error).getContentIfNotHandled()?.let {
                        binding.pbHomeArticle.visibility = View.GONE
                        binding.tvHomeEmptyCategory.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.rvHomeCategory.setHasFixedSize(true)
        binding.rvHomeCategory.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun setSavedRecipe() {
        mainViewModel.savedRecipe.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {}
                is Result.Error -> {}
            }
        }
    }

    private fun setProfile() {
        mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvHomeUsername.text = getString(R.string.greeting, user.name)
            Glide.with(requireContext())
                .load(user.avatarUrl)
                .into(binding.ivHomeProfile)
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

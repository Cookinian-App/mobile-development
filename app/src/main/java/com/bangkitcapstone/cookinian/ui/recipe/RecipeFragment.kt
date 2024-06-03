package com.bangkitcapstone.cookinian.ui.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.databinding.FragmentRecipeBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private val recipeViewModel by viewModels<RecipeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRecipeList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvRecipeList.isNestedScrollingEnabled = false

        setupRecipeListRecyclerView()
    }

    private fun setupRecipeListRecyclerView() {
        val adapter = RecipeListAdapter()
        binding.rvRecipeList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        recipeViewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            adapter.submitData(viewLifecycleOwner.lifecycle, recipe)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
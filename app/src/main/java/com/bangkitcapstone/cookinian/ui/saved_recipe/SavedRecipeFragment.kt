package com.bangkitcapstone.cookinian.ui.saved_recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.databinding.FragmentSavedRecipeBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.helper.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SavedRecipeFragment : Fragment() {
    private var _binding: FragmentSavedRecipeBinding? = null
    private val binding get() = _binding!!
    private val savedRecipeViewModel by viewModels<SavedRecipeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var recipeAdapter: SavedRecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSavedRecipeRecyclerView()

        binding.fabSavedRecipeDeleteAll.setOnClickListener {
            if (recipeAdapter.itemCount > 0) {
                deleteAllSavedRecipe()
            } else {
                showToast(requireContext(), getString(R.string.saved_recipe_empty))
            }
        }
    }

    private fun setupSavedRecipeRecyclerView() {
        savedRecipeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                savedRecipeViewModel.getSavedRecipe().observe(viewLifecycleOwner) { result ->
                    recipeAdapter = SavedRecipeAdapter(result)
                    binding.rvSavedRecipe.adapter = recipeAdapter
                }
            }
        }

        binding.rvSavedRecipe.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(context)
        binding.rvSavedRecipe.layoutManager = layoutManager
    }

    private fun deleteAllSavedRecipe() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete))
            .setMessage(getString(R.string.saved_recipe_delete_all))
            .setPositiveButton(R.string.dialog_positive_button) { _, _ ->
                savedRecipeViewModel.deleteAllSavedRecipes()
            }
            .setNegativeButton(getString(R.string.dialog_negative_button)) { _, _ -> }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
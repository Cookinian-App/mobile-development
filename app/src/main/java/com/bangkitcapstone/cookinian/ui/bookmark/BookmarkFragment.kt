package com.bangkitcapstone.cookinian.ui.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.databinding.FragmentBookmarkBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory

class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private val bookmarkViewModel by viewModels<BookmarkViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var recipeAdapter: SavedRecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBookmarkRecyclerView()
    }

    private fun setupBookmarkRecyclerView() {
        bookmarkViewModel.savedRecipe.observe(viewLifecycleOwner) { recipe ->
            recipeAdapter = SavedRecipeAdapter(recipe)
            binding.rvBookmark.adapter = recipeAdapter
        }

        binding.rvBookmark.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(context)
        binding.rvBookmark.layoutManager = layoutManager
    }

}
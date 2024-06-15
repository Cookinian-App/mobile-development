package com.bangkitcapstone.cookinian.ui.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkitcapstone.cookinian.databinding.FragmentArticleBinding
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.helper.ViewModelFactory

class ArticleFragment : Fragment() {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private val articleViewModel by viewModels<ArticleViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var articleCategoryAdapter: ArticleCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategoryRecyclerView()
    }

    private fun setupCategoryRecyclerView() {
        articleViewModel.categories.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.pbArticle.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.pbArticle.visibility = View.GONE
                    articleCategoryAdapter = ArticleCategoryAdapter(result.data)
                    binding.rvArticleCategory.adapter = articleCategoryAdapter
                }
                is Result.Error -> {
                    binding.pbArticle.visibility = View.GONE
                }
            }
        }

        binding.rvArticleCategory.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

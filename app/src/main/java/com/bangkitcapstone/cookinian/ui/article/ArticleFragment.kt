package com.bangkitcapstone.cookinian.ui.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.databinding.FragmentArticleBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory

class ArticleFragment : Fragment() {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private val articleViewModel by viewModels<ArticleViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var articleCategoryAdapter: ArticleCategoryAdapter
    private lateinit var articleAdapter: ArticleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvArticle.isNestedScrollingEnabled = false

        setupCategoryRecyclerView()
        setupArticleListRecyclerView()

    }

    private fun setupCategoryRecyclerView() {
        articleViewModel.categories.observe(viewLifecycleOwner) { category ->
            articleCategoryAdapter = ArticleCategoryAdapter(category)
            binding.rvArticleCategory.adapter = articleCategoryAdapter
        }

        binding.rvArticleCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupArticleListRecyclerView(dataCategory: String? = null) {
        articleAdapter = ArticleListAdapter()
        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticle.isNestedScrollingEnabled = false
        binding.rvArticle.adapter = articleAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                articleAdapter.retry()
            }
        )


        articleViewModel.getArticlesWithPaging(dataCategory).observe(viewLifecycleOwner) {
            articleAdapter.submitData(lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
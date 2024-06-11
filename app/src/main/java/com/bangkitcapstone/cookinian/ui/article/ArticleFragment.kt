package com.bangkitcapstone.cookinian.ui.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkitcapstone.cookinian.databinding.FragmentArticleBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

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

        setupCategoryRecyclerView()
    }

    private fun setupCategoryRecyclerView() {
        articleViewModel.categories.observe(viewLifecycleOwner) { category ->
            articleCategoryAdapter = ArticleCategoryAdapter(category, articleViewModel.selectedItem) { selectedCategoryKey, selectedPosition ->
                articleViewModel.currentCategory = selectedCategoryKey
                articleViewModel.selectedItem = selectedPosition
                setupArticleListRecyclerView(selectedCategoryKey)
            }
            binding.rvArticleCategory.adapter = articleCategoryAdapter
            setupArticleListRecyclerView(articleViewModel.currentCategory)
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

        lifecycleScope.launch {
            articleAdapter.loadStateFlow
                .distinctUntilChanged { old, new ->
                    old.mediator?.prepend?.endOfPaginationReached == new.mediator?.prepend?.endOfPaginationReached
                }
                .filter {
                    it.refresh is androidx.paging.LoadState.NotLoading
                            && it.prepend.endOfPaginationReached
                            && !it.append.endOfPaginationReached
                }
                .collect {
                    binding.rvArticle.scrollToPosition(0)
                }
        }

        articleViewModel.getArticleWithPaging(dataCategory).observe(viewLifecycleOwner) { pagingData ->
            articleAdapter.submitData(lifecycle, pagingData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

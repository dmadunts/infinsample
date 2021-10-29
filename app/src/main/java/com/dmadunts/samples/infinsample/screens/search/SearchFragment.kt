package com.dmadunts.samples.infinsample.screens.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmadunts.samples.infinsample.R
import com.dmadunts.samples.infinsample.databinding.FragmentSearchBinding
import com.dmadunts.samples.infinsample.extensions.gone
import com.dmadunts.samples.infinsample.extensions.hide
import com.dmadunts.samples.infinsample.extensions.visible
import com.dmadunts.samples.infinsample.screens.BaseFragment
import com.dmadunts.samples.infinsample.utils.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private var searchJob: Job? = null
    private val viewModel: SearchViewModel by inject()
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView() {
        searchAdapter = SearchAdapter(object : SearchAdapter.OnRepoClickListener {
            override fun onRepoClicked(owner: String, repo: String) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("${Constants.BASE_URL}/$owner/${repo}")
                )
                requireContext().startActivity(intent)
            }
        })
        binding.recycler.adapter = searchAdapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.searchField.addTextChangedListener {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(1000)
                val query = it?.toString()
                if (!query.isNullOrEmpty()) {
                    binding.progress.visible()
                    viewModel.searchRepos(query)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.searchReposResponse.observe(viewLifecycleOwner, { pagingData ->
            lifecycleScope.launch {
                binding.progress.hide()
                searchAdapter.submitData(pagingData)
            }
        })

        lifecycleScope.launch {
            searchAdapter.loadStateFlow.collectLatest { loadState ->
                if (loadState.refresh is LoadState.Loading) {
                    binding.progress.visible()
                } else {
                    binding.progress.hide()
                }
                if (loadState.refresh is LoadState.Error) {
                    showSnackbar(
                        (loadState.refresh as LoadState.Error).error.message
                            ?: resources.getString(R.string.Error)
                    )
                    binding.progress.hide()
                }
            }
        }

        searchAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && searchAdapter.itemCount == 0) {
                binding.emptyView.visible()
                binding.emptyView.text = resources.getString(R.string.NothingFound)
                binding.recycler.gone()
            } else {
                binding.emptyView.hide()
                binding.recycler.visible()
            }
        }
    }
}
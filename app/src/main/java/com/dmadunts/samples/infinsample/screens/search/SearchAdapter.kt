package com.dmadunts.samples.infinsample.screens.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dmadunts.samples.infinsample.databinding.ItemRepoBinding
import com.dmadunts.samples.infinsample.extensions.loadImage
import com.dmadunts.samples.infinsample.remote.models.Item

class SearchAdapter(private val listener: OnRepoClickListener) :
    PagingDataAdapter<Item, SearchAdapter.SearchViewHolder>(
        ITEM_COMPARATOR
    ) {
    interface OnRepoClickListener {
        fun onRepoClicked(owner: String, repo: String)
    }

    private lateinit var _binding: ItemRepoBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        _binding =
            ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(_binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        getItem(position)?.let { item ->
            with(holder) {
                binding.avatar.loadImage(item.owner.avatar_url)
                binding.langName.text = item.language
                binding.repoName.text = item.full_name
                itemView.setOnClickListener {
                    listener.onRepoClicked(item.owner.login, item.name)
                }
            }
        }
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem == newItem
        }
    }

    class SearchViewHolder(val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root)

}

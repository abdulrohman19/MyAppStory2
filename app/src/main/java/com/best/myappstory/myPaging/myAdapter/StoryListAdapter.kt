package com.best.myappstory.myPaging.myAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.best.myappstory.databinding.ItemToStoriesBinding
import com.best.myappstory.myResponse.ListStoryItem
import com.best.myappstory.myStories.Story
import com.bumptech.glide.Glide

class StoryListAdapter: PagingDataAdapter<ListStoryItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
        fun onItemMapsClicked(data: Story)
    }

    class MyViewHolder(private val binding: ItemToStoriesBinding): RecyclerView.ViewHolder(binding.root) {
        val ivPhoto = binding.ivItemPhoto
        val tvStory = binding.tvItemName
        val ivLoc = binding.ivMapsAvailability
        fun bind(data: ListStoryItem) {
            this.tvStory.text = data.name
            Glide.with(this.itemView.context).load(data.photoUrl).into(this.ivPhoto)
            if (!data.lon.isNullOrEmpty() && !data.lat.isNullOrEmpty()) {
                ivLoc.visibility = View.VISIBLE
            } else {
                ivLoc.visibility = View.GONE
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
            val dataItem = Story(data.id, data.name, data.photoUrl, data.description, data.lat, data.lon)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(dataItem)
            }
            holder.ivLoc.setOnClickListener {
                onItemClickCallback.onItemMapsClicked(dataItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemToStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
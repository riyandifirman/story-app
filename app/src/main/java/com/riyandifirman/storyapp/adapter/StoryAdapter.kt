package com.riyandifirman.storyapp.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riyandifirman.storyapp.databinding.ItemRowBinding
import com.riyandifirman.storyapp.response.ListStoryItem
import com.riyandifirman.storyapp.ui.detailstory.DetailStoryActivity
import java.text.SimpleDateFormat
import java.util.*

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    class MyViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStoryItem) {

            // fungsi untuk mengubah format tanggal
            val dateString = item.createdAt
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = outputFormat.format(date)

            binding.apply {
                tvItemName.text = item.name
                tvItemDate.text = formattedDate
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .into(ivItemPhoto)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                    val bundle = Bundle()

                    bundle.putString("list_name", item.name)
                    bundle.putString("list_date", formattedDate)
                    bundle.putString("list_image", item.photoUrl)
                    bundle.putString("list_description", item.description)

                    intent.putExtras(bundle)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
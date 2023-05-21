package com.riyandifirman.storyapp.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riyandifirman.storyapp.R
import com.riyandifirman.storyapp.data.Story
import com.riyandifirman.storyapp.databinding.ItemRowBinding
import com.riyandifirman.storyapp.response.ListStoryItem
import com.riyandifirman.storyapp.ui.detailstory.DetailStoryActivity
import java.text.SimpleDateFormat
import java.util.*

//class StoryAdapter(
//    private val list: List<ListStoryItem>,
//    private val listener: OnAdapterClickListener
//) : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
//    // class untuk menampung data yang akan ditampilkan
//    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val name = view.findViewById(R.id.tv_item_name) as TextView
//        val photo = view.findViewById(R.id.iv_item_photo) as ImageView
//        val date = view.findViewById(R.id.tv_item_date) as TextView
//    }
//
//    // fungsi untuk menampilkan data
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
//        return ListViewHolder(view)
//    }
//
//    // fungsi untuk menghitung jumlah data
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    // fungsi untuk mengatur data yang akan ditampilkan
//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        val story = list[position]
//
//        // fungsi untuk mengubah format tanggal
//        val dateString = story.createdAt
//        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//        val date = inputFormat.parse(dateString)
//        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//        val formattedDate = outputFormat.format(date)
//
//        holder.name.text = story.name
//        holder.date.text = formattedDate
//        Glide.with(holder.photo)
//            .load(story.photoUrl)
//            .into(holder.photo)
//
//        holder.itemView.setOnClickListener {
//            listener.onClick(story)
//        }
//    }
//
//    // interface untuk menangani click listener
//    interface OnAdapterClickListener {
//        fun onClick(story: ListStoryItem)
//    }
//}

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null){
            holder.bind(item)
        }
    }

    class MyViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
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
                    val intent = Intent (itemView.context, DetailStoryActivity::class.java)
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

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
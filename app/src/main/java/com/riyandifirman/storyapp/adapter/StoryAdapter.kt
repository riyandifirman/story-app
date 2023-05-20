package com.riyandifirman.storyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riyandifirman.storyapp.R
import com.riyandifirman.storyapp.response.ListStoryItem
import java.text.SimpleDateFormat
import java.util.*

class StoryAdapter(
    private val list: List<ListStoryItem>,
    private val listener: OnAdapterClickListener
) : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    // class untuk menampung data yang akan ditampilkan
    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById(R.id.tv_item_name) as TextView
        val photo = view.findViewById(R.id.iv_item_photo) as ImageView
        val date = view.findViewById(R.id.tv_item_date) as TextView
    }

    // fungsi untuk menampilkan data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ListViewHolder(view)
    }

    // fungsi untuk menghitung jumlah data
    override fun getItemCount(): Int {
        if (list.size > 0) {
            return list.size
        } else {
            return 0
        }
    }

    // fungsi untuk mengatur data yang akan ditampilkan
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = list[position]

        // fungsi untuk mengubah format tanggal
        val dateString = story.createdAt
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = outputFormat.format(date)

        holder.name.text = story.name
        holder.date.text = formattedDate
        Glide.with(holder.photo)
            .load(story.photoUrl)
            .into(holder.photo)

        holder.itemView.setOnClickListener {
            listener.onClick(story)
        }
    }

    // interface untuk menangani click listener
    interface OnAdapterClickListener {
        fun onClick(story: ListStoryItem)
    }
}
package com.riyandifirman.storyapp.ui.detailstory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.riyandifirman.storyapp.databinding.ActivityDetailStoryBinding
import java.text.SimpleDateFormat
import java.util.*

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDetailName.text = intent.getStringExtra("list_name")

        val dateString = intent.getStringExtra("list_date")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = outputFormat.format(date)
        binding.tvDate.text = formattedDate

        binding.tvDetailDescription.text = intent.getStringExtra("list_description")
        Glide.with(this)
            .load(intent.getStringExtra("list_image"))
            .into(binding.ivDetailPhoto)
    }
}
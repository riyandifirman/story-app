package com.riyandifirman.storyapp.ui.detailstory

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.riyandifirman.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDetailName.text = intent.getStringExtra("list_name")
        binding.tvDate.text = intent.getStringExtra("list_date")
        binding.tvDetailDescription.text = intent.getStringExtra("list_description")

        Log.d("KirimNama", "onCreate: ${intent.getStringExtra("list_name")}")
        Log.d("KirimTanggal", "onCreate: ${intent.getStringExtra("list_date")}")
        Log.d("KirimDeskripsi", "onCreate: ${intent.getStringExtra("list_description")}")
        Log.d("KirimGambar", "onCreate: ${intent.getStringExtra("list_image")}")

        Glide.with(this)
            .load(intent.getStringExtra("list_image"))
            .into(binding.ivDetailPhoto)
    }
}
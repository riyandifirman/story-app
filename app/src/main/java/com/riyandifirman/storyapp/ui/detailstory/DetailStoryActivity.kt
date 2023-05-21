package com.riyandifirman.storyapp.ui.detailstory

import android.nfc.NfcAdapter.EXTRA_DATA
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.riyandifirman.storyapp.databinding.ActivityDetailStoryBinding
import com.riyandifirman.storyapp.response.ListStoryItem
import java.text.SimpleDateFormat
import java.util.*

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDetailName.text = intent.getStringExtra("list_name")

//        val dateString = intent.getStringExtra("list_date")
//        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//        val date = inputFormat.parse(dateString)
//        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//        val formattedDate = outputFormat.format(date)
        binding.tvDate.text = intent.getStringExtra("list_date")
        binding.tvDetailDescription.text = intent.getStringExtra("list_description")

        Log.d("KirimNama", "onCreate: ${intent.getStringExtra("list_name")}")
        Log.d("KirimTanggal", "onCreate: ${intent.getStringExtra("list_date")}")
        Log.d("KirimDeskripsi", "onCreate: ${intent.getStringExtra("list_description")}")
        Log.d("KirimGambar", "onCreate: ${intent.getStringExtra("list_image")}")

        Glide.with(this)
            .load(intent.getStringExtra("list_image"))
            .into(binding.ivDetailPhoto)

//        binding.tvDetailName.text = intent.getStringExtra("list_name")
//
//        val dateString = intent.getStringExtra("list_date")
//        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//        val date = inputFormat.parse(dateString)
//        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//        val formattedDate = outputFormat.format(date)
//        binding.tvDate.text = formattedDate
//
//        binding.tvDetailDescription.text = intent.getStringExtra("list_description")
//        Glide.with(this)
//            .load(intent.getStringExtra("list_image"))
//            .into(binding.ivDetailPhoto)
    }
}
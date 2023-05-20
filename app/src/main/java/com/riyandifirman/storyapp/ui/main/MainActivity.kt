package com.riyandifirman.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.riyandifirman.storyapp.R
import com.riyandifirman.storyapp.adapter.StoryAdapter
import com.riyandifirman.storyapp.databinding.ActivityMainBinding
import com.riyandifirman.storyapp.response.GetStoryResponse
import com.riyandifirman.storyapp.response.ListStoryItem
import com.riyandifirman.storyapp.settings.ApiConfig
import com.riyandifirman.storyapp.settings.Preferences
import com.riyandifirman.storyapp.ui.addstory.AddStoryActivity
import com.riyandifirman.storyapp.ui.detailstory.DetailStoryActivity
import com.riyandifirman.storyapp.ui.login.LoginActivity
import com.riyandifirman.storyapp.ui.maps.MapsActivity
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myPreference: Preferences
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreference = Preferences(this)
        myPreference.setStatusLogin(true)

        val rvStory: RecyclerView = binding.rvStory

        // fungsi untuk mengambil data story
        getStory()
        updateStory()

        fab = binding.fab
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    // fungsi untuk menampilkan option menu logout
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // fungsi ketika menu logout di klik
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            val alertDialogBuilder = AlertDialog.Builder(this)
            with(alertDialogBuilder) {
                setTitle("Log Out")
                setMessage("Are you sure want to log out?")
                setPositiveButton("YES") { dialog, which ->
                    myPreference.clearUserToken()
                    myPreference.clearUserLogin()
                    myPreference.setStatusLogin(false)
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                setNegativeButton("NO") { dialog, which ->
                    dialog.dismiss()
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        } else if (item.itemId == R.id.action_map) {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getStory() {
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        scope.launch {
            val token = "Bearer ${myPreference.getUserToken()}"
            withContext(Dispatchers.Main) {
                val client = ApiConfig.getApiService().getAllStory(token)
                client.enqueue(object : Callback<GetStoryResponse> {
                    override fun onResponse(
                        call: Call<GetStoryResponse>,
                        response: Response<GetStoryResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                val dataStory = responseBody.listStory
                                val storyAdapter = StoryAdapter(
                                    dataStory,
                                    object : StoryAdapter.OnAdapterClickListener {
                                        override fun onClick(story: ListStoryItem) {
                                            val intent = Intent(
                                                this@MainActivity,
                                                DetailStoryActivity::class.java
                                            )
                                            val bundle = Bundle()

                                            bundle.putString("list_name", story.name)
                                            bundle.putString("list_image", story.photoUrl)
                                            bundle.putString("list_description", story.description)
                                            bundle.putString("list_date", story.createdAt)

                                            intent.putExtras(bundle)
                                            startActivity(intent)
                                        }

                                    })

                                binding.rvStory.apply {
                                    layoutManager = LinearLayoutManager(this@MainActivity)
                                    setHasFixedSize(true)
                                    adapter = storyAdapter
                                }

                            }
                        }
                    }

                    override fun onFailure(call: Call<GetStoryResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                })
            }
        }
    }

    private fun updateStory() {
        binding.apply {
            if (intent != null) {
                val isNewStory = intent.extras?.getBoolean(SUCCESS_ADD_STORY)
                if (isNewStory != null && isNewStory) {
                    getStory()
                }
            }
        }
    }

    companion object {
        const val SUCCESS_ADD_STORY = "success_add_story"
    }
}
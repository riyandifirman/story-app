package com.riyandifirman.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.riyandifirman.storyapp.R
import com.riyandifirman.storyapp.adapter.LoadingStateAdapter
import com.riyandifirman.storyapp.adapter.StoryAdapter
import com.riyandifirman.storyapp.databinding.ActivityMainBinding
import com.riyandifirman.storyapp.settings.Preferences
import com.riyandifirman.storyapp.ui.addstory.AddStoryActivity
import com.riyandifirman.storyapp.ui.login.LoginActivity
import com.riyandifirman.storyapp.ui.maps.MapsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var myPreference: Preferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: StoryAdapter

    private var storyName: ArrayList<String>? = null
    private var storyImage: ArrayList<String>? = null
    private var storyDescription: ArrayList<String>? = null
    private var storyDate: ArrayList<String>? = null

    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreference = Preferences(this)

        // fungsi untuk mengambil data story
        getStory()
        updateStory()

        fab = binding.fab
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    // fungsi untuk mengambil data story
    private fun getStory() {
        myPreference.setStatusLogin(true)
        val token = myPreference.getUserToken()

        viewModel.allStory.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        storyName = ArrayList()
        storyImage = ArrayList()
        storyDescription = ArrayList()
        storyDate = ArrayList()

        viewModel.getAllStory("Bearer $token")
        viewModel.getStoriesPaging().observe(this) {
            if (it != null) {
                for (i in it.indices) {
                    storyName?.add(it[i].name)
                    storyImage?.add(it[i].photoUrl)
                    storyDescription?.add(it[i].description)
                    storyDate?.add(it[i].createdAt)
                }
            } else {
                Toast.makeText(this, "Empty Data", Toast.LENGTH_SHORT).show()
            }
        }

        Action()
    }

    private fun Action() {
        adapter = StoryAdapter()

        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStory.setHasFixedSize(true)
            rvStory.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
        }
    }

    // fungsi untuk mengupdate data story
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

    companion object {
        const val SUCCESS_ADD_STORY = "success_add_story"
    }
}
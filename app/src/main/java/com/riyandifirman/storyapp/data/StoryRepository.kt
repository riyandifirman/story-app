package com.riyandifirman.storyapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.riyandifirman.storyapp.response.ListStoryItem
import com.riyandifirman.storyapp.settings.ApiService
import com.riyandifirman.storyapp.settings.Preferences

class StoryRepository(private val apiService: ApiService, private val context: Context) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, context)
            }
        ).liveData
    }
}
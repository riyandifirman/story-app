package com.riyandifirman.storyapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.riyandifirman.storyapp.response.ListStoryItem
import com.riyandifirman.storyapp.settings.ApiService

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
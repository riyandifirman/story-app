package com.riyandifirman.storyapp.di

import android.content.Context
import com.riyandifirman.storyapp.data.StoryRepository
import com.riyandifirman.storyapp.settings.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService, context)
    }
}
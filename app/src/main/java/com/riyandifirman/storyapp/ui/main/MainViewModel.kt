package com.riyandifirman.storyapp.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.riyandifirman.storyapp.data.StoryRepository
import com.riyandifirman.storyapp.di.Injection
import com.riyandifirman.storyapp.response.GetStoryResponse
import com.riyandifirman.storyapp.response.ListStoryItem
import com.riyandifirman.storyapp.settings.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(storyRepository: StoryRepository) : ViewModel() {
    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    private val listStory: LiveData<List<ListStoryItem>> = _listStory

    val allStory: LiveData<PagingData<ListStoryItem>> by lazy {
        storyRepository.getStory().cachedIn(viewModelScope)
    }

    fun getStoriesPaging() = listStory

    fun getAllStory(token: String) {
        val client = ApiConfig.getApiService().getAllStory(token)
        client.enqueue(object : Callback<GetStoryResponse> {
            override fun onResponse(
                call: Call<GetStoryResponse>,
                response: Response<GetStoryResponse>
            ) {
                if (response.isSuccessful) {
                    _listStory.postValue(response.body()?.listStory)
                }
            }

            override fun onFailure(call: Call<GetStoryResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }

    class Factory(private var context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(Injection.provideRepository(context)) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}

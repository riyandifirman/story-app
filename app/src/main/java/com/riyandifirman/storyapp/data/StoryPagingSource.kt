package com.riyandifirman.storyapp.data

import android.content.Context
import android.util.Log
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.riyandifirman.storyapp.response.ListStoryItem
import com.riyandifirman.storyapp.settings.ApiService
import com.riyandifirman.storyapp.settings.Preferences
import retrofit2.HttpException
import java.io.IOException

class StoryPagingSource (private val apiService: ApiService, context: Context) : PagingSource<Int, ListStoryItem>() {

    private var myPreferences = Preferences(context)

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {

            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = "Bearer ${myPreferences.getUserToken()}"

            val responseData = apiService.getAllStoryPaging(token, position, params.loadSize)
            val data = responseData.listStory
            LoadResult.Page(
                data = data,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (data.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException){
            return LoadResult.Error(exception)
        }
    }


    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
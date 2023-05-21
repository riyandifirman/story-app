package com.riyandifirman.storyapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.riyandifirman.storyapp.adapter.StoryAdapter
import com.riyandifirman.storyapp.data.StoryPagingSource
import com.riyandifirman.storyapp.data.StoryRepository
import com.riyandifirman.storyapp.response.ListStoryItem
import com.riyandifirman.storyapp.utils.DataDummy
import com.riyandifirman.storyapp.utils.MainDispatcherRule
import com.riyandifirman.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainDispatcherRule()

    private lateinit var mainViewModel: MainViewModel

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(storyRepository)
    }

    // Test Case 1 : Ketika berhasil memuat data story
    @Test
    fun `when Get Stories Should Not Null and Return Success Data`() = runTest {
        val dummyAllStory = DataDummy.generateDummyAllStory()
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyAllStory)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data

        `when`(storyRepository.getStory()).thenReturn(expectedStory)

        val actualStory: PagingData<ListStoryItem> = mainViewModel.allStory.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStory)

        // Memastikan data tidak null.
        assertNotNull(differ.snapshot())
        // Memastikan jumlah data sesuai dengan yang diharapkan.
        assertEquals(dummyAllStory.size, differ.snapshot().size)
        // Memastikan data pertama yang dikembalikan sesuai.
        assertEquals(dummyAllStory[0], differ.snapshot()[0])
    }

    // Test Case 2 : Ketika tidak ada data story
    @Test
    fun `when Get Stories Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data

        `when`(storyRepository.getStory()).thenReturn(expectedStory)

        val actualStory: PagingData<ListStoryItem> = mainViewModel.allStory.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStory)

        // Memastikan jumlah data yang dikembalikan nol
        assertEquals(0, differ.snapshot().size)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
package com.best.myappstory.myViewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.best.myappstory.myPaging.myAdapter.StoryListAdapter
import com.best.myappstory.myPaging.myData.StoryRepository
import com.best.myappstory.myResponse.ListStoryItem
import com.best.myappstory.utils.MainCoroutineRule
import com.best.myappstory.utils.PagingDataTestSource
import com.best.myappstory.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var mainViewModel: MainViewModel

    @Test
    fun `when get story should not null and return right data`() = runTest {
        val dummyStoryItem = DummyData.genDummyListOfStory()
        val dummyData = PagingDataTestSource.snapshot(dummyStoryItem)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = dummyData

        Mockito.`when`(storyRepository.getStory()).thenReturn(expectedStory.asFlow())

        mainViewModel = MainViewModel(storyRepository)
        val actualStory = mainViewModel.story.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )

        differ.submitData(actualStory)

        advanceUntilIdle()
        Mockito.verify(storyRepository).getStory()
        assertNotNull(differ.snapshot())
        assertEquals(dummyStoryItem.size, differ.snapshot().size)
        assertEquals(dummyStoryItem[0], differ.snapshot()[0])
    }

    @Test
    fun `when get empty story should return no data`() = runTest {
        val dummyEmptyData = PagingData.from(emptyList<ListStoryItem>())
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = dummyEmptyData

        Mockito.`when`(storyRepository.getStory()).thenReturn(expectedStory.asFlow())

        mainViewModel = MainViewModel(storyRepository)
        val actualStory = mainViewModel.story.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )

        differ.submitData(actualStory)

        advanceUntilIdle()
        Mockito.verify(storyRepository).getStory()
        assertEquals(0, differ.snapshot().size)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
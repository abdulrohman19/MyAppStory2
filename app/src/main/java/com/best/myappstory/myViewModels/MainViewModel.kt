package com.best.myappstory.myViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.best.myappstory.myPaging.myData.StoryRepository
import com.best.myappstory.myResponse.ListStoryItem

class MainViewModel (storyRepository: StoryRepository): ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope).asLiveData()

    companion object {
        private const val TAG = "MainActivity"
    }
}
package com.best.myappstory.myPaging.myData

import androidx.paging.*
import com.best.myappstory.myApi.Apiservice
import com.best.myappstory.myResponse.ListStoryItem
import kotlinx.coroutines.flow.Flow

class StoryRepository(private val apiService: Apiservice) {
    fun getStory(): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).flow
    }
}
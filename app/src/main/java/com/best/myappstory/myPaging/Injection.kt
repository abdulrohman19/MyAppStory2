package com.best.myappstory.myPaging

import android.content.Context
import com.best.myappstory.myAct.getToken
import com.best.myappstory.myApi.ApiConfig
import com.best.myappstory.myPaging.myData.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService(getToken(context))
        return StoryRepository(apiService)
    }
}
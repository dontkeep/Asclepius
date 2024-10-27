package com.dicoding.asclepius.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.local.entity.ResultEntity
import com.dicoding.asclepius.local.room.dao.ResultDao
import com.dicoding.asclepius.network.conf.ApiService
import com.dicoding.asclepius.network.response.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

class AppRepository(private val resultDao: ResultDao, private val apiService: ApiService) {

   private val apiKey = BuildConfig.API_KEY

   suspend fun getNews(): Call<NewsResponse>{
      return withContext(Dispatchers.IO) {
         apiService.getNews("health", "en", "cancer", apiKey)
      }
   }

   suspend fun insertResult(result: ResultEntity) {
      resultDao.insertResult(result)
   }

   suspend fun deleteResult(result: ResultEntity) {
      resultDao.deleteResult(result)
   }

   fun getAllResult(): LiveData<List<ResultEntity>> = liveData {
      val result: LiveData<List<ResultEntity>> = resultDao.getAllResult()
      emitSource(result)
   }

   companion object {
      @Volatile
      private var instance: AppRepository? = null
      fun getInstance(
         resultDao: ResultDao,
         apiService: ApiService
      ): AppRepository =
         instance ?: synchronized(this) {
            instance ?: AppRepository(resultDao, apiService)
         }.also { instance = it }
   }
}
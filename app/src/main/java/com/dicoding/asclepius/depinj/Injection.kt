package com.dicoding.asclepius.depinj

import android.content.Context
import com.dicoding.asclepius.local.repository.AppRepository
import com.dicoding.asclepius.local.room.db.DatabaseResult
import com.dicoding.asclepius.network.conf.ApiConfig

object Injection {
   fun provideRepository(context: Context): AppRepository {
      val database = DatabaseResult.getInstance(context)
      val dao = database.resultDao()
      val apiService = ApiConfig.getApiService()
      return AppRepository.getInstance(dao, apiService)
   }
}
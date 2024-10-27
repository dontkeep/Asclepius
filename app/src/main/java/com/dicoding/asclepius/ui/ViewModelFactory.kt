package com.dicoding.asclepius.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.depinj.Injection
import com.dicoding.asclepius.local.repository.AppRepository
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import com.dicoding.asclepius.viewmodel.HomeViewModel
import com.dicoding.asclepius.viewmodel.NewsViewModel
import com.dicoding.asclepius.viewmodel.ResultViewModel

class ViewModelFactory private constructor(
   private val appRepository: AppRepository,
) :
   ViewModelProvider.NewInstanceFactory() {

   @Suppress("UNCHECKED_CAST")
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return when {
         modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
            HomeViewModel() as T
         }

         modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
            HistoryViewModel(appRepository) as T
         }

         modelClass.isAssignableFrom(ResultViewModel::class.java) -> {
            ResultViewModel(appRepository) as T
         }

         modelClass.isAssignableFrom(NewsViewModel::class.java) -> {
            NewsViewModel(appRepository) as T
         }

         else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
      }
   }

   companion object {
      @Volatile
      private var instance: ViewModelFactory? = null
      fun getInstance(context: Context): ViewModelFactory =
         instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context))
         }.also { instance = it }
   }
}

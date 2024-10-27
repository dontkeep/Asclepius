package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.local.repository.AppRepository
import com.dicoding.asclepius.network.response.ArticlesItem
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.await
import java.io.IOException

class NewsViewModel(private val appRepository: AppRepository) : ViewModel() {
   private val _news = MutableLiveData<List<ArticlesItem>>()
   val news: LiveData<List<ArticlesItem>> = _news

   private val _loading = MutableLiveData<Boolean>()
   val loading: LiveData<Boolean> = _loading

   private val _errorMessage = MutableLiveData<String>()
   val errorMessage: LiveData<String> = _errorMessage

   init {
      _loading.value = false
   }

   fun getNews(){
      _loading.value = true
      viewModelScope.launch {
         try {
            _news.value = appRepository.getNews().await().articles
         } catch (e: IOException) {
            _errorMessage.value = "Network Error"
            _news.value = emptyList()
         } catch (e: HttpException) {
            _errorMessage.value = "Server Error"
            _news.value = emptyList()
         } finally {
            _loading.value = false
         }
      }
   }
}
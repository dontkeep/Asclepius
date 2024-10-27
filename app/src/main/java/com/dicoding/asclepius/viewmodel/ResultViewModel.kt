package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.local.entity.ResultEntity
import com.dicoding.asclepius.local.repository.AppRepository

class ResultViewModel(private val repository: AppRepository): ViewModel() {
   suspend fun saveResult(result: ResultEntity) {
      repository.insertResult(result)
   }
}
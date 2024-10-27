package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.local.entity.ResultEntity
import com.dicoding.asclepius.local.repository.AppRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: AppRepository): ViewModel() {
   val allResult: LiveData<List<ResultEntity>> = repository.getAllResult()

   fun deleteResult(result: ResultEntity) {
      viewModelScope.launch {
         repository.deleteResult(result)
      }
   }
}
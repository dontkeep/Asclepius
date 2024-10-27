package com.dicoding.asclepius.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ResultEntity(
   @PrimaryKey(autoGenerate = true)
   val id: Int = 0,

   val imageUri: String,

   val resultText: String,

   val inferenceTime: String,

   val date: String
)

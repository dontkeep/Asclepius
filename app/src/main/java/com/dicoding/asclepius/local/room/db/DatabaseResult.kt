package com.dicoding.asclepius.local.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.local.entity.ResultEntity
import com.dicoding.asclepius.local.room.dao.ResultDao

@Database(entities = [ResultEntity::class], version = 1, exportSchema = false)
abstract class DatabaseResult: RoomDatabase() {
   abstract fun resultDao(): ResultDao

   companion object {
      @Volatile
      private var INSTANCE: DatabaseResult? = null

      fun getInstance(context: Context): DatabaseResult {
         return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
               context.applicationContext,
               DatabaseResult::class.java,
               "result_database"
            ).build()
            INSTANCE = instance
            instance
         }
      }
   }
}
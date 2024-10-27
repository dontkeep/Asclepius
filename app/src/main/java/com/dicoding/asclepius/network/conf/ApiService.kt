package com.dicoding.asclepius.network.conf

import com.dicoding.asclepius.network.response.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
   @GET("top-headlines")
   fun getNews(
      @Query("category") country: String,
      @Query("language") lang: String,
      @Query("q") query: String,
      @Query("apiKey") apiKey: String
   ): Call<NewsResponse>
}
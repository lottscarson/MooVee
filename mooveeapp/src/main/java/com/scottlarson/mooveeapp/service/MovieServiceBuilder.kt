package com.scottlarson.mooveeapp.service

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieServiceBuilder {
    private const val URL = "https://api.themoviedb.org/3/"
    val movieService : MovieService = Retrofit.Builder()
        .baseUrl(URL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MovieService::class.java)
}
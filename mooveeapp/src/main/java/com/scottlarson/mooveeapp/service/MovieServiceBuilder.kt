package com.scottlarson.mooveeapp.service

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieServiceBuilder private constructor(context: Context) {
    val movieService: MovieService
    val cacheSize = (5 * 1024 * 1024).toLong()
    init {
        val okCache = Cache(context.cacheDir, cacheSize)
        val okHttpClient = OkHttpClient.Builder()
            .cache(okCache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(context)!!) {
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                } else {
                    request.newBuilder().header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=" + 60 * 60 * 24
                    ).build()
                }
                chain.proceed(request)
            }
            .build()

        val URL = "https://api.themoviedb.org/3/"
        movieService = Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieService::class.java)
    }

    @SuppressLint("ServiceCast")
    fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    companion object : SingletonHolder<MovieServiceBuilder, Context>(::MovieServiceBuilder)
}

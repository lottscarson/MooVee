package com.scottlarson.mooveeapp.service

import com.scottlarson.mooveeapp.models.Movie
import com.scottlarson.mooveeapp.models.Movies
import com.scottlarson.mooveeapp.models.Review
import com.scottlarson.mooveeapp.models.Reviews
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {
    @GET("discover/movie?api_key=51dde878761550c3178baaf0ec4f323f")
    suspend fun getMovies() : Response<Movies>

    @GET("movie/{movie_id}?api_key=51dde878761550c3178baaf0ec4f323f")
    suspend fun getDetails(@Path("movie_id") movieId: String) : Response<Movie>

    @GET("movie/{movie_id}/reviews?api_key=51dde878761550c3178baaf0ec4f323f")
    suspend fun getReviews(@Path("movie_id") movieId: String) : Response<Reviews>
}

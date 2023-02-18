package com.movietask.di

import com.movietask.model.MoviesResponse
import com.movietask.utils.Constants
import retrofit2.http.*


interface ApiInterface {

    @GET("Top250Movies/"+Constants.API_KEY)
    suspend fun getMovies(): MoviesResponse

}

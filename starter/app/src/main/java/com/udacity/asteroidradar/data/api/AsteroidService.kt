package com.udacity.asteroidradar.data.api

import com.udacity.asteroidradar.model.PictureOfDay
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidService {
    @GET("neo/rest/v1/feed")
    suspend fun getFeed(): String

    @GET("planetary/apod")
    fun imageOfTheDay(): Call<PictureOfDay>


}
package com.udacity.asteroidradar.data.api

import com.udacity.asteroidradar.model.PictureOfDay
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET

interface AsteroidService {
    @GET("/icare/api/public/survey/list")
    fun getFeed(): Call<JSONObject>

    @GET("/iplanetary/apod")
    fun imageOfTheDay(): Call<PictureOfDay>


}
package com.udacity.asteroidradar.model

import com.squareup.moshi.Json

data class FeedResponse(@Json(name = "media_type") val mediaType: String, val title: String,
                        val url: String)
package com.udacity.asteroidradar.data.api

import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.HttpUrl

import okhttp3.Interceptor
import okhttp3.Request


class ServiceFactory {

    fun retrieveService(): AsteroidService {

        val client = OkHttpClient.Builder()
        client.interceptors().add(APIKeyInterceptor())
        client.build()


        val retrofitICare = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .build()

        return retrofitICare.create(AsteroidService::class.java)
    }


    fun APIKeyInterceptor():Interceptor{
        return Interceptor { chain ->
            var request: Request = chain.request()
            val url: HttpUrl = request.url().newBuilder().addQueryParameter("API_KEY", BuildConfig.NASA_API_KEY).build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }
    }
}
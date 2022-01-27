package com.udacity.asteroidradar.data.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.HttpUrl

import okhttp3.Interceptor
import okhttp3.Request
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class ServiceFactory {

    fun retrieveService(): AsteroidService {
        val client = OkHttpClient.Builder()
            .addInterceptor(APIKeyInterceptor())
            .build()

       val moshi = Moshi.Builder().build()
        val retrofitICare = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        return retrofitICare.create(AsteroidService::class.java)
    }


    private fun APIKeyInterceptor():Interceptor{
        return Interceptor { chain ->
            var request: Request = chain.request()
            val url: HttpUrl = request.url().newBuilder().addQueryParameter("api_key", BuildConfig.NASA_API_KEY).build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }
    }
}
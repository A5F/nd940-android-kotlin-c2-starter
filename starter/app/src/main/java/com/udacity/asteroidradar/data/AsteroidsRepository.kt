package com.udacity.asteroidradar.data

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.data.api.AsteroidService
import com.udacity.asteroidradar.data.api.ServiceFactory
import com.udacity.asteroidradar.data.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.model.PictureOfDay
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AsteroidsRepository {

    lateinit var retrofitService :AsteroidService
    companion object {
        private var INSTANCE :AsteroidsRepository? = null
        fun getInstance():AsteroidsRepository?{
            if (INSTANCE== null){
                INSTANCE = AsteroidsRepository()
                INSTANCE?.retrofitService = ServiceFactory().retrieveService()

            }
            return INSTANCE
        }
    }


    fun getImageOfTheDay(onSuccess: (picture: PictureOfDay) -> Unit,
                         onError:(e:Throwable)->Unit){
        val call = retrofitService.imageOfTheDay()
        call.enqueue(object: Callback<PictureOfDay> {
            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
                if (response.body()!=null){
                    onSuccess(response.body()!!)
                }else{
                    onError(Exception("Empty Body"))
                }
            }

            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                onError(t)
            }

        })

    }

    fun getFeedList(onSuccess: (list: ArrayList<Asteroid>) -> Unit,
                    onError:(e:Throwable)->Unit){
        val call = retrofitService.getFeed()
        call.enqueue(object: Callback<JSONObject> {
            override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                val body = response.body()
                if (body != null){
                    val parsedResponse= parseAsteroidsJsonResult(response.body()!!)
                    onSuccess(parsedResponse)
                }else{
                    onError(Exception("Empty Body"))
                }

            }

            override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                onError(t)
            }

        })
    }





}
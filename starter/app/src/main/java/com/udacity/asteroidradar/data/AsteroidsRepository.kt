package com.udacity.asteroidradar.data

import android.content.Context
import android.util.Log
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.data.api.AsteroidService
import com.udacity.asteroidradar.data.api.ServiceFactory
import com.udacity.asteroidradar.data.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.local.*
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AsteroidsRepository {

    private val startDate = LocalDateTime.now()
    private val endDate = LocalDateTime.now().minusDays(7)

    lateinit var retrofitService: AsteroidService

    companion object {
        private var INSTANCE: AsteroidsRepository? = null
        fun getInstance(context: Context): AsteroidsRepository? {
            if (INSTANCE == null) {
                INSTANCE = AsteroidsRepository()
                INSTANCE?.retrofitService = ServiceFactory().retrieveService()
                INSTANCE?.asteroidDatabase = getDatabase(context)
            }
            return INSTANCE
        }
    }

    lateinit var asteroidDatabase: AsteroidDatabase

    fun getImageOfTheDay(
        onSuccess: (picture: PictureOfDay) -> Unit,
        onError: (e: Throwable) -> Unit
    ) {
        val call = retrofitService.imageOfTheDay()
        call.enqueue(object : Callback<PictureOfDay> {
            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
                if (response.body() != null) {
                    onSuccess(response.body()!!)
                } else {
                    onError(Exception("Empty Body"))
                }
            }

            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                onError(t)
            }

        })

    }




     fun getFeedList(filterType: FilterAsteroid): List<Asteroid> {


        val asteroidList: List<Asteroid> = when (filterType){
            FilterAsteroid.TODAY ->{
                asteroidDatabase.asteroidDao.getAsteroidsDay(startDate.format(DateTimeFormatter.ISO_DATE)).map { it.toDomainModel() }
            }
            FilterAsteroid.WEEK ->{
                asteroidDatabase.asteroidDao.getAsteroidsByDate(
                    endDate = startDate.format(DateTimeFormatter.ISO_DATE),
                    startDate = endDate.format(DateTimeFormatter.ISO_DATE)
                ).map { it.toDomainModel() }
            }
            FilterAsteroid.ALL ->{
                asteroidDatabase.asteroidDao.getAsteroids().map { it.toDomainModel() }
            }
        }
        return asteroidList
    }

    suspend fun getFeedListSync(onSuccess:(List<Asteroid>)->Unit={},
                            onError:()-> Unit={}) {
        withContext(Dispatchers.IO) {
            try {
                val asteroids = retrofitService.getFeed()
                    val result = parseAsteroidsJsonResult(JSONObject(asteroids))
                    asteroidDatabase.asteroidDao.insertAll(result.map { it.toDatabaseModel()})
                    onSuccess(result)
                    Log.d("Refresh getFeedListSync", "Success")
            }   catch (err: Exception) {
                Log.e(" getFeedListSync", "Failed:"+err.message.toString())
        }
    }

}
    enum class FilterAsteroid {
        TODAY,
        WEEK,
        ALL

    }
}
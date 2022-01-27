package com.udacity.asteroidradar.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.data.AsteroidsRepository
import retrofit2.HttpException

class RefreshWorker(private val appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "AsteroidWorker"
    }

    override suspend fun doWork(): Result {
        Log.d("RefreshWorker", "start work")
        val repository = AsteroidsRepository.getInstance(appContext)
        return try {

            repository!!.getFeedListSync()
            Log.d("RefreshWorker", "success")

            Result.success()

        } catch (e: HttpException) {
            Log.d("RefreshWorker", "retry")
            Result.retry()


        }
    }
}
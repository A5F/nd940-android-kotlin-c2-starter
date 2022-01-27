package com.udacity.asteroidradar.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroidTable ORDER BY closeApproachDate DESC")
    fun getAsteroids(): List<AsteroidTable>

    @Query("SELECT * FROM asteroidTable WHERE closeApproachDate = :startDate ORDER BY closeApproachDate DESC")
    fun getAsteroidsDay(startDate: String): List<AsteroidTable>

    @Query("SELECT * FROM asteroidTable WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate DESC")
    fun getAsteroidsByDate(startDate: String, endDate: String): List<AsteroidTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroidList: List<AsteroidTable>)
}
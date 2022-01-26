package com.udacity.asteroidradar.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroidTable")
data class AsteroidTable constructor(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)


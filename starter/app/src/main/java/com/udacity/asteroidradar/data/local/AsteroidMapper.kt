package com.udacity.asteroidradar.data.local

import com.udacity.asteroidradar.model.Asteroid



fun AsteroidTable.toDomainModel(): Asteroid {
        return Asteroid(
                id = this.id,
                codename = this.codename,
                closeApproachDate = this.closeApproachDate,
                absoluteMagnitude = this.absoluteMagnitude,
                estimatedDiameter = this.estimatedDiameter,
                relativeVelocity = this.relativeVelocity,
                distanceFromEarth = this.distanceFromEarth,
                isPotentiallyHazardous = this.isPotentiallyHazardous)

    }

fun Asteroid.toDatabaseModel(): AsteroidTable {
        return AsteroidTable(
                id = this.id,
                codename = this.codename,
                closeApproachDate = this.closeApproachDate,
                absoluteMagnitude = this.absoluteMagnitude,
                estimatedDiameter = this.estimatedDiameter,
                relativeVelocity = this.relativeVelocity,
                distanceFromEarth = this.distanceFromEarth,
                isPotentiallyHazardous = this.isPotentiallyHazardous
            )
        }

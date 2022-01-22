package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.data.AsteroidsRepository
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _feedEvent = MutableLiveData<List<Asteroid>>()
    val _feedLiveData: LiveData<List<Asteroid>>
        get() = _feedEvent

    private val _imageOfDayEvent = MutableLiveData<PictureOfDay>()
    val _imageOfDayEventLiveData: LiveData<PictureOfDay>
        get() = _imageOfDayEvent

    fun downloadData(){
        getImageOfTheDay()
        
        getFeedList()
    }
    fun getImageOfTheDay(){

        viewModelScope.launch {
            AsteroidsRepository.getInstance()?.getImageOfTheDay(onError = {

            }, onSuccess = {

            })?:errorImage()
        }
    }

    fun errorImage(){

    }

    fun getFeedList(){
        viewModelScope.launch {
            AsteroidsRepository.getInstance()?.getFeedList(onError = {

            }, onSuccess = {

            })?:errorFeed()
        }
    }

    fun errorFeed(){

    }

}
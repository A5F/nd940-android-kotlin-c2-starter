package com.udacity.asteroidradar.presentation

import androidx.lifecycle.*
import com.udacity.asteroidradar.data.AsteroidsRepository
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.launch

class MainViewModel(private val asteroidsRepository: AsteroidsRepository) : ViewModel() {

    private val _feedEvent = MutableLiveData<List<Asteroid>>()
    val _feedLiveData: LiveData<List<Asteroid>>
        get() = _feedEvent

    private val _imageOfDayEvent = MutableLiveData<PictureOfDay?>()
    val _imageOfDayEventLiveData: LiveData<PictureOfDay?>
        get() = _imageOfDayEvent

    var currentAsteroid : Asteroid? = null
    private var currentFilter :AsteroidsRepository.FilterAsteroid = AsteroidsRepository.FilterAsteroid.ALL

    fun downloadData(){
        getImageOfTheDay()
        getFeedList()
    }

    init {
        viewModelScope.launch {
            asteroidsRepository.getFeedListSync(onSuccess = {
                _feedEvent.postValue(it)
            })

        }
    }

    private fun getImageOfTheDay(){

        viewModelScope.launch {
            asteroidsRepository.getImageOfTheDay(onError = {
                _imageOfDayEvent.postValue(null)
            }, onSuccess = {
                _imageOfDayEvent.postValue(it)
            })
        }
    }

    private fun getFeedList(){
        viewModelScope.launch {
            val asteroidList = asteroidsRepository.getFeedList(currentFilter)
            _feedEvent.postValue(asteroidList)
        }
    }

    fun onChangeFilter(filter: AsteroidsRepository.FilterAsteroid) {
        currentFilter= filter
        getFeedList()
    }


    class Factory(val asteroidsRepository: AsteroidsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(asteroidsRepository) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}
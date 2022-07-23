package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import com.udacity.asteroidradar.database.AsteroidTable
import com.udacity.asteroidradar.database.PicOfDayDatabaseDao
import com.udacity.asteroidradar.database.PicOfDayTable
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    val database: AsteroidDatabaseDao,
    val picOfDayDatabase: PicOfDayDatabaseDao
) : ViewModel() {

    private val _navigateToDetailFragment = MutableLiveData<AsteroidTable>()
    val navigateToDetailFragment
        get() = _navigateToDetailFragment

    fun onAsteroidClicked(asteroidItem: AsteroidTable) {
        _navigateToDetailFragment.value = asteroidItem
    }

    fun onDetailFragmentNavigated() {
        _navigateToDetailFragment.value = null
    }

    private val _weekly = MutableLiveData<List<AsteroidTable>>()
    val weekly: LiveData<List<AsteroidTable>>
        get() = _weekly

    private val _today = MutableLiveData<List<AsteroidTable>>()
    val today: LiveData<List<AsteroidTable>>
        get() = _today

    private val _saved = MutableLiveData<List<AsteroidTable>>()
    val saved: LiveData<List<AsteroidTable>>
        get() = _saved


    private val _asteroid = MutableLiveData<ArrayList<AsteroidTable>>()
    val asteroid: LiveData<ArrayList<AsteroidTable>>
        get() = _asteroid

    private val _image = MutableLiveData<PicOfDayTable>()
    val image: LiveData<PicOfDayTable>
        get() = _image

    init {

        initializeAsteroids()
    }

    private fun initializeAsteroids() {

        val asteroidReository: AsteroidsRepository =
            AsteroidsRepository(database, picOfDayDatabase)
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

            asteroidReository.refreshAsteroids()
        }

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

            asteroidReository.refreshPicOfDay()
        }


    }

    fun showPicOfDay() {
        viewModelScope.launch(Dispatchers.Default) {
            var temp = picOfDayDatabase.getLatestPic()
            withContext(Dispatchers.Main)
            {
                _image.value = temp
            }
        }
    }


    fun showWeekly() {
        viewModelScope.launch(Dispatchers.Default) {
            var temp = database.getAsteroidsWeekly(Constants.START_DATE)
            withContext(Dispatchers.Main)
            {
                _weekly.value = temp
            }


        }
    }

    fun showToday() {
        viewModelScope.launch(Dispatchers.Default) {
            var temp = database.getAsteroidsToday(Constants.START_DATE)
            withContext(Dispatchers.Main)
            {
                _today.value = temp
            }


        }
    }

    fun showSaved() {
        viewModelScope.launch(Dispatchers.Default) {
            var temp = database.getAsteroidsSaved()
            withContext(Dispatchers.Main)
            {
                _saved.value = temp
            }


        }
    }

}
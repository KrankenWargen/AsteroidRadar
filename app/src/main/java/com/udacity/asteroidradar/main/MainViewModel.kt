package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.PlanetsApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import com.udacity.asteroidradar.database.AsteroidTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class MainViewModel(
    val database: AsteroidDatabaseDao,
    application: Application
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

    private val _image = MutableLiveData<PictureOfDay>()
    val image: LiveData<PictureOfDay>
        get() = _image

    init {

        initializeAsteroids()
    }

    private fun initializeAsteroids() {
        PlanetsApi.retrofitService.getPicDay(Constants.URL_PIC_DAY)
            .enqueue(object : retrofit2.Callback<PictureOfDay> {
                override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<PictureOfDay>,
                    response: Response<PictureOfDay>
                ) {
                    _image.value = response.body()
                }
            })

        PlanetsApi.retrofitService.getAsteroids(Constants.URL_PLANETS)
            .enqueue(object : retrofit2.Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.i("MainViewModel", "Failure")

                }

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    Log.i("MainViewModel", "Success")
                    var obj = JSONObject(response.body())
                    var asteroidsList: ArrayList<AsteroidTable> = parseAsteroidsJsonResult(obj)
                    insertAll(asteroidsList)

                    _asteroid.value = asteroidsList
                }
            })
    }

    fun insertAll(asteroidsList: ArrayList<AsteroidTable>) {
        viewModelScope.launch(Dispatchers.Default) {
            database.insertAll(*asteroidsList.toTypedArray())
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
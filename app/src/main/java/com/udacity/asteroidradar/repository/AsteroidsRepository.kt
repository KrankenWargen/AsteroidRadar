/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.PlanetsApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import com.udacity.asteroidradar.database.AsteroidTable
import com.udacity.asteroidradar.database.PicOfDayDatabaseDao
import com.udacity.asteroidradar.database.PicOfDayTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await

class AsteroidsRepository(
    private val database: AsteroidDatabaseDao,
    private val database_pod: PicOfDayDatabaseDao
) {


    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = PlanetsApi.retrofitService.getAsteroids(Constants.URL_PLANETS).await()
            var obj = JSONObject(asteroids)
            var asteroidsList: ArrayList<AsteroidTable> = parseAsteroidsJsonResult(obj)
            database.insertAll(*asteroidsList.toTypedArray())

        }
    }

    suspend fun refreshPicOfDay() {
        withContext(Dispatchers.Default) {
            val picOfDay = PlanetsApi.retrofitService.getPicDay(Constants.URL_PIC_DAY).await()
            database_pod.insert(PicOfDayTable(picOfDay.url, picOfDay.title, picOfDay.date))

        }
    }
}

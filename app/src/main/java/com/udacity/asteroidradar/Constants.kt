package com.udacity.asteroidradar

import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates

object Constants {
    const val API_QUERY_DATE_FORMAT = "YYYY-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov/"
    const val YOUR_API_KEY = "BvLERlUgmvHZgYgmU3iJ5acxTekNrAw9yPJi4Vv4"
    var START_DATE = getNextSevenDaysFormattedDates().get(0)
    var END_DATE = getNextSevenDaysFormattedDates().get(7)
    var URL_PLANETS = "neo/rest/v1/feed?start_date=${START_DATE}&end_date=${END_DATE}&api_key=${YOUR_API_KEY}"
    var URL_PIC_DAY = "planetary/apod?api_key=${YOUR_API_KEY}"
}
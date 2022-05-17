package com.example.todolist.service.http

import com.example.todolist.vo.WeatherForecastResVO
import com.example.todolist.vo.WeatherReqVO
import com.example.todolist.vo.WeatherResVO
import com.google.gson.GsonBuilder
import okhttp3.*

class WeatherService {
    var client = OkHttpClient()

    fun getWeatherInfo(weatherReqVO: WeatherReqVO): WeatherResVO? {
        var weatherRes : WeatherResVO? = null
        val url = "https://api.openweathermap.org/data/2.5/weather?q=${weatherReqVO.query}&appid=&units=imperial"
        val request = Request
            .Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        if(response.isSuccessful) {
            val body = response.body?.string()
            println(body)
            val gson = GsonBuilder().create()
            weatherRes = gson.fromJson(body, WeatherResVO::class.java)
        } else {
            println("Error Response")
        }

        return weatherRes
    }

    fun getWeatherForecastInfo(weatherReqVO: WeatherReqVO): WeatherForecastResVO? {
        var weatherForecastRes : WeatherForecastResVO? = null
        val url = "https://api.openweathermap.org/data/2.5/forecast?q=${weatherReqVO.query}&appid=&units=imperial"
        val request = Request
                .Builder()
                .url(url)
                .build()

        val response = client.newCall(request).execute()
        if(response.isSuccessful) {
            val body = response.body?.string()
            // find out how to get list only from response
            val gson = GsonBuilder().create()
            weatherForecastRes = gson.fromJson(body, WeatherForecastResVO::class.java)
        } else {
            println("Error Response")
        }

        return weatherForecastRes
    }
}

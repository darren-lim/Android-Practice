package com.example.todolist.vo

import com.google.gson.annotations.SerializedName

data class WeatherResVO (
    @SerializedName("name")
    val name : String = "",
    @SerializedName("weather")
    val weather : List<WeatherDataVO>,
    @SerializedName("main")
    val main : WeatherMainDataVO,
    @SerializedName("dt")
    val dt : Long = 0,
    @SerializedName("timezone")
    val timezone : Long = 0
) {
    data class WeatherDataVO (
        @SerializedName("icon")
        val icon : String = "",
        @SerializedName("description")
        val description : String = "",
    )

    data class WeatherMainDataVO (
        @SerializedName("temp")
        val temp : Double = 0.0,
        @SerializedName("temp_min")
        val temp_min : Double = 0.0,
        @SerializedName("temp_max")
        val temp_max : Double = 0.0
    )
}
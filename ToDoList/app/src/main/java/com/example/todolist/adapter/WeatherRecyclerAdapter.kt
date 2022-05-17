package com.example.todolist.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.vo.WeatherData
import com.example.todolist.vo.WeatherForecastResVO
import com.example.todolist.vo.WeatherResVO
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class WeatherRecyclerAdapter(var data: ArrayList<WeatherData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_CURRENT = 1
        const val VIEW_TYPE_FORECAST = 2
    }

    var currData :  ArrayList<WeatherData> = data

    inner class CurrentViewHolder(v: View): RecyclerView.ViewHolder(v) {
        var cityName: TextView = v.findViewById(R.id.tv_city_name)
        var temp: TextView = v.findViewById(R.id.tv_temperature)
        var imageIcon: ImageView = v.findViewById(R.id.image_icons)
        var description: TextView = v.findViewById(R.id.tv_description)
        var tempLow: TextView = v.findViewById(R.id.tvTempLow)
        var tempHigh: TextView = v.findViewById(R.id.tvTempHigh)
        var tvDateTime: TextView = v.findViewById(R.id.tvDateTime)
        var hourlyRecycler: RecyclerView = v.findViewById(R.id.recyclerview_hourly)
        lateinit var hourlyRecyclerAdapter: WeatherHourlyRecyclerAdapter
        fun bind(position: Int) {
            val weatherRes : WeatherResVO = (currData[position].data as WeatherResVO)
            val weatherData = weatherRes.weather[0]
            val tempData = weatherRes.main

            setWeatherIcon(imageIcon, weatherData.icon)

            cityName.text = weatherRes.name
            description.text = weatherData.description
            temp.text = round(tempData.temp).toInt().toString() + "\u2109"
            val lowText = "Low: ${round(tempData.temp_min).toInt()}" + "\u2109"
            val highText = "High: ${round(tempData.temp_max).toInt()}" + "\u2109"
            tempLow.text = lowText
            tempHigh.text = highText
            tvDateTime.text = getDateTime(weatherRes.dt, weatherRes.timezone)

            hourlyRecyclerAdapter = WeatherHourlyRecyclerAdapter(currData.slice(1..11) as ArrayList<WeatherData>)
            hourlyRecycler.adapter = hourlyRecyclerAdapter
        }
    }

    inner class ForecastViewHolder(v: View): RecyclerView.ViewHolder(v) {
        var dayOfWeek: TextView = v.findViewById(R.id.tv_day)
        var dayIcon: ImageView = v.findViewById(R.id.image_day_icon)
        var dayTemps: TextView = v.findViewById(R.id.tv_day_temps)

        @RequiresApi(Build.VERSION_CODES.N)
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(position: Int) {
            val dailyRes : WeatherForecastResVO.WeatherDailyDataVO = (currData[position].data as WeatherForecastResVO.WeatherDailyDataVO)

            var dateFormat = android.icu.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            dateFormat.timeZone = android.icu.util.TimeZone.getTimeZone("UTC")
            val date = dateFormat.parse(dailyRes.dt_txt)
            dateFormat = android.icu.text.SimpleDateFormat("EEE, MMM d HH:mm")
            dateFormat.timeZone = android.icu.util.TimeZone.getDefault()
            val newDate = dateFormat.format(date)

            dayOfWeek.text = newDate

            setWeatherIcon(dayIcon, dailyRes.weather[0].icon)

            dayTemps.text = "L: ${round(dailyRes.main.temp_min).toInt()}" + "\u2109" + " H: ${round(dailyRes.main.temp_max).toInt()}" + "\u2109"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == VIEW_TYPE_CURRENT) {
            return CurrentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_recycler, parent, false))
        }
        return ForecastViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_daily_item, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(currData[position].viewType == VIEW_TYPE_CURRENT) {
            (holder as CurrentViewHolder).bind(position)
        } else {
            (holder as ForecastViewHolder).bind(position)
        }

    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        return currData[position].viewType
    }

    private fun setWeatherIcon(imageIcon: ImageView, iconName: String) {
        when(iconName) {
            "01d" -> imageIcon.setImageResource(R.drawable.ic_clear_sky)
            "01n" -> imageIcon.setImageResource(R.drawable.ic_clear_sky)
            "02d" -> imageIcon.setImageResource(R.drawable.ic_few_clouds)
            "02n" -> imageIcon.setImageResource(R.drawable.ic_few_clouds)
            "03d" -> imageIcon.setImageResource(R.drawable.ic_scattered_clouds)
            "03n" -> imageIcon.setImageResource(R.drawable.ic_scattered_clouds)
            "04d" -> imageIcon.setImageResource(R.drawable.ic_broken_clouds)
            "04n" -> imageIcon.setImageResource(R.drawable.ic_broken_clouds)
            "09d" -> imageIcon.setImageResource(R.drawable.ic_shower_rain)
            "10d" -> imageIcon.setImageResource(R.drawable.ic_rain)
            "11d" -> imageIcon.setImageResource(R.drawable.ic_thunderstorm)
            "13d" -> imageIcon.setImageResource(R.drawable.ic_snow)
            "50d" -> imageIcon.setImageResource(R.drawable.ic_mist)
            else -> imageIcon.setImageResource(R.drawable.ic_clear_sky)
        }
        imageIcon.visibility = View.VISIBLE
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(dt: Long, timeZone: Long): String? {
        try {
            val now = dt + timeZone
            val netDate = Date(now * 1000)
            val formatter = SimpleDateFormat("MM/dd/yyyy HH:mm a", Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
}
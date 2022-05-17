package com.example.todolist.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.vo.WeatherData
import com.example.todolist.vo.WeatherForecastResVO
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round

class WeatherHourlyRecyclerAdapter(var items: ArrayList<WeatherData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        var timeText: TextView = v.findViewById(R.id.tv_hourly_time)
        var hourlyText: TextView = v.findViewById(R.id.tv_hourly_temp)
        var hourlyIcon: ImageView = v.findViewById(R.id.image_hourly_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_hourly_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder){
            val item = items[position].data as WeatherForecastResVO.WeatherDailyDataVO

            var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            dateFormat.timeZone = android.icu.util.TimeZone.getTimeZone("UTC")
            val date = dateFormat.parse(item.dt_txt)
            dateFormat = SimpleDateFormat("MM/dd HH:mm")
            dateFormat.timeZone = android.icu.util.TimeZone.getDefault()
            val newDate = dateFormat.format(date)
            holder.timeText.text = newDate
            holder.hourlyText.text = round(item.main.temp).toInt().toString() + "\u2109"

            when(item.weather[0].icon) {
                "01d" -> holder.hourlyIcon.setImageResource(R.drawable.ic_clear_sky)
                "01n" -> holder.hourlyIcon.setImageResource(R.drawable.ic_clear_sky)
                "02d" -> holder.hourlyIcon.setImageResource(R.drawable.ic_few_clouds)
                "02n" -> holder.hourlyIcon.setImageResource(R.drawable.ic_few_clouds)
                "03d" -> holder.hourlyIcon.setImageResource(R.drawable.ic_scattered_clouds)
                "03n" -> holder.hourlyIcon.setImageResource(R.drawable.ic_scattered_clouds)
                "04d" -> holder.hourlyIcon.setImageResource(R.drawable.ic_broken_clouds)
                "04n" -> holder.hourlyIcon.setImageResource(R.drawable.ic_broken_clouds)
                "09d" -> holder.hourlyIcon.setImageResource(R.drawable.ic_shower_rain)
                "10d" -> holder.hourlyIcon.setImageResource(R.drawable.ic_rain)
                "11d" -> holder.hourlyIcon.setImageResource(R.drawable.ic_thunderstorm)
                "13d" -> holder.hourlyIcon.setImageResource(R.drawable.ic_snow)
                "50d" -> holder.hourlyIcon.setImageResource(R.drawable.ic_mist)
                else -> holder.hourlyIcon.setImageResource(R.drawable.ic_clear_sky)
            }
            holder.hourlyIcon.visibility = View.VISIBLE
        }

    }

    override fun getItemCount() = items.size
}
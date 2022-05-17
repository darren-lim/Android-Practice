package com.example.todolist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.todolist.adapter.WeatherHourlyRecyclerAdapter
import com.example.todolist.adapter.WeatherRecyclerAdapter
import com.example.todolist.databinding.WeatherFragmentBinding
import com.example.todolist.service.http.WeatherService
import com.example.todolist.util.LoadingUtil
import com.example.todolist.vo.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

// openweatherapi key: cb7d947dcacaad81c6bffcbc59f13d0c

class WeatherFragment : Fragment() {
    private lateinit var binding: WeatherFragmentBinding

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: WeatherRecyclerAdapter

    private lateinit var mSwipeRefreshView: SwipeRefreshLayout
    private lateinit var mSearchCity: String
    private var refresh: Boolean = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = WeatherFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    fun init() {
        mSearchCity = ""
        binding.etCity.requestFocus()

        mSwipeRefreshView = binding.weatherRefresh
        mSwipeRefreshView.setOnRefreshListener {
            refresh = true
            fetchData()
        }

        binding.btnGetWeather.setOnClickListener {
            fetchData()
        }
        mRecyclerView = binding.recyclerviewDaily
    }

    private fun setData(data: WeatherResVO?, forecast: WeatherForecastResVO?) {
        CoroutineScope(Dispatchers.Main).launch {
            if(data == null || forecast == null) {
                binding.tvNoData.visibility = View.VISIBLE
                stopRefresh()
                return@launch
            } else {
                binding.tvNoData.visibility = View.GONE
            }

            val dataList = ArrayList<WeatherData>()

            dataList.add(WeatherData(WeatherRecyclerAdapter.VIEW_TYPE_CURRENT, data))
            forecast.list.forEach {
                dataList.add(WeatherData(WeatherRecyclerAdapter.VIEW_TYPE_FORECAST, it))
            }

            mRecyclerAdapter = WeatherRecyclerAdapter(dataList)
            mRecyclerView.adapter = mRecyclerAdapter
        }
    }

    private fun fetchData() {
        val context = requireContext()
        val weatherService = WeatherService()

        if(!refresh){
            mSearchCity = binding.etCity.text.toString()
        } else {
            refresh = false
        }

        CoroutineScope(Dispatchers.Main).launch {
            mSwipeRefreshView.isRefreshing = false
        }

        if(mSearchCity == "") {
            return
        }

        LoadingUtil.show(context, "Fetch data......")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val weatherReqVO = WeatherReqVO(
                        mSearchCity
                )

                val result: WeatherResVO? = weatherService.getWeatherInfo(weatherReqVO)
                val forecast : WeatherForecastResVO? = weatherService.getWeatherForecastInfo(weatherReqVO)
                println(forecast)
                setData(result, forecast)
                stopRefresh()
            } catch (e: Exception) {
                println("Error")
                stopRefresh()
                binding.tvNoData.visibility = View.VISIBLE
            }
        }
    }

    private fun stopRefresh() {
        LoadingUtil.hide()
        CoroutineScope(Dispatchers.Main).launch {
            mSwipeRefreshView.isRefreshing = false
        }
    }
}
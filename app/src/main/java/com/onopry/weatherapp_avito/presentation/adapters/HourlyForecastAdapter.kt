package com.onopry.weatherapp_avito.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.onopry.domain.model.forecast.Hourly
import com.onopry.weatherapp_avito.databinding.ItemHourlyForecastListBinding
import com.onopry.weatherapp_avito.utils.setImageByWeatherCode
import java.time.LocalDateTime

class HourlyForecastAdapter() :
    RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder>() {

    private val hourlyForecast = mutableListOf<Hourly>()

    fun updateHourlyForecastData(list: List<Hourly>) {
        val diffCallback = HourlyDiffCallback(newHourlyList = list, oldHourlyList = hourlyForecast)
        val calculateDiff = DiffUtil.calculateDiff(diffCallback)
        hourlyForecast.clear()
        hourlyForecast.addAll(list)
        calculateDiff.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        val binding = ItemHourlyForecastListBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return HourlyForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        holder.bind(hourlyForecast[position])
    }

    override fun getItemCount() = hourlyForecast.size

    inner class HourlyForecastViewHolder(private val binding: ItemHourlyForecastListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(hourlyWeather: Hourly) {
            val date = LocalDateTime.parse(hourlyWeather.time)
            binding.weatherStateImage.setImageByWeatherCode(hourlyWeather.weatherCode)
            binding.dateTv.text = date.toLocalTime().toString()
            binding.temperatureValTv.text = hourlyWeather.temperature
        }
    }
}

class HourlyDiffCallback(
    private val newHourlyList: List<Hourly>,
    private val oldHourlyList: List<Hourly>,
) : BaseDiffCallback<Hourly>(newHourlyList, oldHourlyList) {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        newHourlyList[newItemPosition].time == oldHourlyList[oldItemPosition].time

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        newHourlyList[newItemPosition] == oldHourlyList[oldItemPosition]
}
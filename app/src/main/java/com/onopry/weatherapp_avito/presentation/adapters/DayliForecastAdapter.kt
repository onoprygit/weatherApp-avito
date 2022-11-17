package com.onopry.weatherapp_avito.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.onopry.weatherapp_avito.databinding.ItemDayForecastBinding
import com.onopry.weatherapp_avito.presentation.model.DailyUi
import com.onopry.weatherapp_avito.utils.gone
import com.onopry.weatherapp_avito.utils.setDescriptionByWeatherCode
import com.onopry.weatherapp_avito.utils.setImageByWeatherCode
import com.onopry.weatherapp_avito.utils.show
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

typealias OnDaySelectedClickListener = () -> Unit

class DailyForecastAdapter() :
    RecyclerView.Adapter<DailyForecastAdapter.DailyForecastViewHolder>() {

    private val dailyForecast = mutableListOf<DailyUi>()
    private val hourlyForecastAdapter = HourlyForecastAdapter()

    fun updateDailyForecastData(list: List<DailyUi>) {
        val diffCallback = DailyDiffCallback(newDailyList = list, oldDailyList = dailyForecast)
        val calculateDiff = DiffUtil.calculateDiff(diffCallback)
        dailyForecast.clear()
        dailyForecast.addAll(list)
        calculateDiff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val binding = ItemDayForecastBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return DailyForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {

        holder.bind(dailyForecast[position])
    }

    override fun getItemCount() = dailyForecast.size

    inner class DailyForecastViewHolder(private val binding: ItemDayForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dailyWeather: DailyUi) {
            itemView.setOnClickListener {
                val currentExpandedState = dailyWeather.isExpanded
                dailyWeather.isExpanded = !currentExpandedState
                notifyItemChanged(adapterPosition)
            }
            //            val date = LocalDateTime.parse(dailyWeather.time)
            val date = LocalDate.parse(dailyWeather.time)
            val dayOfWeek: String =
                date.dayOfWeek.name
//                    .toCharArray().map { it.lowercase() }.toString().replaceFirstChar { it.titlecase() }
            val month: String = date.month.name
//                .getDisplayName(TextStyle.NARROW, Locale.ENGLISH).toCharArray().map { it.lowercase() }.toString().replaceFirstChar { it.titlecase() }
            with(binding) {
                weatherStateImage.setImageByWeatherCode(dailyWeather.weatherCode)
                tempMax.text = dailyWeather.temperatureMax
                tempMin.text = dailyWeather.temperatureMin
                textWeatherDescription.setDescriptionByWeatherCode(dailyWeather.weatherCode)
                forecastDateTv.text = "$dayOfWeek, $month ${date.dayOfMonth}"


                if (!dailyWeather.isExpanded) {
                    expandablePart.gone()

                    windValTv.text = dailyWeather.windSpeedMax
                    precipitationValTv.text = dailyWeather.precipitationSum
                    sunsetSunriseValTv.text = "${dailyWeather.sunrise}/${dailyWeather.sunset}"

                    hourlyForecastAdapter.updateHourlyForecastData(dailyWeather.hourlyWeather)
                    hourlyForecastRecycler.adapter = hourlyForecastAdapter
                } else {
                    expandablePart.show()
                }
            }
        }
    }
}

class DailyDiffCallback(
    private val newDailyList: List<DailyUi>,
    private val oldDailyList: MutableList<DailyUi>
) : BaseDiffCallback<DailyUi>(newDailyList, oldDailyList) {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        newDailyList[newItemPosition].time == oldDailyList[oldItemPosition].time

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        newDailyList[newItemPosition] == oldDailyList[oldItemPosition]
}

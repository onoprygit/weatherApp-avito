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
import java.time.LocalDateTime

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
        return DailyForecastViewHolder(binding, HourlyForecastAdapter())
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {

        holder.bind(dailyForecast[position])
    }

    override fun getItemCount() = dailyForecast.size

    inner class DailyForecastViewHolder(
        private val binding: ItemDayForecastBinding,
        private val adapter: HourlyForecastAdapter
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.hourlyForecastRecycler.adapter = adapter
        }

        fun bind(dailyWeather: DailyUi) {
            itemView.setOnClickListener {
                val currentExpandedState = dailyWeather.isExpanded
                dailyWeather.isExpanded = !currentExpandedState
                notifyItemChanged(absoluteAdapterPosition)
            }
            val date = LocalDate.parse(dailyWeather.time)
            val dayOfWeek: String =
                date.dayOfWeek.name
            val month: String = date.month.name
            with(binding) {
                weatherStateImage.setImageByWeatherCode(dailyWeather.weatherCode)
                tempMax.text = dailyWeather.temperatureMax
                tempMin.text = dailyWeather.temperatureMin
                textWeatherDescription.setDescriptionByWeatherCode(dailyWeather.weatherCode)
                forecastDateTv.text = "$dayOfWeek, $month ${date.dayOfMonth}"

                val sunrise = LocalDateTime.parse(dailyWeather.sunrise).toLocalTime()
                val sunset = LocalDateTime.parse(dailyWeather.sunset).toLocalTime()

                windValTv.text = dailyWeather.windSpeedMax
                precipitationValTv.text = dailyWeather.precipitationSum
                sunsetSunriseValTv.text = "${sunrise}/${sunset}"

                adapter.updateHourlyForecastData(dailyWeather.hourlyWeather)

                if (!dailyWeather.isExpanded) expandablePart.gone()
                else expandablePart.show()
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

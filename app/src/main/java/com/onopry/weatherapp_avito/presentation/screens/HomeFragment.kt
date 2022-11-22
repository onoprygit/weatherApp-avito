package com.onopry.weatherapp_avito.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import com.onopry.data.utils.debugLog
import com.onopry.domain.model.forecast.Locality
import com.onopry.domain.model.forecast.LocalitySearch
import com.onopry.weatherapp_avito.R
import com.onopry.weatherapp_avito.databinding.FragmentHomeBinding
import com.onopry.weatherapp_avito.presentation.adapters.CitySearchAdapter
import com.onopry.weatherapp_avito.presentation.adapters.DailyForecastAdapter
import com.onopry.weatherapp_avito.presentation.adapters.DailyListDecoration
import com.onopry.weatherapp_avito.presentation.adapters.HourlyForecastAdapter
import com.onopry.weatherapp_avito.presentation.adapters.HourlyListDecoration
import com.onopry.weatherapp_avito.presentation.uistate.ForecastState
import com.onopry.weatherapp_avito.presentation.uistate.LocalityState
import com.onopry.weatherapp_avito.presentation.uistate.PermissionState
import com.onopry.weatherapp_avito.presentation.uistate.SearchState
import com.onopry.weatherapp_avito.utils.DailyToUiConverter
import com.onopry.weatherapp_avito.utils.getDayOfWeekDayMonth
import com.onopry.weatherapp_avito.utils.setDescriptionByWeatherCode
import com.onopry.weatherapp_avito.utils.setImageByWeatherCode
import com.onopry.weatherapp_avito.utils.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var dividerDailyList: DividerItemDecoration
    private val dailyListDecoration = DailyListDecoration()
    private val hourlyListDecoration = HourlyListDecoration()

    private val geoLocationPermissionLauncher =
        registerForActivityResult(RequestPermission(), ::onGrantedGeoPermissionResult)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        debugLog("Checking permission")
        geoLocationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

        dividerDailyList = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        ResourcesCompat.getDrawable(resources, R.drawable.devider, null)?.let {
            dividerDailyList.setDrawable(it)
        }

        observeViewModelStates()

        binding.autoCompleteTv.doOnTextChanged { query, _, _, _ ->
            query?.let {
                viewModel.sendQuery(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.localityState.collect { localityState ->
                    when (localityState) {
                        is LocalityState.City -> binding.autoCompleteTv.setText("${localityState.locality.country}, ${localityState.locality.name}")
                        is LocalityState.Pending -> binding.autoCompleteTv.setText("Loading your location...")
                        is LocalityState.Error -> binding.autoCompleteTv.setText("Cannot identify name of your location")
                    }
                }
            }
        }

        binding.autoCompleteTv.setOnItemClickListener { parent, _, pos, _ ->
            val city = parent.getItemAtPosition(pos) as LocalitySearch
            debugLog("Send after click row ${city.name}, lat: ${city.lat}, lon: ${city.lon}")
            viewModel.sendLocalityState(
                LocalityState.City(
                    locality = Locality(
                        country = city.country,
                        name = city.name,
                        lon = city.lon,
                        lat = city.lat
                    ), isIpLocality = false
                )
            )
            binding.autoCompleteTv.clearFocus()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchState.collect {
                    debugLog(it.toString())
                    if (it is SearchState.Content)
                        binding.autoCompleteTv.setAdapter(
                            CitySearchAdapter(
                                requireContext(),
                                R.layout.item_search_response,
                                it.cities.toMutableList()
                            )
                        )
                }
            }
        }


        handleErrorStates()
    }

    private fun handleErrorStates() {
    }

    private fun observeViewModelStates() {
        observeForecastState()
        observeLocalityState()
    }

    private fun observeForecastState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.forecastState.collect { forecastState: ForecastState ->
                    when (forecastState) {
                        is ForecastState.Success -> {
                            bindCurrentForecastData(forecastState)
                        }
                    }
                }
            }
        }
    }

    private fun observeLocalityState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.localityState.collect { localityState ->
                    when (localityState) {
                        is LocalityState.City -> {
                            debugLog(localityState.locality.name.toString() + "is IP: " + localityState.isIpLocality.toString())
                        }
                    }
                }
            }
        }
    }

    private fun bindCurrentForecastData(forecastState: ForecastState.Success) {
        with(binding) {
            val units = forecastState.data.hourlyUnits
            val currentWeather = forecastState.data.currentWeather
            val currentDayWeather = forecastState.data.dailyWeather[0]

            currentWeatherImage.setImageByWeatherCode(currentWeather.weatherCode)
            currentWeatherDescTv.setDescriptionByWeatherCode(currentWeather.weatherCode)
            currentDate.text = LocalDateTime.parse(currentWeather.time).toLocalDate()
                .getDayOfWeekDayMonth(fullDay = true, fullMonth = false)

            currentWeatherMinMaxTempTv.text =
                "${currentDayWeather.temperatureMin}/${currentDayWeather.temperatureMax}"
            currentWeatherTempTv.text = currentWeather.temperature

            currentWeatherWind.infoImage.setImageResource(R.drawable.ic_wind_speed)
            currentWeatherWind.infoNameTv.text = "Wind"
            currentWeatherWind.infoValueTv.text = currentWeather.windSpeed

            currentWeatherTempFeels.infoImage.setImageResource(R.drawable.ic_temp_feels_like)
            currentWeatherTempFeels.infoNameTv.text = "Feels (min/max)"
            currentWeatherTempFeels.infoValueTv.text =
                "${currentDayWeather.apparentTemperatureMin}/${currentDayWeather.apparentTemperatureMax}"


            currentWeatherPrecipitation.infoImage.setImageResource(R.drawable.ic_precipitation)
            currentWeatherPrecipitation.infoNameTv.text = "Precipitation"
            currentWeatherPrecipitation.infoValueTv.text = "${currentDayWeather.precipitationSum}"

            val sunrise = LocalDateTime.parse(currentDayWeather.sunrise)
            val sunset = LocalDateTime.parse(currentDayWeather.sunset)

            currentWeatherSun.infoImage.setImageResource(R.drawable.ic_sun)
            currentWeatherSun.infoNameTv.text = "Sunrise - sunset"
            currentWeatherSun.infoValueTv.text =
                "${sunrise.hour}:${sunrise.minute} - ${sunset.hour}:${sunset.minute}"

            val hourlyForecastAdapter = HourlyForecastAdapter()
            hourlyForecastAdapter.updateHourlyForecastData(forecastState.data.dailyWeather[0].hourlyWeather)
            currentWeatherRecycler.adapter = hourlyForecastAdapter
            currentWeatherRecycler.addItemDecoration(hourlyListDecoration)

            val dailyForecastAdapter = DailyForecastAdapter()
            dailyForecastAdapter.updateDailyForecastData(DailyToUiConverter.convert(forecastState.data.dailyWeather))
            weeklyForecastRecycler.adapter = dailyForecastAdapter
            weeklyForecastRecycler.addItemDecoration(DailyListDecoration())
        }
    }

    private fun onGrantedGeoPermissionResult(granted: Boolean) {
        if (granted) {
            debugLog("Permission is granted")
            shortToast("Разрешенгие получено")
            onLocationPermissionGranted()
        } else {
            debugLog("Permission is not granted")
            askUserForOpeningAppSettings()
        }
    }

    @SuppressLint("MissingPermission")
    private fun onLocationPermissionGranted() {
        debugLog("send LocationState [Pending]")
        viewModel.sendLocalityState(LocalityState.Pending)

        val locationManager = LocationServices.getFusedLocationProviderClient(requireContext())
        locationManager.lastLocation.addOnSuccessListener { location ->
            location?.let { locationNotNull ->

                debugLog("sen LocationState [City]")
                viewModel.sendLocalityState(
                    LocalityState.City(
                        locality = Locality(
                            lat = locationNotNull.latitude.toFloat(),
                            lon = locationNotNull.longitude.toFloat()
                        ),
                        isIpLocality = false
                    )

                )
                debugLog("sen PermissionState [Granted]")
                viewModel.sendPermissionState(PermissionState.Granted)
            }
            debugLog("Fetching forecast")
        }
    }

    private fun askUserForOpeningAppSettings() {
        val appSettingIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )

        if (requireContext().packageManager.resolveActivity(
                appSettingIntent, PackageManager.MATCH_DEFAULT_ONLY
            ) == null
        ) {
            Toast.makeText(requireContext(), "Permission are denied forever", Toast.LENGTH_SHORT)
                .show()
        } else {
            viewModel.sendPermissionState(PermissionState.Denied)
            showPermissionDeniedDialog(appSettingIntent)
        }
    }

    private fun showPermissionDeniedDialog(appSettingIntent: Intent) {
        AlertDialog.Builder(requireContext())
            .setTitle("Разрешение отклонено")
            .setMessage(
                """
                        Вы отколнили разрешение навсегда.
                        Оно требуется для более точного прогноза. Вы всегда можете перейти в настройки и разрешить доступ к геолокации.                      
                        Перейти в настройки?
                    """.trimIndent()
            )
            .setPositiveButton("Да") { _, _ ->
                startActivity(appSettingIntent)
            }
            .setNegativeButton("Нет") { _, _ ->

            }
            .create()
            .show()
    }
}
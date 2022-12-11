package com.onopry.weatherapp_avito.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
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
import com.onopry.weatherapp_avito.presentation.uistate.ScreenState
import com.onopry.weatherapp_avito.presentation.uistate.SearchState
import com.onopry.weatherapp_avito.utils.DailyToUiConverter
import com.onopry.weatherapp_avito.utils.getDayOfWeekDayMonth
import com.onopry.weatherapp_avito.utils.gone
import com.onopry.weatherapp_avito.utils.setDescriptionByWeatherCode
import com.onopry.weatherapp_avito.utils.setImageByWeatherCode
import com.onopry.weatherapp_avito.utils.shortToast
import com.onopry.weatherapp_avito.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private val geoLocationPermissionLauncher =
        registerForActivityResult(RequestPermission(), ::onGrantedGeoPermissionResult)

    private val dailyListDecoration: DailyListDecoration by lazy { DailyListDecoration() }
    private val hourlyListDecoration: HourlyListDecoration by lazy { HourlyListDecoration() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        debugLog("Checking permission")
        geoLocationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

        setupForecast()
        setupLocality()
        setupSearch()
        handleScreenState()
    }

    private fun handleScreenState() {
        binding.refresh.setOnRefreshListener {
            viewModel.sendRefreshAction()
            binding.refresh.isRefreshing = false
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.collect { screenState ->
                    when (screenState) {
                        is ScreenState.Loading -> {
                            showLoading()
                        }
                        is ScreenState.ForecastError -> {
                            showError(screenState.msg)
                        }
                        is ScreenState.LocationError -> {
                            showError(screenState.msg)
                        }
                        is ScreenState.Content -> {
                            showContent()
                        }
                    }
                }
            }
        }
    }

    private fun showError(msg: String) {
        binding.contentContainer.gone()
        binding.errorBottomImage.show()
        binding.refresh.isRefreshing = false
        with(binding.statePart) {
//            progressBar.gone()
            errorMessageTv.show()
            tryAgainButton.show()
            errorImage.show()

            errorMessageTv.text = msg.ifBlank { getString(R.string.unexpected_error) }
            tryAgainButton.setOnClickListener { viewModel.sendRefreshState(true) }
        }
    }

    private fun showLoading() {
        binding.contentContainer.gone()
        binding.errorBottomImage.gone()
        binding.refresh.isRefreshing = true
        with(binding.statePart) {
//            progressBar.show()
            errorMessageTv.gone()
            tryAgainButton.gone()
            errorImage.gone()
        }
    }

    private fun showContent() {
        binding.contentContainer.show()
        binding.errorBottomImage.gone()
        binding.refresh.isRefreshing = false
        with(binding.statePart) {
//            progressBar.gone()
            errorMessageTv.gone()
            tryAgainButton.gone()
            errorImage.gone()
        }
    }

    private fun setupSearch() {
        binding.autoCompleteTv.doOnTextChanged { query, _, _, _ ->
            query?.let { viewModel.sendQuery(it) }
        }

        binding.autoCompleteTv.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) binding.autoCompleteTv.hint = ""
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.localityState.collect { localityState ->
                    when (localityState) {
                        is LocalityState.City -> {
                            if (localityState.locality.name != null) {
                                binding.autoCompleteTv.hint =
                                    "${localityState.locality.country}, ${localityState.locality.name}"
                            }
                        }
//                        is LocalityState.Pending -> binding.autoCompleteTv.setText("Loading your location...")
                        is LocalityState.Error -> binding.autoCompleteTv.hint =
                            "Cannot identify name of your location"
//                        is LocalityState.Error -> binding.autoCompleteTv.setText("Cannot identify name of your location")
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
    }


    private fun setupForecast() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.forecastState.collect { forecastState: ForecastState ->
                    when (forecastState) {
                        is ForecastState.Success -> {
                            showContent()
                            bindCurrentForecastData(forecastState)
                        }
                        is ForecastState.Loading ->
                            showLoading()
                    }
                }
            }
        }
    }

    private fun setupLocality() {
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
                requireContext().getString(
                    R.string.min_slash_max_values,
                    currentDayWeather.temperatureMin,
                    currentDayWeather.temperatureMax
                )
            currentWeatherTempTv.text = currentWeather.temperature

            currentWeatherWind.infoImage.setImageResource(R.drawable.ic_wind_speed)
            currentWeatherWind.infoNameTv.text =
                requireContext().getString(R.string.wind_block_title)
            currentWeatherWind.infoValueTv.text = currentWeather.windSpeed

            currentWeatherTempFeels.infoImage.setImageResource(R.drawable.ic_temp_feels_like)
            currentWeatherTempFeels.infoNameTv.text =
                requireContext().getString(R.string.feels_block_title)
            currentWeatherTempFeels.infoValueTv.text =
                requireContext().getString(
                    R.string.min_slash_max_values,
                    currentDayWeather.apparentTemperatureMin,
                    currentDayWeather.apparentTemperatureMax
                )


            currentWeatherPrecipitation.infoImage.setImageResource(R.drawable.ic_precipitation)
            currentWeatherPrecipitation.infoNameTv.text =
                requireContext().getString(R.string.precipitation_block_title)
            currentWeatherPrecipitation.infoValueTv.text = currentDayWeather.precipitationSum

            val sunrise = LocalDateTime.parse(currentDayWeather.sunrise)
            val sunset = LocalDateTime.parse(currentDayWeather.sunset)

            currentWeatherSun.infoImage.setImageResource(R.drawable.ic_sun)
            currentWeatherSun.infoNameTv.text = requireContext().getString(R.string.sun_block_title)
            currentWeatherSun.infoValueTv.text =
                requireContext().getString(
                    R.string.sun_time,
                    sunrise.hour,
                    sunrise.minute,
                    sunset.hour,
                    sunset.minute
                )

            setupRecyclers(forecastState)
        }
    }

    private fun setupRecyclers(forecastState: ForecastState.Success) {

        val hourlyForecastAdapter = HourlyForecastAdapter()
        hourlyForecastAdapter.updateHourlyForecastData(forecastState.data.dailyWeather[0].hourlyWeather)
        binding.currentWeatherRecycler.adapter = hourlyForecastAdapter
        binding.currentWeatherRecycler.addItemDecoration(hourlyListDecoration)

        val dailyForecastAdapter = DailyForecastAdapter()
        dailyForecastAdapter.updateDailyForecastData(DailyToUiConverter.convert(forecastState.data.dailyWeather))
        binding.weeklyForecastRecycler.adapter = dailyForecastAdapter
        binding.weeklyForecastRecycler.addItemDecoration(dailyListDecoration)
    }


    private fun onGrantedGeoPermissionResult(granted: Boolean) {
        if (granted) {
            debugLog("Permission is granted")
            shortToast(R.string.permission_granted_message)
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
                appSettingIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Snackbar.make(binding.root, getString(R.string.permission_denied_forever_message), Snackbar.LENGTH_SHORT).show()
        } else {
            viewModel.sendPermissionState(PermissionState.Denied)
            showPermissionDeniedDialog(appSettingIntent)
        }
    }

    private fun showPermissionDeniedDialog(appSettingIntent: Intent) {

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.geo_denied_alert_title))
            .setMessage(getString(R.string.geo_denied_alert_content).trimIndent())
            .setPositiveButton(getString(R.string.geo_denied_alert_negative_btn)) { _, _ ->
                startActivity(appSettingIntent)
            }
            .create()

        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE,
            getString(R.string.geo_denied_alert_negative_btn)
        ) { _, _ -> alertDialog.dismiss() }

    }
}
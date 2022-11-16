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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.LocationServices
import com.onopry.data.utils.debugLog
import com.onopry.domain.model.forecast.Locality
import com.onopry.weatherapp_avito.R
import com.onopry.weatherapp_avito.databinding.FragmentHomeBinding
import com.onopry.weatherapp_avito.presentation.uistate.ForecastState
import com.onopry.weatherapp_avito.presentation.uistate.LocalityState
import com.onopry.weatherapp_avito.presentation.uistate.PermissionState
import com.onopry.weatherapp_avito.utils.setImageByWeatherCode
import com.onopry.weatherapp_avito.utils.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private val geoLocationPermissionLauncher =
        registerForActivityResult(RequestPermission(), ::onGrantedGeoPermissionResult)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        debugLog("Checking permission")
        geoLocationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

        observeViewModelStates()
        handleErrorStates()
    }

    private fun handleErrorStates() {
    }

    private fun observeViewModelStates(){
        observeForecastState()
        observeLocalityState()
    }

    private fun observeForecastState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.forecastState.collect { forecastState: ForecastState ->
                    when (forecastState) {
                        is ForecastState.Success -> {
                            bindCurrForecastData(forecastState)
                        }
                        //                        ForecastState.Empty -> TODO()
                        //                        is ForecastState.Error -> TODO()
                        //                        ForecastState.Loading -> TODO()
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
                            debugLog(localityState.locality.name.toString() + "is IP: "+ localityState.isIpLocality.toString())
                        }
                    }
                }
            }
        }
    }

    private fun bindCurrForecastData(forecastState: ForecastState.Success) {
        with(binding) {
            weatherStateImage
                .setImageByWeatherCode(forecastState.data.currentWeather.weatherCode)

            tempIndicatorTv.text =
                forecastState.data.currentWeather.temperature.toString()

            windDirectionsVal.text =
                forecastState.data.currentWeather.windDirection.toString()

            windSpeedVal.text =
                forecastState.data.currentWeather.windSpeed.toString()
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
            //            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            //
            //            }
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
            viewModel.fetchForecast()

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
            shortToast("Geo permission is denied now")
//            showPermissionDeniedDialog(appSettingIntent)
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
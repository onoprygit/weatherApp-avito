package com.onopry.weatherapp_avito.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.LocationServices
import com.onopry.data.utils.debugLog
import com.onopry.weatherapp_avito.R
import com.onopry.weatherapp_avito.databinding.FragmentHomeBinding
import com.onopry.weatherapp_avito.presentation.states.LocationState
import com.onopry.weatherapp_avito.utils.shortToast
import kotlin.system.exitProcess

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

//        binding.textview.text = "asdasasdasd"
//        binding.temperatureIndicatorTv.text = "\uf02e"
        checkAppPermissions()



    }

    @SuppressLint("MissingPermission")
    private fun getSystemLocation(){
        val locationManager = LocationServices.getFusedLocationProviderClient(requireContext())
        viewModel.setLocation(LocationState.Pending)
        locationManager.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let { locationNotNull ->
                viewModel.setLocation(
                    LocationState.Granted(
                        latitude = locationNotNull.latitude.toFloat(),
                        longitude = locationNotNull.longitude.toFloat(),
                        isNative = true
                    )
                )
            }
        }
    }

    // Better to use Result API, but now it is what it is.
    // todo: Waiting for improvement #1 - Result API for permissions
    private fun checkAppPermissions() {
        if (checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            //todo добавить пасхалку если успею
            getSystemLocation()

        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                COERCE_LOCATION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == COERCE_LOCATION_REQUEST_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                shortToast("Разрешенгие получено")
                getSystemLocation()
            }
            else{
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)){
                    askUserForOpeningAppSettings()
                }
            }
        }
    }

    private fun askUserForOpeningAppSettings(){
        val appSettingIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )

        if (requireContext().packageManager.resolveActivity(appSettingIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Toast.makeText(requireContext(), "Permission are denied forever", Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle("Разрешение отклонено")
                .setMessage("""
                    Вы отколнили разрешение навсегда.
                    Вы можете изменить свое решение в настройках приложения.
                    Перейти в настройки?
                """.trimIndent())
                .setPositiveButton("Перейти") { _, _ ->
                    startActivity(appSettingIntent)
                }
                .setNegativeButton("Закрыть приложение") { _, _ ->
                    requireActivity().finish()
                    exitProcess(0)
                }
                .create()
                .show()
        }
    }

    companion object{
        const val COERCE_LOCATION_REQUEST_CODE = 999
    }
}
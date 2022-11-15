package com.onopry.weatherapp_avito.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onopry.data.utils.debugLog
import com.onopry.domain.usecase.GetForecastUseCase
import com.onopry.domain.utils.ApiError
import com.onopry.domain.utils.ApiException
import com.onopry.domain.utils.ApiSuccess
import com.onopry.weatherapp_avito.presentation.uistate.ForecastState
import com.onopry.weatherapp_avito.presentation.uistate.LocationState
import com.onopry.weatherapp_avito.presentation.uistate.PermissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.security.Permission
import javax.inject.Inject



/*
* Возможные объекты и сосотяния:
* Состояние экрана
* Состояние прогноза
* Состояние локации
*
* */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {

    private val forecastMutableStateFlow = MutableStateFlow<ForecastState>(ForecastState.Empty)
    val forecastStateFlow: StateFlow<ForecastState> = forecastMutableStateFlow

    private val locationMutableStateFlow = MutableStateFlow<LocationState>(LocationState.Empty)
    val locationStateFlow: StateFlow<LocationState> = locationMutableStateFlow

    private val permissionMutableStateFlow = MutableStateFlow<PermissionState>(PermissionState.Empty)
    val permissionStateFlow: StateFlow<PermissionState> = permissionMutableStateFlow



    init {
//        fetchForecast()

        forecastMutableStateFlow.onEach {
            debugLog("Forecast state is ${it.javaClass.simpleName}")
        }.launchIn(viewModelScope)
    }

    fun fetchForecast(
        startDate: String = "2022-11-15",
        endDate: String = "2022-11-21"
    ) {
        debugLog("fetching forecast...")
        viewModelScope.launch {
            val state = locationMutableStateFlow.value
            debugLog("")
            var latitude: Float
            var longitude: Float
            if (state is LocationState.Granted){
                debugLog("forecast state is Granted")
                forecastMutableStateFlow.emit(ForecastState.Loading)
                getForecastUseCase(state.latitude.toString(), state.longitude.toString(), startDate, endDate)
                    .collect { result ->
                        when (result) {
                            is ApiSuccess -> ForecastState.Success(data = result.data)
                            is ApiError -> ForecastState.Error(message = "Error: ${result.message} with code: ${result.code}")
                            is ApiException -> ForecastState.Error(message = "Oops... An unexpected error occurred")
                        }
                    }

            }
        }
    }

    fun sendLocationState(state: LocationState) {
        viewModelScope.launch {
            debugLog("SetLocation from fragment, STATE = ${state.javaClass.simpleName}")
            locationMutableStateFlow.emit(state)
        }
    }

    private fun getCityName(latitude: Float, longitude: Float) {
    }
}
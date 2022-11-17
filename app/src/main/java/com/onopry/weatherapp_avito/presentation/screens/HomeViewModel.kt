package com.onopry.weatherapp_avito.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onopry.data.utils.debugLog
import com.onopry.domain.model.forecast.Locality
import com.onopry.domain.usecase.GetForecastUseCase
import com.onopry.domain.usecase.GetLocationByIpUseCase
import com.onopry.domain.usecase.GetLocationNameUseCase
import com.onopry.domain.utils.ApiError
import com.onopry.domain.utils.ApiException
import com.onopry.domain.utils.ApiSuccess
import com.onopry.weatherapp_avito.presentation.model.ForecastPeriod
import com.onopry.weatherapp_avito.presentation.uistate.ForecastState
import com.onopry.weatherapp_avito.presentation.uistate.LocalityState
import com.onopry.weatherapp_avito.presentation.uistate.PermissionState
import com.onopry.weatherapp_avito.presentation.uistate.ScreenState
import com.onopry.weatherapp_avito.utils.isNotNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase,
    private val getLocationNameUseCase: GetLocationNameUseCase,
    private val getLocationByIpUseCase: GetLocationByIpUseCase
) : ViewModel() {

    private val forecastPeriod = ForecastPeriod()

    private val screenStateMutableFlow = MutableStateFlow<ScreenState>(ScreenState.Empty)
    val screenState: StateFlow<ScreenState> = screenStateMutableFlow

    private val localityMutableStateFlow = MutableStateFlow<LocalityState>(LocalityState.Empty)
    val localityState: StateFlow<LocalityState> = localityMutableStateFlow

    private val forecastMutableStateFlow = MutableStateFlow<ForecastState>(ForecastState.Empty)
    val forecastState: StateFlow<ForecastState> = forecastMutableStateFlow

    private val permissionMutableStateFlow =
        MutableStateFlow<PermissionState>(PermissionState.Empty)
    val permissionState: StateFlow<PermissionState> = permissionMutableStateFlow


    init {
        localityMutableStateFlow.onEach { localityState ->
            debugLog("Locality state is [${localityState.javaClass.simpleName}]")
            when (localityState) {
                is LocalityState.City -> {
                    if (localityState.locality.name != null){
                        fetchForecastOnly(localityState)
                    } else {
                        fetchForecastWithLocalityName(localityState)
                    }
                }

                is LocalityState.None -> fetchIpLocality()
            }
        }.launchIn(viewModelScope)

        permissionMutableStateFlow.onEach { permissionState ->
            debugLog("Permission state is: [${permissionState.javaClass.simpleName}]")
            when (permissionState) {
                is PermissionState.Denied -> localityMutableStateFlow.emit(LocalityState.None)
            }
        }.launchIn(viewModelScope)

        forecastMutableStateFlow.onEach { forecastState ->
            debugLog("Forecast state is: [${forecastState.javaClass.simpleName}]")
            when (forecastState) {
                is ForecastState.Error -> screenStateMutableFlow.emit(
                    ScreenState.ForecastError(msg = forecastState.message)
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun fetchIpLocality() {
        viewModelScope.launch {
            getLocationByIpUseCase().collect { result ->
                when (result) {
                    is ApiSuccess -> {
                        localityMutableStateFlow.emit(
                            LocalityState.City(
                                locality = result.data,
                                isIpLocality = true
                            )
                        )
                    }
                    is ApiError -> {
                        screenStateMutableFlow.emit(
                            ScreenState.LocationError(
                                msg = """Oops... Identifying location error with code ${result.code}:
                                            |${result.message}
                                        """.trimMargin()
                            )
                        )
                    }
                    is ApiException -> {
                        screenStateMutableFlow.emit(
                            ScreenState.LocationError(
                                msg = """Oops... Identifying location exception:
                                    |${result.message}"""".trimMargin()
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun fetchBaseLocality() {
        debugLog("fetchBaseLocality()")
        val localityState = localityMutableStateFlow.value
        if (localityState is LocalityState.City && !localityState.isIpLocality) {
            getLocationNameUseCase(
                lat = localityState.locality.lat,
                lon = localityState.locality.lon
            ).collect { localityResult ->
                when (localityResult) {
                    is ApiSuccess -> {
                        localityMutableStateFlow.emit(
                            LocalityState.City(
                                locality = localityResult.data,
                                isIpLocality = false
                            )
                        )
                    }
                    is ApiError -> {
                        if (localityResult.code == 503)
                            fetchIpLocality()
                        else {
                            screenStateMutableFlow.emit(
                                ScreenState.LocationError(
                                    msg = """Oops... Identifying location error with code ${localityResult.code}:
                                            |${localityResult.message}
                                        """.trimMargin()
                                )
                            )
                        }
                    }
                    is ApiException -> {
                        screenStateMutableFlow.emit(
                            ScreenState.LocationError(
                                msg = """Oops... Identifying location exception:
                                    |${localityResult.message}"""".trimMargin()
                            )
                        )
                    }

                }
            }
        }
    }


    fun fetchForecast() {
        debugLog("fetchForecast()")
        viewModelScope.launch {
            forecastMutableStateFlow.emit(ForecastState.Loading)
            val localityState = localityMutableStateFlow.value
            val permissionState = permissionState.value
            if (localityState is LocalityState.City && permissionState is PermissionState.Granted) {
                if (isLocalityHasFullData(localityState.locality)) {
                    fetchForecastOnly(localityState)
                } else {
                    fetchForecastWithLocalityName(localityState)
                }
            }
            fetchForecastWithLocalityByIp()
        }

        /*        debugLog("fetching forecast...")
                viewModelScope.launch {
                    val state = localityMutableStateFlow.value
                    var latitude: Float
                    var longitude: Float
                    if (state is LocalityState.City) {
                        debugLog("forecast state is Granted")
                        forecastMutableStateFlow.emit(ForecastState.Loading)
                        getForecastUseCase(
                            state.locality.lat.toString(),
                            state.locality.lon.toString(),
                            forecastPeriod.startDate.toString(),
                            forecastPeriod.endDate.toString()
                        ).collect { result ->
                            when (result) {
                                is ApiSuccess -> forecastMutableStateFlow.emit(
                                    ForecastState.Success(data = result.data)
                                )
                                is ApiError -> forecastMutableStateFlow.emit(
                                    ForecastState.Error(message = "Error: ${result.message} with code: ${result.code}")
                                )
                                is ApiException -> forecastMutableStateFlow.emit(
                                    ForecastState.Error(
                                        message = "Oops... An unexpected error occurred"
                                    )
                                )
                            }
                        }
                    }
                }*/
    }

    private suspend fun fetchForecastWithLocalityByIp() {
        debugLog("fetchForecastWithLocalityByIp()")
        val localityByIp = withContext(Dispatchers.IO){
            getLocationByIpUseCase()
        }
        localityByIp.onEach { localityResult ->
            when(localityResult) {
                is ApiSuccess -> {
                    val city = LocalityState.City(
                        locality = localityResult.data,
                        isIpLocality = true
                    )
                    localityMutableStateFlow.emit(
                        city
                    )
                }
            }
        }
    }

    private suspend fun fetchForecastWithLocalityName(localityState: LocalityState.City) {
        fetchBaseLocality()
        fetchForecastOnly(localityState)
    }

    private suspend fun fetchForecastOnly(localityState: LocalityState.City) {
        getForecastUseCase(
            lat = localityState.locality.lat.toString(),
            lon = localityState.locality.lon.toString(),
            startDate = forecastPeriod.startDate.toString(),
            endDate = forecastPeriod.endDate.toString()
        ).collect { forecastResult ->
            when (forecastResult) {
                is ApiSuccess -> {
                    forecastMutableStateFlow.emit(
                        ForecastState.Success(data = forecastResult.data)
                    )
                }
                is ApiError -> {
                    forecastMutableStateFlow.emit(
                        ForecastState.Error(
                            message = """Oops... Cannot get forecast from server with code ${forecastResult.code}:
                                            ${forecastResult.message}. Check internet connection and try again
                                        """.trimIndent()
                        )
                    )
                }
                is ApiException -> {
                    forecastMutableStateFlow.emit(
                        ForecastState.Error(
                            message = """Oops... Unexpected error occurred:
                                            ${forecastResult.message}. Check internet connection and try again
                                        """.trimIndent()
                        )
                    )
                }
            }
        }
    }


    fun sendLocalityState(state: LocalityState) {
        viewModelScope.launch {
            debugLog("Changing LocalityState in SetLocation(), STATE = ${state.javaClass.simpleName}")
            localityMutableStateFlow.emit(state)
        }
    }

    fun sendPermissionState(state: PermissionState) {
        debugLog("Changing PermissionState in sendPermissionState(), STATE = ${state.javaClass.simpleName}")
        viewModelScope.launch {
            permissionMutableStateFlow.emit(state)
        }
    }

    private fun isLocalityHasFullData(locality: Locality) =
        locality.name.isNotNull() && locality.country.isNotNull()


    private fun getCityName(latitude: Float, longitude: Float) {
    }
}
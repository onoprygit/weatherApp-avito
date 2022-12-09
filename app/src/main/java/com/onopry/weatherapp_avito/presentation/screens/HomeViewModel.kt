package com.onopry.weatherapp_avito.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onopry.data.utils.debugLog
import com.onopry.domain.model.forecast.Locality
import com.onopry.domain.usecase.GetForecastUseCase
import com.onopry.domain.usecase.GetLocationByIpUseCase
import com.onopry.domain.usecase.GetLocationNameUseCase
import com.onopry.domain.usecase.SearchCityByQueryUseCase
import com.onopry.domain.utils.ApiError
import com.onopry.domain.utils.ApiException
import com.onopry.domain.utils.ApiSuccess
import com.onopry.weatherapp_avito.presentation.model.ForecastPeriod
import com.onopry.weatherapp_avito.presentation.uistate.ForecastState
import com.onopry.weatherapp_avito.presentation.uistate.LocalityState
import com.onopry.weatherapp_avito.presentation.uistate.PermissionState
import com.onopry.weatherapp_avito.presentation.uistate.ScreenState
import com.onopry.weatherapp_avito.presentation.uistate.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase,
    private val getLocationNameUseCase: GetLocationNameUseCase,
    private val getLocationByIpUseCase: GetLocationByIpUseCase,
    private val searchCityByQueryUseCase: SearchCityByQueryUseCase
) : ViewModel() {
    private val forecastPeriod = ForecastPeriod()
    private var entryPreviousSearch = ""
    private var searchQueryJob: Job? = null

    private val refreshMutableFlow = MutableStateFlow(false)
    val refreshState: StateFlow<Boolean> = refreshMutableFlow

    private val screenStateMutableFlow = MutableStateFlow<ScreenState>(ScreenState.Empty)
    val screenState: StateFlow<ScreenState> = screenStateMutableFlow

    private val localityMutableStateFlow = MutableStateFlow<LocalityState>(LocalityState.Empty)
    val localityState: StateFlow<LocalityState> = localityMutableStateFlow

    private val forecastMutableStateFlow = MutableStateFlow<ForecastState>(ForecastState.Empty)
    val forecastState: StateFlow<ForecastState> = forecastMutableStateFlow

    private val permissionMutableStateFlow =
        MutableStateFlow<PermissionState>(PermissionState.Empty)

    private val searchStateMutableStateFlow = MutableStateFlow<SearchState>(SearchState.Empty)
    val searchState: StateFlow<SearchState> = searchStateMutableStateFlow

    private val entrySearch = MutableStateFlow("")
    private val searchRequest = MutableStateFlow("")

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

    fun sendRefreshState(state: Boolean) {
        viewModelScope.launch { refreshMutableFlow.emit(state) }
    }

    fun sendQuery(query: CharSequence) {
        viewModelScope.launch {
            val prevWithoutSpaces = entryPreviousSearch.replace("\\s".toRegex(), "")
            val currWithoutSpaces = query.replace("\\s".toRegex(), "")
            if (prevWithoutSpaces != currWithoutSpaces || query != currWithoutSpaces || query.isNotBlank())
                entrySearch.emit(query.toString())
        }
    }

    private fun loadForecast() {
        viewModelScope.launch {
            screenStateMutableFlow.emit(ScreenState.Loading)
            forecastMutableStateFlow.emit(ForecastState.Loading)
            val locality = localityState.value
            val permissionState = permissionMutableStateFlow.value

            if (permissionState is PermissionState.Granted) {
                when (locality) {
                    is LocalityState.City -> {
                        getUserLocation(locality)
                        getForecastByLocation(locality)
                    }

                }
            } else {
                when (locality) {
                    is LocalityState.Empty -> {
                        getLocationByIpUseCase().collect { localityResult ->
                            when (localityResult) {
                                is ApiSuccess -> localityMutableStateFlow.emit(
                                    LocalityState.City(
                                        locality = localityResult.data,
                                        isIpLocality = true
                                    )
                                )
                                is ApiError -> localityMutableStateFlow.emit(LocalityState.Error(msg = localityResult.message))
                                is ApiException -> localityMutableStateFlow.emit(
                                    LocalityState.Error(
                                        msg = localityResult.message
                                    )
                                )
                            }
                        }
                    }
                    is LocalityState.City -> {
                        getUserLocation(locality)
                        getForecastByLocation(locality)
                    }

                }
            }
        }
    }

    private fun refresh(isRefreshing: Boolean) {
        if (isRefreshing)
            loadForecast()
    }

    fun sendRefreshAction(){
        viewModelScope.launch {
            refreshMutableFlow.emit(true)
        }
    }

    private fun getForecastByLocation(locality: LocalityState.City) {
        getForecastUseCase(
            lat = locality.locality.lat,
            lon = locality.locality.lon,
            forecastPeriod.startDate.toString(),
            forecastPeriod.endDate.toString()
        ).onEach { forecast ->
            when (forecast) {
                is ApiSuccess -> forecastMutableStateFlow.emit(ForecastState.Success(forecast.data))
                is ApiError -> forecastMutableStateFlow.emit(ForecastState.Error(message = forecast.message))
                is ApiException -> forecastMutableStateFlow.emit(ForecastState.Error(message = forecast.message))
            }
            refreshMutableFlow.emit(false)
        }.launchIn(viewModelScope)
    }

    private fun getUserLocation(userLocality: LocalityState.City) {
        if (!isLocalityHasFullData(userLocality.locality)) {
            getLocationNameUseCase(userLocality.locality.lat, userLocality.locality.lon)
                .onEach { locality ->
                    when (locality) {
                        is ApiSuccess -> localityMutableStateFlow.emit(
                            LocalityState.City(
                                locality = locality.data,
                                isIpLocality = false
                            )
                        )
                        is ApiError -> localityMutableStateFlow.emit(LocalityState.Error(msg = locality.message))
                        is ApiException -> localityMutableStateFlow.emit(LocalityState.Error(msg = locality.message))
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun isLocalityHasFullData(locality: Locality) =
        !(locality.name.isNullOrEmpty() && locality.country.isNullOrEmpty())

    private fun search(query: String) {
        searchQueryJob?.cancel()
        searchQueryJob = viewModelScope.launch {
            searchStateMutableStateFlow.emit(SearchState.Loading)
            searchCityByQueryUseCase(query).collect { cities ->
                when (cities) {
                    is ApiError -> searchStateMutableStateFlow.emit(SearchState.Error(cities.message))
                    is ApiException -> searchStateMutableStateFlow.emit(SearchState.Error(cities.message))
                    is ApiSuccess -> {
                        if (cities.data.isEmpty())
                            searchStateMutableStateFlow.emit(SearchState.Empty)
                        else {
                            searchStateMutableStateFlow.emit(SearchState.Content(cities = cities.data))
                        }
                    }
                }
            }
            searchQueryJob = null
        }
    }


    init {
        refreshState
            .onEach { isRefreshing ->
                debugLog("Refresh state is [$isRefreshing]")
                refresh(isRefreshing)
            }.launchIn(viewModelScope)

        localityMutableStateFlow
            .onEach { localityState ->
                debugLog("Locality state is [${localityState.javaClass.simpleName}]")
                when (localityState) {
                    is LocalityState.City -> loadForecast()
                    is LocalityState.Pending -> screenStateMutableFlow.emit(ScreenState.Loading)
                    is LocalityState.Error -> screenStateMutableFlow.emit(
                        ScreenState.LocationError(
                            msg = localityState.msg
                        )
                    )
                }
            }.launchIn(viewModelScope)

        permissionMutableStateFlow
            .onEach { permissionState ->
                debugLog("Permission state is: [${permissionState.javaClass.simpleName}]")
                if (permissionState is PermissionState.Denied) sendLocalityState(LocalityState.None)
            }.launchIn(viewModelScope)

        forecastMutableStateFlow
            .onEach { forecastState ->
                debugLog("Forecast state is: [${forecastState.javaClass.simpleName}]")
                when (forecastState) {
                    is ForecastState.Error -> screenStateMutableFlow.emit(
                        ScreenState.ForecastError(msg = forecastState.message)
                    )
                    is ForecastState.Loading -> screenStateMutableFlow.emit(ScreenState.Loading)
                }
            }.launchIn(viewModelScope)

        entrySearch
            .onEach { query ->
                if (query == "")
                    searchQueryJob?.cancel()
            }.debounce(500)
            .onEach {
                if (it.isNotEmpty()){
                    searchRequest.emit(it)
                }
            }
            .launchIn(viewModelScope)

        searchRequest
            .filter { it != "" }
            .onEach { search(it) }
            .launchIn(viewModelScope)
    }
}
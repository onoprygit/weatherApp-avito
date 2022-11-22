package com.onopry.weatherapp_avito.presentation.uistate

import com.onopry.domain.model.forecast.Locality
import com.onopry.domain.model.forecast.LocalitySearch

sealed class SearchState(){
    object Empty: SearchState()
    object Loading: SearchState()
    class Error(val msg: String): SearchState()
    class Content(val cities: List<LocalitySearch>): SearchState()
}

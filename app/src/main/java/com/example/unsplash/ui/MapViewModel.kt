package com.example.unsplash.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

var latitude = 0.0
var longitude = 0.0
var description = ""
class MapViewModel : ViewModel() {



    //Список достопремичательностей
    private val markersList = listOf(latitude, longitude, description)


    private val _markersFlow = MutableStateFlow(markersList)
    val markersFlow = _markersFlow.asStateFlow()
}
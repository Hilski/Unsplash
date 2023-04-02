package com.example.unsplash.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MapViewModel : ViewModel() {

    //Список достопремичательностей
    private val markersList = listOf(
        listOf(55.75175, 37.61744, "Московский Кремль (музей-заповедник)"),
        listOf(55.75202, 37.61407, "Александровский сад (Парк)"),
        listOf(55.75298, 37.58851, "Сладкий музей (Детский музей)"),
        listOf(55.75830, 37.70593, "Лефортовские бани (Сауна)")
    )

    private val _markersFlow = MutableStateFlow(markersList)
    val markersFlow = _markersFlow.asStateFlow()
}
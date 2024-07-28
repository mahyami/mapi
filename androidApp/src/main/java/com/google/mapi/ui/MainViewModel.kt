package com.google.mapi.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mapi.data.MapsApiService
import com.google.mapi.business.ParseCSVApplicationService
import com.google.mapi.domain.PlacesRepository
import kotlinx.coroutines.launch


class MainViewModel(
    private val parseCSVApplicationService: ParseCSVApplicationService = ParseCSVApplicationService(),
    private val placesRepository: PlacesRepository = PlacesRepository()
) : ViewModel() {


    fun getPlacesDetails(context: Context) {
        viewModelScope.launch {
            parseCSVApplicationService.getLocations(
                context = context,
                fileName = "food.csv"
            )
                .take(5) // TODO:: comment
                .mapNotNull { location ->
                    extractFtIdFromUrl(location.url)
                }.map { ftId ->
                    placesRepository.getPlaceDetails(ftId)
                }
        }
    }

    private fun extractFtIdFromUrl(url: String): String? {
        val sequence = "!4m2!3m1!1s"
        val index = url.indexOf(sequence)

        return if (index != -1) {
            val startIndex = index + sequence.length
            val endIndex = url.indexOf("/", startIndex).takeIf { it != -1 } ?: url.length
            url.substring(startIndex, endIndex)
        } else {
            null
        }
    }
}
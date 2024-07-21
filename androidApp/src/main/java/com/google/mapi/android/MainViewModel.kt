package com.google.mapi.android

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.MapsApiService
import com.google.mapi.business.ParseCSVApplicationService
import kotlinx.coroutines.launch


class MainViewModel(
    private val mapsApiService: MapsApiService = MapsApiService(),
    private val parseCSVApplicationService: ParseCSVApplicationService = ParseCSVApplicationService()
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
                    mapsApiService.getPlaceDetails(ftId)
                        .onSuccess {
                            Log.d("PLACE:: ", it)
                        }
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
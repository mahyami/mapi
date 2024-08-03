package com.google.mapi.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.mapi.business.ParseCSVApplicationService
import com.google.mapi.data.gemini.GeminiService
import com.google.mapi.domain.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val parseCSVApplicationService: ParseCSVApplicationService,
    private val placesRepository: PlacesRepository,
    private val geminiService: GeminiService,
) : ViewModel() {


    fun getPlacesDetails(context: Context) {
        viewModelScope.launch {
            parseCSVApplicationService.getLocations(
                context = context,
                fileName = "food.csv"
            )
                .take(10) // TODO:: comment
                .mapNotNull { location ->
                    extractFtIdFromUrl(location.url)
                }.map { ftId ->
                    placesRepository.getPlaceDetails(ftId)
                }
                .let {
                    geminiService.sendMessage().let { response ->
                        response.candidates.map {
                            it.content.parts.map {
                                Log.d("MAHYA:: ", "Response candidate: ${it.asTextOrNull()}\n")
                            }
                        }
                        Log.d("MAHYA:: ", "Response text: ${response.text}\n")
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
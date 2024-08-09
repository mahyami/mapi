package com.google.mapi.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mapi.business.AuthenticationService
import com.google.mapi.business.ParseCSVApplicationService
import com.google.mapi.data.gemini.GeminiService
import com.google.mapi.domain.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val parseCSVApplicationService: ParseCSVApplicationService,
    private val placesRepository: PlacesRepository,
    private val geminiService: GeminiService,
    private val authenticationService: AuthenticationService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlacesUiState>(
        PlacesUiState.Sync.Initial
    )
    val uiState: StateFlow<PlacesUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update {
                PlacesUiState.Sync.Loading
            }
            placesRepository.getPlacesCount().let { count ->
                if (count != 0) {
                    _uiState.update {
                        PlacesUiState.Gemini.PlacesRecommendation(emptyList())
                    }
                }
            }
        }
    }

    private fun getPlacesDetails(context: Context) {
        viewModelScope.launch {
            parseCSVApplicationService.getLocations(
                context = context,
            ).mapNotNull { location ->
                extractFtIdFromUrl(location.url)
            }.map { ftId ->
                placesRepository.getPlaceDetails(ftId)
            }
        }
    }

    fun handleGoogleCallback(context: Context, uri: Uri) {
        authenticationService.handleCallback(context, uri)
            .also {
                getPlacesDetails(context)
            }
    }

    fun onSubmitButtonClicked(prompt: String) {
        viewModelScope.launch {
            _uiState.update {
                PlacesUiState.Gemini.Loading
            }
            geminiService.sendMessage(prompt).let { response ->
                response.text?.let {
                    parseGeminiResultToUiModel(it)
                }?.let { places ->
                    _uiState.update {
                        PlacesUiState.Gemini.PlacesRecommendation(places = places)
                    }
                }
            }
        }
    }

    fun onSyncButtonClicked() {
        val redirectUri = "https://ipiyush.com/mapi/"
        val takoutApi = "https://www.googleapis.com/auth/dataportability.saved.collections"
        val url = "https://accounts.google.com/o/oauth2/auth?response_type=code" +
                "&client_id=1007629705241-20m5rskcp6iqlrrfthrhs5h05pur5oan.apps.googleusercontent.com" +
                "&redirect_uri=$redirectUri" +
                "&scope=$takoutApi" +
                "&state=eJmhKiMS3CX0bbO1aTwaTzM07cULhG" +
                "&access_type=offline"

        _uiEvent.update {
            UiEvent.OpenBrowser(url)
        }
    }

    fun onOpenMapsClicked(url: String) {
        _uiEvent.update {
            UiEvent.OpenBrowser(url)
        }
    }

    private val _uiEvent = MutableStateFlow<UiEvent>(UiEvent.Default)
    val uiEvent: StateFlow<UiEvent> = _uiEvent

    sealed interface UiEvent {
        data class OpenBrowser(val url: String) : UiEvent
        data object Default : UiEvent
    }

    private fun parseGeminiResultToUiModel(response: String): List<PlaceUiModel> {
        val placeUiModels = mutableListOf<PlaceUiModel>()
        val jsonObject = JSONObject(response)
        val jsonArray = jsonObject.getJSONArray("gemini_result")

        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            val name = item.getString("name")
            val url = item.getString("url")
            placeUiModels.add(PlaceUiModel(name, url))
        }

        return placeUiModels
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
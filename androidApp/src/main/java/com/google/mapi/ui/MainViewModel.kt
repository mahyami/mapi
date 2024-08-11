package com.google.mapi.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mapi.business.AuthenticationService
import com.google.mapi.business.GetPlacesIdsApplicationService
import com.google.mapi.business.TakeoutSavedCollectionsService
import com.google.mapi.data.gemini.GeminiService
import com.google.mapi.domain.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPlacesIds: GetPlacesIdsApplicationService,
    private val placesRepository: PlacesRepository,
    private val geminiService: GeminiService,
    private val authenticationService: AuthenticationService,
    private val takeoutSavedCollectionsService: TakeoutSavedCollectionsService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlacesUiState>(
        PlacesUiState.Sync.Initial
    )
    val uiState: StateFlow<PlacesUiState> = _uiState

    init {
        viewModelScope.launch {
            placesRepository.getPlacesCount().let { count ->
                if (count != 0) {
                    _uiState.update {
                        PlacesUiState.Gemini.PlacesRecommendation(emptyList())
                    }
                }
            }
        }
    }

    fun onReturnFromOAuth(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.update {
                PlacesUiState.Sync.Loading
            }
            authenticationService.authenticate(uri) { accessToken ->
                takeoutSavedCollectionsService.getTakeoutData(
                    context,
                    accessToken
                ) { takeoutReceived ->
                    onTakeoutCsvDataReceived(context = context, isSuccess = takeoutReceived)
                }
            }
        }
    }

    private fun onTakeoutCsvDataReceived(context: Context, isSuccess: Boolean) {
        viewModelScope.launch {
            if (isSuccess) {
                getPlacesDetails(context)
            } else {
                _uiState.update {
                    PlacesUiState.Sync.Initial
                }
                emitToastEvent("Takeout data not received")
            }
        }
    }

    private fun getPlacesDetails(context: Context) {
        viewModelScope.launch {
            _uiState.update {
                PlacesUiState.Sync.Loading
            }

            placesRepository.getPlacesDetails(getPlacesIds(context))
                .collectLatest { placesDetailsReceived ->
                    if (placesDetailsReceived) {
                        _uiState.update {
                            PlacesUiState.Gemini.PlacesRecommendation(emptyList())
                        }
                    } else {
                        _uiState.update {
                            PlacesUiState.Sync.Initial
                        }
                        emitToastEvent("All places details not received")
                    }
                }
        }
    }

    private fun emitToastEvent(reason: String) {
        Log.e("MainViewModel::emitToastEvent ", "REASON:: $reason")
        _uiEvent.update {
            UiEvent.ShowToast("Failed to get data! Try again")
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
        data class ShowToast(val message: String) : UiEvent
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
}
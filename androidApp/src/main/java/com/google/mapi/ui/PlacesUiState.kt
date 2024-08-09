package com.google.mapi.ui

sealed interface PlacesUiState {
    sealed interface Sync : PlacesUiState {
        data object Loading : Sync
        data object Initial : Sync
    }

    sealed interface Gemini : PlacesUiState {
        data object Loading : Gemini
        data class PlacesRecommendation(val places: List<PlaceUiModel>) : Gemini
    }
}

data class PlaceUiModel(val name: String, val url: String)

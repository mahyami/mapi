package com.google.mapi.ui

sealed interface PlacesUiState {
    sealed interface Sync : PlacesUiState {
        data object Loading : Sync
        data object Initial : Sync
    }

    sealed interface Gemini : PlacesUiState {
        data object Loading : Gemini
        data class PlacesRecommendation(val places: Places) : Gemini
        sealed interface Places {
            data object NotFound : Places
            data class Found(val items: List<PlaceUiModel>) : Places
        }
    }
}

data class PlaceUiModel(val name: String, val url: String)

package com.google.mapi.ui

sealed interface PlacesUiState {
    sealed interface Success: PlacesUiState {
        data object Initial : Success
        data object Synced : Success
        data class PlacesRecommendation(val places: List<PlaceUiModel>) : Success
    }
    data class Loading(val type: LoadingType) : PlacesUiState {
        enum class LoadingType {
            SYNC_LOADING, GEMINI_LOADING
        }
    }
}

data class PlaceUiModel(val name: String, val url: String)

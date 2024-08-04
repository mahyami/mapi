package com.google.mapi.ui

sealed interface PlacesUiState {
    data class Success(val places: List<PlaceUiModel>) : PlacesUiState
    data object Loading : PlacesUiState
}

data class PlaceUiModel(val name: String, val url: String)

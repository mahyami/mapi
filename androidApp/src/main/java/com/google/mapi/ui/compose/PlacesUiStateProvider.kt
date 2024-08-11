package com.google.mapi.ui.compose

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.mapi.ui.PlaceUiModel
import com.google.mapi.ui.PlacesUiState

class PlacesUiStateProvider : PreviewParameterProvider<PlacesUiState> {
    override val values = sequenceOf(
        PlacesUiState.Sync.Initial,
        PlacesUiState.Sync.Loading,
        PlacesUiState.Gemini.Loading,
        PlacesUiState.Gemini.PlacesRecommendation(PlacesUiState.Gemini.Places.NotFound),
        PlacesUiState.Gemini.PlacesRecommendation(
            PlacesUiState.Gemini.Places.Found(
                listOf(
                    PlaceUiModel(
                        name = "Place 1",
                        url = "",
                    ),
                    PlaceUiModel(
                        name = "Place 2 has a very veery long name",
                        url = "",
                    )
                )
            )
        ),
        PlacesUiState.Gemini.PlacesRecommendation(
            PlacesUiState.Gemini.Places.Found(
                emptyList()
            )
        )
    )
}
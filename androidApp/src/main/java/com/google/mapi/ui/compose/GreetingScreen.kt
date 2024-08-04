package com.google.mapi.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.mapi.R
import com.google.mapi.ui.Colors
import com.google.mapi.ui.MapiTheme
import com.google.mapi.ui.PlaceUiModel
import com.google.mapi.ui.PlacesUiState

@Composable
fun GreetingScreen(
    onSyncButtonClicked: () -> Unit,
    onSubmitButtonClicked: (String) -> Unit,
    onOpenMapsClicked: (String) -> Unit,
    uiState: PlacesUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.title_welcome),
            fontWeight = FontWeight.Bold,
            color = Colors.Primary,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.subtitle_welcome),
            color = Colors.Primary,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.footer_welcome),
            color = Colors.Primary,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 24.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            onClick = onSyncButtonClicked,
        ) {
            Text(text = stringResource(R.string.button_sync))
        }

        var inputText by remember { mutableStateOf("") }

        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text(stringResource(R.string.input_placeholder)) },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 24.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            onClick = { onSubmitButtonClicked(inputText) },
            enabled = uiState is PlacesUiState.Success
        ) {

            Text(text = stringResource(R.string.button_submit))
        }

        when (uiState) {
            PlacesUiState.Loading -> {}
            is PlacesUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(items = uiState.places) { place ->
                        Row {
                            Text(
                                text = place.name,
                                modifier = Modifier.padding(8.dp)
                            )

                            ClickableText(
                                text = AnnotatedString(stringResource(R.string.maps_url)),
                                modifier = Modifier.padding(8.dp),
                                onClick = {
                                    onOpenMapsClicked(place.url)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DefaultPreview() {
    MapiTheme {
        GreetingScreen(
            onSyncButtonClicked = {},
            onSubmitButtonClicked = {},
            onOpenMapsClicked = {},
            uiState = PlacesUiState.Success(listOf(PlaceUiModel("Cafe", "https://maps.google.com")))
        )
    }
}
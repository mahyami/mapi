package com.google.mapi.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.google.mapi.R
import com.google.mapi.ui.Colors
import com.google.mapi.ui.Colors.BackgroundTint
import com.google.mapi.ui.MapiTheme
import com.google.mapi.ui.PlacesUiState

@Composable
fun GreetingScreen(
    onSyncButtonClicked: () -> Unit,
    onSubmitButtonClicked: (String) -> Unit,
    onOpenMapsClicked: (String) -> Unit,
    uiState: PlacesUiState
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.vertical_maps),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(0f),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(BackgroundTint, blendMode = BlendMode.Hardlight)
        )

        Box(
            modifier = Modifier
                .padding(40.dp)
                .zIndex(1f)
                .align(Alignment.Center),
        )
        {
            Column {
                Text(
                    text = stringResource(R.string.title_welcome),
                    color = Colors.White,
                    fontSize = 28.sp,
                    lineHeight = 32.sp,
                    fontFamily = FontFamily(Font(R.font.raleway_bold)),
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                when (uiState) {
                    is PlacesUiState.Loading -> {
                        when (uiState.type) {
                            PlacesUiState.Loading.LoadingType.SYNC_LOADING -> {

                            }

                            PlacesUiState.Loading.LoadingType.GEMINI_LOADING -> {

                            }
                        }
                    }

                    PlacesUiState.Success.Initial -> {
                        Image(
                            painter = painterResource(id = R.drawable.vertical_maps_sync),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                                .clipToBounds()
                                .clickable(onClick = onSyncButtonClicked),
                        )
                    }

                    is PlacesUiState.Success.PlacesRecommendation -> TODO()
                    PlacesUiState.Success.Synced -> TODO()

                }
            }

        }
    }
}
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = stringResource(R.string.title_welcome),
//            fontWeight = FontWeight.Bold,
//            color = Colors.Primary,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Text(
//            text = stringResource(R.string.subtitle_welcome),
//            color = Colors.Primary,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Text(
//            text = stringResource(R.string.footer_welcome),
//            color = Colors.Primary,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Button(
//            modifier = Modifier
//                .padding(top = 24.dp, bottom = 24.dp)
//                .align(Alignment.CenterHorizontally)
//                .fillMaxWidth(),
//            onClick = onSyncButtonClicked,
//        ) {
//            Text(text = stringResource(R.string.button_sync))
//        }
//
//        var inputText by remember { mutableStateOf("") }
//
//        TextField(
//            value = inputText,
//            onValueChange = { inputText = it },
//            label = { Text(stringResource(R.string.input_placeholder)) },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Button(
//            modifier = Modifier
//                .padding(top = 24.dp, bottom = 24.dp)
//                .align(Alignment.CenterHorizontally)
//                .fillMaxWidth(),
//            onClick = { onSubmitButtonClicked(inputText) },
//            enabled = uiState is PlacesUiState.Success
//        ) {
//
//            Text(text = stringResource(R.string.button_submit))
//        }
//
//        when (uiState) {
//            PlacesUiState.Loading -> {}
//            is PlacesUiState.Success -> {
//                LazyColumn(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    items(items = uiState.places) { place ->
//                        Row {
//                            Text(
//                                text = place.name,
//                                modifier = Modifier.padding(8.dp)
//                            )
//
//                            ClickableText(
//                                text = AnnotatedString(stringResource(R.string.maps_url)),
//                                modifier = Modifier.padding(8.dp),
//                                onClick = {
//                                    onOpenMapsClicked(place.url)
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DefaultPreview() {
    MapiTheme {
        GreetingScreen(
            onSyncButtonClicked = {},
            onSubmitButtonClicked = {},
            onOpenMapsClicked = {},
            uiState = PlacesUiState.Success.Initial
        )
    }
}
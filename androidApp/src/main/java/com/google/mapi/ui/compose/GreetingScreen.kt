package com.google.mapi.ui.compose

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.google.mapi.R
import com.google.mapi.ui.Colors
import com.google.mapi.ui.Colors.BackgroundTint
import com.google.mapi.ui.MapiTheme
import com.google.mapi.ui.PlaceUiModel
import com.google.mapi.ui.PlacesUiState
import kotlinx.coroutines.delay

@Composable
fun GreetingScreen(
    onSyncButtonClicked: () -> Unit,
    onAskGeminiButtonClicked: (String) -> Unit,
    onOpenMapsClicked: (String) -> Unit,
    uiState: PlacesUiState
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MaPiBackground()

        when (uiState) {
            is PlacesUiState.Gemini ->
                GeminiPlacesRecommendation(
                    uiState = uiState,
                    onSyncButtonClicked = onSyncButtonClicked,
                    onAskGeminiButtonClicked = onAskGeminiButtonClicked,
                    onOpenMapsClicked = onOpenMapsClicked
                )

            PlacesUiState.Sync.Initial -> SyncInitialScreen(onSyncButtonClicked)
            PlacesUiState.Sync.Loading -> SyncLoadingScreen()
        }
    }
}

@Composable
private fun MaPiBackground() {
    Image(
        painter = painterResource(id = R.drawable.vertical_maps),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .zIndex(0f),
        contentScale = ContentScale.Crop,
        colorFilter = ColorFilter.tint(BackgroundTint, blendMode = BlendMode.Hardlight)
    )
}

private const val RESYNC_BUTTON_WIDTH = 150

private const val USER_INPUT_HEIGHT = 300

private val roundedCornersRadius = 8.dp

private val textStyle = TextStyle(
    fontSize = 18.sp,
    fontFamily = FontFamily(Font(R.font.raleway_regular)),
)

@Composable
private fun GeminiPlacesRecommendation(
    uiState: PlacesUiState.Gemini,
    onSyncButtonClicked: () -> Unit,
    onAskGeminiButtonClicked: (String) -> Unit,
    onOpenMapsClicked: (String) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState !is PlacesUiState.Gemini.Loading) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.title_gemini_resync),
                    color = Colors.White,
                    fontSize = 28.sp,
                    lineHeight = 32.sp,
                    fontFamily = FontFamily(Font(R.font.raleway_bold)),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Image(
                    painter = painterResource(id = R.drawable.vertical_maps_sync),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .width(RESYNC_BUTTON_WIDTH.dp)
                        .clip(shape = RoundedCornerShape(roundedCornersRadius))
                        .clipToBounds()
                        .clickable(onClick = onSyncButtonClicked),
                )
            }
        }

        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            var inputText by remember { mutableStateOf("") }
            TextField(
                value = inputText,
                textStyle = textStyle,
                onValueChange = { inputText = it },
                label = {
                    Text(
                        color = Colors.Primary,
                        text = stringResource(R.string.input_placeholder),
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_semi_bold)),
                        ),
                    )
                },
                shape = RoundedCornerShape(roundedCornersRadius),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(USER_INPUT_HEIGHT.dp)
                    .padding(16.dp)
            )

            when (uiState) {
                PlacesUiState.Gemini.Loading -> Loading(
                    loadingDrawable = R.drawable.gemini
                )

                is PlacesUiState.Gemini.PlacesRecommendation -> {
                    AskGeminiButton(
                        onAskGeminiButtonClicked = { onAskGeminiButtonClicked(inputText) }
                    )

                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp)
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        items(items = uiState.places) { place ->
                            PlaceRow(
                                place = place,
                                onOpenMapsClicked = onOpenMapsClicked
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun PlaceRow(
    place: PlaceUiModel,
    onOpenMapsClicked: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .background(
                color = Colors.TransparentWhite,
                shape = RoundedCornerShape(roundedCornersRadius)
            )
    ) {
        Text(
            text = place.name,
            style = textStyle.copy(fontSize = 16.sp),
            maxLines = 2,
            modifier = Modifier
                .padding(8.dp)
                .weight(2f)
        )

        ClickableText(
            text = AnnotatedString(stringResource(R.string.maps_url)),
            style = TextStyle(
                color = Colors.Secondary,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.raleway_bold)),
            ),
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically)
                .weight(1f),
            onClick = {
                onOpenMapsClicked(place.url)
            }
        )
    }
}

@Composable
private fun ColumnScope.AskGeminiButton(onAskGeminiButtonClicked: () -> Unit) {
    Row(
        modifier = Modifier.Companion
            .align(Alignment.CenterHorizontally)
            .background(
                color = Colors.TransparentWhite,
                shape = RoundedCornerShape(roundedCornersRadius)
            )
            .padding(roundedCornersRadius)
            .clickable {
                onAskGeminiButtonClicked()
            }
    ) {
        Text(
            text = stringResource(R.string.button_gemini_go),
            color = Colors.Primary,
            fontSize = 32.sp,
            fontFamily = FontFamily(Font(R.font.raleway_bold)),
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        Image(
            painter = painterResource(id = R.drawable.gemini),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
        )
    }
}

@Composable
private fun Loading(@DrawableRes loadingDrawable: Int) {
    var alpha by remember { mutableStateOf(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            while (alpha > 0f) {
                delay(100)
                alpha -= 0.1f
            }
            while (alpha < 1f) {
                delay(100)
                alpha += 0.1f
            }
        }
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        label = "LoadingFadeOutFadeInAnimation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(animatedAlpha),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0 until 3) {
            Image(
                painter = painterResource(id = loadingDrawable),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(top = 16.dp),
            )
        }
    }
}

@Composable
private fun BoxScope.SyncInitialScreen(onSyncButtonClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(40.dp)
            .zIndex(1f)
            .align(Alignment.Center),
    )
    {
        Column {
            Text(
                text = stringResource(R.string.title_sync_initial),
                color = Colors.White,
                fontSize = 28.sp,
                lineHeight = 32.sp,
                fontFamily = FontFamily(Font(R.font.raleway_bold)),
                modifier = Modifier
                    .fillMaxWidth(),
            )
            Image(
                painter = painterResource(id = R.drawable.vertical_maps_sync),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clip(shape = RoundedCornerShape(roundedCornersRadius))
                    .clipToBounds()
                    .clickable(onClick = onSyncButtonClicked),
            )
        }
    }
}

@Composable
private fun BoxScope.SyncLoadingScreen() {
    Box(
        modifier = Modifier
            .padding(40.dp)
            .zIndex(1f)
            .align(Alignment.Center),
    )
    {
        Column {
            Text(
                text = stringResource(R.string.title_sync_loading),
                color = Colors.White,
                fontSize = 28.sp,
                lineHeight = 32.sp,
                fontFamily = FontFamily(Font(R.font.raleway_bold)),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            )
            Loading(R.drawable.maps_dot)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DefaultPreview(
    @PreviewParameter(PlacesUiStateProvider::class) uiState: PlacesUiState

) {
    MapiTheme {
        GreetingScreen(
            onSyncButtonClicked = {},
            onAskGeminiButtonClicked = {},
            onOpenMapsClicked = {},
            uiState = uiState
        )
    }
}
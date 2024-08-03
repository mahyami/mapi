package com.google.mapi.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.mapi.R
import com.google.mapi.ui.Colors
import com.google.mapi.ui.MapiTheme

@Composable
fun GreetingScreen(onButtonClick: () -> Unit) {
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
            onClick = onButtonClick,

            ) {
            Text(text = stringResource(R.string.button_sync))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DefaultPreview() {
    MapiTheme {
        GreetingScreen({})
    }
}
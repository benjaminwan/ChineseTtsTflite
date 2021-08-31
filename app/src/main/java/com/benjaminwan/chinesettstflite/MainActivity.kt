package com.benjaminwan.chinesettstflite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.benjaminwan.chinesettstflite.R
import com.benjaminwan.chinesettstflite.tts.TtsManager
import com.benjaminwan.chinesettstflite.ui.speech.SpeechScreen
import com.benjaminwan.chinesettstflite.ui.theme.ChineseTtsTfliteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TtsManager.initModels(this)
        setContent {
            ChineseTtsTfliteTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        topBar = { MainTopAppBar() },
                        bottomBar = {},
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            SpeechScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainTopAppBar() {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name), maxLines = 2)
        },
        modifier = Modifier
            .requiredHeight(42.dp)
            .fillMaxWidth(),
        navigationIcon = {

        },
        actions = {

        },
    )
}


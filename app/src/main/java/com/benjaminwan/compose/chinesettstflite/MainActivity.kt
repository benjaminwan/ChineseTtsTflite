package com.benjaminwan.compose.chinesettstflite

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
import androidx.compose.ui.unit.dp
import com.benjaminwan.compose.chinesettstflite.tts.TtsManager
import com.benjaminwan.compose.chinesettstflite.ui.speech.SpeechScreen
import com.benjaminwan.compose.chinesettstflite.ui.theme.ChineseTtsTfliteTheme

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
    val title = "TTS"
    TopAppBar(
        title = {
            Text(text = title, maxLines = 2)
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


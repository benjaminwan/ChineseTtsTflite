package com.benjaminwan.chinesettstflite.ui.speech

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.benjaminwan.chinesettstflite.R
import com.benjaminwan.chinesettstflite.models.TtsType
import com.benjaminwan.chinesettstflite.tts.TtsManager
import com.benjaminwan.chinesettstflite.ui.MainViewModel
import com.benjaminwan.chinesettstflite.utils.gotoTtsSetting

@Composable
fun SpeechScreen(mainVM: MainViewModel) {
    val defText = stringResource(R.string.zho_sample)
    var speechText by remember { mutableStateOf(defText) }
    val type by TtsManager.typeState //模型选择
    val speed by TtsManager.speedState //语速控制
    val speedEnable = type == TtsType.FASTSPEECH2
    val ttsReady by mainVM.ttsReadyState //tts初始化状态
    val isSpeak by mainVM.speakState//是否正在朗读
    Column {
        OutlinedTextField(
            value = speechText,
            onValueChange = { speechText = it },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            enabled = ttsReady,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 4.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.tts_model_select))
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable { TtsManager.type = TtsType.FASTSPEECH2 },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = TtsType.FASTSPEECH2.name)
                    RadioButton(
                        selected = type == TtsType.FASTSPEECH2,
                        onClick = { TtsManager.type = TtsType.FASTSPEECH2 },
                        enabled = ttsReady
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable { TtsManager.type = TtsType.TACOTRON2 },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = TtsType.TACOTRON2.name)
                    RadioButton(
                        selected = type == TtsType.TACOTRON2,
                        onClick = { TtsManager.type = TtsType.TACOTRON2 },
                        enabled = ttsReady
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 4.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.tts_speed_select))
            Row {
                RadioButton(
                    selected = speed == 1.2f,
                    onClick = { TtsManager.speed = 1.2f },
                    enabled = speedEnable && ttsReady
                )
                Text(text = stringResource(R.string.tts_speed_slow))
            }
            Row {
                RadioButton(
                    selected = speed == 1.0f,
                    onClick = { TtsManager.speed = 1.0f },
                    enabled = speedEnable && ttsReady
                )
                Text(text = stringResource(R.string.tts_speed_mid))
            }
            Row {
                RadioButton(
                    selected = speed == 0.8f,
                    onClick = { TtsManager.speed = 0.8f },
                    enabled = speedEnable && ttsReady
                )
                Text(text = stringResource(R.string.tts_speed_fast))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 4.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val context = LocalContext.current
            Button(onClick = { context.gotoTtsSetting() }) {
                Text(text = stringResource(R.string.goto_tts_setting))
            }
            Button(
                onClick = { mainVM.sayText(speechText) },
                enabled = ttsReady && !isSpeak
            ) {
                Text(text = stringResource(R.string.start_speech))
            }
            Button(
                onClick = { mainVM.stop() },
                enabled = ttsReady && isSpeak
            ) {
                Text(text = stringResource(R.string.stop_speech))
            }
        }
    }
}
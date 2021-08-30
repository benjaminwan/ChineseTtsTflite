package com.benjaminwan.compose.chinesettstflite.ui.speech

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benjaminwan.compose.chinesettstflite.models.AudioData
import com.benjaminwan.compose.chinesettstflite.models.TtsType
import com.benjaminwan.compose.chinesettstflite.tts.TtsManager

@Composable
fun SpeechScreen() {
    //var speechText by remember { mutableStateOf("君不见,黄河之水天上来,奔流到海不复回,君不见,高堂明镜悲白发,朝如青丝暮成雪,人生得意须尽欢,莫使金樽空对月") }
    var speechText by remember { mutableStateOf("时间就像海绵里的水，只要愿挤，总还是有的。其实地上本没有路，走的人多了，也便成了路。") }
    val ttsType by TtsManager.ttsTypeState
    val speed by TtsManager.speedState
    val speedEnable = ttsType == TtsType.FASTSPEECH2
    val ttsReady by TtsManager.ttsReadyState
    val ttsState by TtsManager.ttsState
    val currentSpeech by TtsManager.currentSpeechState
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
                .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "语音模型选择:")
            Row {
                RadioButton(
                    selected = ttsType == TtsType.FASTSPEECH2,
                    onClick = { TtsManager.ttsType = TtsType.FASTSPEECH2 },
                    enabled = ttsReady
                )
                Text(text = TtsType.FASTSPEECH2.name)
            }
            Row {
                RadioButton(
                    selected = ttsType == TtsType.TACOTRON2,
                    onClick = { TtsManager.ttsType = TtsType.TACOTRON2 },
                    enabled = ttsReady
                )
                Text(text = TtsType.TACOTRON2.name)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 4.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "语速控制：")
            Row {
                RadioButton(selected = speed == 1.2f, onClick = { TtsManager.speed = 1.2f }, enabled = speedEnable && ttsReady)
                Text(text = "慢")
            }
            Row {
                RadioButton(selected = speed == 1.0f, onClick = { TtsManager.speed = 1.0f }, enabled = speedEnable && ttsReady)
                Text(text = "普通")
            }
            Row {
                RadioButton(selected = speed == 0.8f, onClick = { TtsManager.speed = 0.8f }, enabled = speedEnable && ttsReady)
                Text(text = "快")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 4.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { TtsManager.input(speechText) },
                enabled = ttsReady && ttsState.isStop
            ) {
                Text(text = "开始")
            }
            Button(
                onClick = { TtsManager.stop() },
                enabled = ttsReady && ttsState.isStart
            ) {
                Text(text = "停止")
            }
        }
        if (ttsState.isStart && currentSpeech != AudioData.emptyAudioData) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp, 4.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text(text = "当前朗读: ")
                    Text(text = currentSpeech.text, style = MaterialTheme.typography.body2)
                }
                Row {
                    Text(text = "当前/总共: ")
                    Text(text = "${currentSpeech.pos}/${currentSpeech.max}")
                }
            }
        }
    }
}
package com.benjaminwan.compose.chinesettstflite.ui.speech

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benjaminwan.compose.chinesettstflite.models.TtsType

@Composable
fun SpeechScreen() {
    var speechText by remember { mutableStateOf("时间就像海绵里的水，只要愿挤，总还是有的。其实地上本没有路，走的人多了，也便成了路。") }
    var ttsType by remember { mutableStateOf(TtsType.FASTSPEECH2) }
    var speed by remember { mutableStateOf(1.0f) }
    val speedEnable = ttsType == TtsType.FASTSPEECH2
    Column {
        OutlinedTextField(
            value = speechText,
            onValueChange = { speechText = it },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "语音模型选择:")
            Row {
                RadioButton(selected = ttsType == TtsType.FASTSPEECH2, onClick = { ttsType = TtsType.FASTSPEECH2 })
                Text(text = TtsType.FASTSPEECH2.name)
            }
            Row {
                RadioButton(selected = ttsType == TtsType.TACOTRON2, onClick = { ttsType = TtsType.TACOTRON2 })
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
                RadioButton(selected = speed == 1.2f, onClick = { speed = 1.2f }, enabled = speedEnable)
                Text(text = "慢")
            }
            Row {
                RadioButton(selected = speed == 1.0f, onClick = { speed = 1.0f }, enabled = speedEnable)
                Text(text = "普通")
            }
            Row {
                RadioButton(selected = speed == 0.8f, onClick = { speed = 0.8f }, enabled = speedEnable)
                Text(text = "快")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 4.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "开始")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "停止")
            }
        }
    }
}
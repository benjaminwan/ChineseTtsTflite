package com.benjaminwan.chinesettstflite.ui.speech

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    val ttsReady by mainVM.ttsReadyState //tts初始化状态
    val isSpeak by mainVM.speakState//是否正在朗读
    val isTypeEnable = ttsReady && !isSpeak
    val isSpeedEnable = ttsReady && !isSpeak && type == TtsType.FASTSPEECH2
    val onFastSpeechClick: (() -> Unit) = {
        TtsManager.type = TtsType.FASTSPEECH2
    }
    val onTacotronClick: (() -> Unit) = {
        TtsManager.type = TtsType.TACOTRON2
    }
    val onSpeedSlowClick: (() -> Unit) = {
        TtsManager.speed = 1.2f
    }
    val onSpeedNormalClick: (() -> Unit) = {
        TtsManager.speed = 1.0f
    }
    val onSpeedFastClick: (() -> Unit) = {
        TtsManager.speed = 0.8f
    }
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
                .padding(4.dp, 2.dp),
        ) {
            Text(text = stringResource(R.string.tts_model_select))
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(4.dp, 2.dp),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(isTypeEnable, onClick = onFastSpeechClick),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = TtsType.FASTSPEECH2.name)
                    RadioButton(
                        selected = type == TtsType.FASTSPEECH2,
                        onClick = onFastSpeechClick,
                        enabled = isTypeEnable
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(isTypeEnable, onClick = onTacotronClick),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = TtsType.TACOTRON2.name)
                    RadioButton(
                        selected = type == TtsType.TACOTRON2,
                        onClick = onTacotronClick,
                        enabled = isTypeEnable
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(R.string.tts_speed_select))
            Row(
                modifier = Modifier.clickable(isSpeedEnable, onClick = onSpeedSlowClick),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = speed == 1.2f,
                    onClick = onSpeedSlowClick,
                    enabled = isSpeedEnable
                )
                Text(text = stringResource(R.string.tts_speed_slow))
            }
            Row(
                modifier = Modifier.clickable(isSpeedEnable, onClick = onSpeedNormalClick),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = speed == 1.0f,
                    onClick = onSpeedNormalClick,
                    enabled = isSpeedEnable
                )
                Text(text = stringResource(R.string.tts_speed_mid))
            }
            Row(
                modifier = Modifier.clickable(isSpeedEnable, onClick = onSpeedFastClick),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = speed == 0.8f,
                    onClick = onSpeedFastClick,
                    enabled = isSpeedEnable
                )
                Text(text = stringResource(R.string.tts_speed_fast))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
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
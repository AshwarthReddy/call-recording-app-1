package com.anr.call_recording_app_1.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.anr.recording.model.RegisteredCall
import java.io.File

class CallRecordingPlayerActivity : AppCompatActivity() {


    private val recorder by lazy {
        AudioRecorderImpl(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null
    companion object {
        private const val fileName = "mobilenumber"
        fun intent(context: Context, callerId: RegisteredCall) =
            Intent(context, CallRecordingPlayerActivity::class.java).apply {
                putExtra(fileName, callerId)
            }
    }

    private val callerInfo: RegisteredCall by lazy {
        intent?.getSerializableExtra(fileName) as RegisteredCall
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var filePath = applicationContext.filesDir.absolutePath;
        val file: File = File("$filePath/audio1.mp3")
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    callerInfo.audioFile.also {
                        if (it != null) {
                            recorder?.start(it)
                        }
                        audioFile = it
                    }
                }) {
                    Text(text = "Start recording")
                }
                Button(onClick = {
                    recorder?.stop()
                }) {
                    Text(text = "Stop recording")
                }
                Button(onClick = {
                    player.playFile(callerInfo.audioFile ?: return@Button)
                }) {
                    Text(text = "Play")
                }
                Button(onClick = {
                    player.stop()
                }) {
                    Text(text = "Stop playing")
                }
            }
        }
    }


}

package com.anr.call_recording_app_1.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import java.io.File


class CallBroadcastReceiver : BroadcastReceiver() {

    private lateinit var telephonyManager: TelephonyManager
    private lateinit var applicationContext: Context;

    private val recorder by lazy {
        applicationContext?.let { AudioRecorderImpl(it) }
    }


    private fun startCallRecording() {
        telephonyManager = applicationContext?.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                when (state) {
                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                        startRecording();
                    }

                    TelephonyManager.CALL_STATE_IDLE -> {
                        recorder?.stop()
                    }
                }
            }
        }

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun startRecording() {
        var filePath = applicationContext.filesDir.absolutePath;

        try {
            val file: File = File(filePath)
            if (!file.exists()){
                file.mkdirs();
            }
            File(applicationContext.cacheDir, "audio1.mp3").also {
                recorder?.start(it)
            }

        } catch (e: Exception) {
            Log.e("error###, ", e.stackTraceToString())
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) applicationContext = context
        startCallRecording()
    }
}
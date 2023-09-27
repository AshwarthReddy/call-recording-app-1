package com.anr.call_recording_app_1.service

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}
package com.anr.call_recording_app_1.service

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}
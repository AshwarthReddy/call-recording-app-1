package com.anr.recording.model

import java.io.File
import java.io.Serializable
import java.time.LocalDateTime


enum class CallType {
    INCOMING,
    OUTGOING,
    MISSED
}


data class RegisteredCall(
    val id: Int,
    val mobileNumber: String,
    var name: String?,
    var callType: CallType?,
    var dateTime: LocalDateTime,
    var duration: Long?,
    var audioFile: File?
): Serializable




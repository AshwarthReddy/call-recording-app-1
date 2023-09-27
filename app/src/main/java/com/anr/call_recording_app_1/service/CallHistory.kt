package com.anr.call_recording_app_1.service

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import android.util.Log
import com.anr.recording.model.CallType
import com.anr.recording.model.RegisteredCall
import com.google.gson.Gson
import java.time.Instant
import java.time.LocalDateTime
import java.util.TimeZone


@SuppressLint("Recycle", "Range")
fun getCallLogs(ctx: Context): List<RegisteredCall> {
    val callsHistory: ArrayList<RegisteredCall> = ArrayList()
    val query = ctx.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)
    query.apply {
        val managedCursor = this;

        var count: Int = 1;
        if (managedCursor != null) {
            while (managedCursor.moveToNext()) {
                val phNumber: String =
                    managedCursor.getString(managedCursor.getColumnIndex(CallLog.Calls.NUMBER))
                val callDuration: Long =
                    managedCursor.getLong(managedCursor.getColumnIndex(CallLog.Calls.DURATION))
                val date: Long =
                    managedCursor.getLong(managedCursor.getColumnIndex(CallLog.Calls.DATE))
                val name = managedCursor.getString(managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(date),
                    TimeZone.getDefault().toZoneId()
                )
                val call = RegisteredCall(
                    count,
                    phNumber, name,
                    getCallType(cursor = managedCursor),
                    convertLocalDate(callDuration),
                    callDuration, null
                )
                callsHistory.add(call)

                count++
            }
        }

    }
    Log.i("call history", callsHistory.toString())

    return callsHistory;
}

@SuppressLint("Range")
fun getCallHistory(ctx: Context, mobileNumber: String): List<RegisteredCall> {
    val callsHistory: ArrayList<RegisteredCall> = ArrayList();
    val cursor: Cursor? = ctx.contentResolver.query(
        CallLog.Calls.CONTENT_URI,
        null,
        CallLog.Calls.NUMBER + " = ? ",
        arrayOf(mobileNumber),
        ""
    )
    if (cursor != null) {
        Log.i("columns", cursor.columnNames.toString())
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(CallLog.Calls._ID))
                val number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                val name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
                val photoId = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_PHOTO_ID))
                val duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION))
                val date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
                var dir: CallType? = getCallType(cursor)
                callsHistory.add(
                    RegisteredCall(
                        id,
                        number, name, dir,
                        convertLocalDate(date),
                        duration, null
                    )
                );
            };
        }
    }
    val json = Gson().toJson(callsHistory)
    return callsHistory;
}

@SuppressLint("Range")
private fun getCallType(cursor: Cursor): CallType? {
    val callType: String =
        cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))
    val dircode = callType.toInt()
    var dir: CallType? = null
    when (dircode) {
        CallLog.Calls.OUTGOING_TYPE -> dir = CallType.OUTGOING
        CallLog.Calls.INCOMING_TYPE -> dir = CallType.INCOMING
        CallLog.Calls.MISSED_TYPE -> dir = CallType.MISSED
    }
    return dir
}


fun convertLocalDate(timeStamp: Long): LocalDateTime = LocalDateTime.ofInstant(
    Instant.ofEpochMilli(timeStamp), TimeZone
        .getDefault().toZoneId()
);
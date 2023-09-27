package com.anr.call_recording_app_1

import android.Manifest.permission.*
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.anr.call_recording_app_1.ui.theme.CallHistoryScreen

class MainActivity : ComponentActivity() {


    @RequiresApi(34)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (hasPermissions(this)) {
                CallHistoryScreen() {
                    startActivity(CallerIdHistoryActivity.intent(this, it))
                }
            } else {
                requestPermissions(this)
            }

        }

    }
}


@RequiresApi(34)
private fun hasPermissions(ctx: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        ctx,
        RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                ctx,
                READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                ctx,
                READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                ctx,
                READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
}

@RequiresApi(34)
private fun requestPermissions(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity, arrayOf(
            RECORD_AUDIO, READ_CALL_LOG,
            READ_PHONE_STATE,
            READ_MEDIA_AUDIO,
            WRITE_EXTERNAL_STORAGE,
            READ_EXTERNAL_STORAGE,
            MANAGE_DEVICE_POLICY_ACCESSIBILITY,
        ), PackageManager.PERMISSION_GRANTED
    )
}










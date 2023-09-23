package com.anr.call_recording_app_1

import android.Manifest.permission
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.anr.call_recording_app_1.service.getCallLogs
import com.anr.call_recording_app_1.ui.theme.Callrecordingapp1Theme
import com.anr.recording.model.RegisteredCall
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val CAMERA_REQUEST = 1888

    @RequiresApi(34)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var result = ActivityCompat.requestPermissions(this, arrayOf(
            permission.RECORD_AUDIO,permission.READ_CALL_LOG,
            permission.READ_PHONE_STATE,
            permission.READ_MEDIA_AUDIO,
            permission.READ_EXTERNAL_STORAGE,
            permission.MANAGE_DEVICE_POLICY_ACCESSIBILITY
        ), PackageManager.PERMISSION_GRANTED)
//        Settings.Secure.putString(getContentResolver(),
//            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, "com.anr.call_recording_app_1/CallRecordingService");
//        Settings.Secure.putString(getContentResolver(),
//            Settings.Secure.ACCESSIBILITY_ENABLED, "1");
        Log.i("result", result.toString());

        setContent {
            Callrecordingapp1Theme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorScheme.background
                ) {
                    CallHistoryMenu() {
                        startActivity(CallerIdHistory.intent(this, it))
                    }
                }

            }
        }

    }
}


    @ExperimentalMaterial3Api
    @Composable
    fun CallHistoryMenu(selectedItem: (RegisteredCall) -> Unit) {
        val context = LocalContext.current
        val callLogs = remember { getCallLogs(context) };

        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            "call history",
                            maxLines = 1,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    scrollBehavior = scrollBehavior
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(callLogs) {
                    CallHistoryCard(it, selectedItem = selectedItem)
                }
            }
        }
    }

    @Composable
    fun CallHistoryCard(
        registeredCall: RegisteredCall,
        selectedItem: (RegisteredCall) -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { selectedItem(registeredCall) }

            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Call,
                        contentDescription = "Incoming Call"
                    )
                }
                Column(modifier = Modifier.padding(8.dp)) {
                    val useName = if (registeredCall.name?.isBlank() == true) {
                        registeredCall.mobileNumber
                    } else {
                        registeredCall.name
                    };
                    if (useName != null) {
                        Text(
                            text = useName,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = registeredCall.dateTime.format(
                                DateTimeFormatter.ofPattern(
                                    "mm-dd-yyyy",
                                    Locale.ENGLISH
                                )
                            ),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "${registeredCall.duration.toString()} sec",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }










package com.anr.call_recording_app_1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
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
import com.anr.call_recording_app_1.service.getCallHistory
import com.anr.recording.model.RegisteredCall
import java.time.format.DateTimeFormatter
import java.util.Locale

class CallerIdHistory : AppCompatActivity() {

    companion object {
        private const val mobileNumber = "mobilenumber"
        fun intent(context: Context, callerId: RegisteredCall) =
            Intent(context, CallerIdHistory::class.java).apply {
                putExtra(mobileNumber, callerId)
            }
    }

    private val callerInfo: RegisteredCall by lazy {
        intent?.getSerializableExtra(mobileNumber) as RegisteredCall
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val useName = getUserName()
        setContent {
//            DisplayMobileNumber(callerInfo.mobileNumber)
            CallerIdHistory(callerInfo.mobileNumber, useName)
        }
    }

    @Composable
    fun DisplayMobileNumber(mobileNumber:String){
        Text(text = mobileNumber)
    }

    private fun getUserName(): String? {
        val useName = if (callerInfo.name?.isBlank() == true) {
            callerInfo.mobileNumber
        } else {
            callerInfo.name
        };
        return useName
    }
}


@ExperimentalMaterial3Api
@Composable
fun CallerIdHistory(mobileNumber: String, name: String?) {
    val context = LocalContext.current
    val callerIdHistory = remember { getCallHistory(context, mobileNumber) };

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ), title = {
                    Text(
                        "call history - $name", maxLines = 1, fontWeight = FontWeight.Bold
                    )
                }, scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(callerIdHistory) {
                CallHistoryCard(it)
            }
        }
    }
}


@Composable
fun CallHistoryCard(
    registeredCall: RegisteredCall
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)

        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Call, contentDescription = "Incoming Call"
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
                        text = useName, style = MaterialTheme.typography.bodyLarge
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
                                "mm-dd-yyyy", Locale.ENGLISH
                            )
                        ), style = MaterialTheme.typography.bodyLarge
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


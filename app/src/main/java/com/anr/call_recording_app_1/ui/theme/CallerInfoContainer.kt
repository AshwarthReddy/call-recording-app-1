package com.anr.call_recording_app_1.ui.theme

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.anr.recording.model.RegisteredCall
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CallerInfoContainer(
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
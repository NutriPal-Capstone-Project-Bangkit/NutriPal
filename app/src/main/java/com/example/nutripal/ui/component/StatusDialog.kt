package com.example.nutripal.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.ui.text.font.FontWeight
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.ui.theme.NunitoFontFamily

@Composable
fun StatusDialog(
    showDialog: Boolean,
    isSuccess: Boolean,
    message: String,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Animated Icon
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                color = if (isSuccess) Primary.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSuccess) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Success",
                                tint = Primary,
                                modifier = Modifier.size(40.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                contentDescription = "Error",
                                tint = Color.Red,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    // Message
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = if (isSuccess) Primary else Color.Red
                    )

                    // Button
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSuccess) Primary else Color.Red
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "OK",
                            color = Color.White,
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
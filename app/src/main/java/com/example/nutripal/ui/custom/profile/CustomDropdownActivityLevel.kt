package com.example.nutripal.ui.custom.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutripal.R
import com.example.nutripal.ui.theme.Disabled
import com.example.nutripal.ui.theme.NunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownActivityLevel(
    selectedOption: String,
    isExpanded: Boolean,
    onExpandedChange: () -> Unit,
    onOptionSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            "Level Aktivitas",
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { onExpandedChange() }
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = { },
                label = {
                    if (selectedOption.isEmpty()) {
                        Text(
                            "Pilih",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                            )
                        )
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Icon",
                        tint = Color(0xFF2A2A2A)
                    )
                },
                leadingIcon = {
                    val iconResId = when (selectedOption) {
                        "Santai" -> R.drawable.ic_santai
                        "Cukup Aktif" -> R.drawable.ic_cukup_aktif
                        "Aktif" -> R.drawable.ic_aktif
                        "Rutin" -> R.drawable.ic_rutin
                        else -> null
                    }
                    iconResId?.let {
                        Icon(
                            painter = painterResource(id = it),
                            contentDescription = "Selected Icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                readOnly = true,
                shape = MaterialTheme.shapes.medium,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Disabled,
                    focusedBorderColor = Disabled,
                    containerColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Color(0xFF2A2A2A)
                )
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, MaterialTheme.shapes.medium)
                    .border(0.5.dp, color = Disabled, MaterialTheme.shapes.medium)
            ) {
                DropdownMenuItem(
                    text = {
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_santai),
                                contentDescription = "Santai",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Santai",
                                style = TextStyle(
                                    fontFamily = NunitoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    },
                    onClick = {
                        onOptionSelected("Santai")
                        onDismiss()
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.5.dp,
                    color = Color(0xFFE1E1E1)
                )

                DropdownMenuItem(
                    text = {
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_cukup_aktif),
                                contentDescription = "Cukup Aktif",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Cukup Aktif",
                                style = TextStyle(
                                    fontFamily = NunitoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    },
                    onClick = {
                        onOptionSelected("Cukup Aktif")
                        onDismiss()
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.5.dp,
                    color = Color(0xFFE1E1E1)
                )

                DropdownMenuItem(
                    text = {
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_aktif),
                                contentDescription = "Aktif",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Aktif",
                                style = TextStyle(
                                    fontFamily = NunitoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    },
                    onClick = {
                        onOptionSelected("Aktif")
                        onDismiss()
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.5.dp,
                    color = Color(0xFFE1E1E1)
                )

                DropdownMenuItem(
                    text = {
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_rutin),
                                contentDescription = "Rutin",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Rutin",
                                style = TextStyle(
                                    fontFamily = NunitoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    },
                    onClick = {
                        onOptionSelected("Rutin")
                        onDismiss()
                    }
                )
            }
        }
    }
}

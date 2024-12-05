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
import com.example.nutripal.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownLifestyle(
    selectedOption: String,
    isExpanded: Boolean,
    onExpandedChange: () -> Unit,
    onOptionSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            "Gaya Hidup",
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

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
                    // Tampilkan ikon berdasarkan opsi yang dipilih
                    val iconResId = when (selectedOption) {
                        "Umum" -> R.drawable.ic_general_rounded
                        "Diet" -> R.drawable.ic_diet_rounded
                        "Atlet" -> R.drawable.ic_athlete_rounded
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
                                painter = painterResource(id = R.drawable.ic_general_rounded),
                                contentDescription = "Umum",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Umum",
                                style = TextStyle(
                                    fontFamily = NunitoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    },
                    onClick = {
                        onOptionSelected("Umum")
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
                                painter = painterResource(id = R.drawable.ic_diet_rounded),
                                contentDescription = "Diet",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Diet",
                                style = TextStyle(
                                    fontFamily = NunitoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    },
                    onClick = {
                        onOptionSelected("Diet")
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
                                painter = painterResource(id = R.drawable.ic_athlete_rounded),
                                contentDescription = "Atlet",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Atlet",
                                style = TextStyle(
                                    fontFamily = NunitoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    },
                    onClick = {
                        onOptionSelected("Atlet")
                        onDismiss()
                    }
                )
            }
        }
    }
}

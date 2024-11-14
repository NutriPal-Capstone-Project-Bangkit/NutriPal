package com.example.nutripal.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutripal.ui.theme.Disabled
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomGenderDropdown(
    selectedGender: String,
    isExpanded: Boolean,
    onExpandedChange: () -> Unit,
    onGenderSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            "Jenis Kelamin",
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { onExpandedChange() }
        ) {
            OutlinedTextField(
                value = selectedGender,
                onValueChange = { },
                label = {
                    if (selectedGender.isEmpty()) {
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
                readOnly = true,
                shape = MaterialTheme.shapes.medium,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Disabled,
                    focusedBorderColor = Disabled,
                    unfocusedLabelColor = Color(0xFF2A2A2A),
                    focusedLabelColor = Primary,
                    containerColor = Color.Transparent
                ),
                // Apply the same TextStyle to the selected value in the TextField
                textStyle = TextStyle(
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Color(0xFF2A2A2A) // Ensure the text color is dark enough for readability
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
                        Text(
                            "Pria",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                    },
                    onClick = {
                        onGenderSelected("Pria")
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.5.dp,
                    color = Color(0xFFE1E1E1)
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            "Wanita",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                    },
                    onClick = {
                        onGenderSelected("Wanita")
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                )
            }
        }
    }
}

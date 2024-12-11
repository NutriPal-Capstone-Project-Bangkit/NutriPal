package com.example.nutripal.ui.custom.scan.result.addtodaily

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
fun CustomProductDropdown(
    selectedProduct: String,
    isExpanded: Boolean,
    onExpandedChange: () -> Unit,
    onProductSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            "Jenis Produk",
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        )
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { onExpandedChange() }
        ) {
            OutlinedTextField(
                value = selectedProduct,
                onValueChange = { },
                label = {
                    if (selectedProduct.isEmpty()) {
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
                        Text(
                            "Makanan",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                    },
                    onClick = {
                        onProductSelected("Makanan")
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
                            "Minuman",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                    },
                    onClick = {
                        onProductSelected("Minuman")
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
                            "Snack",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                    },
                    onClick = {
                        onProductSelected("Snack")
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
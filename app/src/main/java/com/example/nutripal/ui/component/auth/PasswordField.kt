@file:Suppress("FunctionName")

package com.example.nutripal.ui.component.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.nutripal.ui.theme.NunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    password: String,
    onValueChange: (String) -> Unit,
    label: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(true) }

    // Check if the password is valid (at least 6 characters)
    val passwordErrorMessage = if (password.length < 6) "Password must be at least 6 characters" else ""

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = password,
            onValueChange = {
                if (it.length <= 16) {
                    onValueChange(it)
                }
                isPasswordValid = it.length >= 6 // Validate password length
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = { Text("Masukkan passwordmu", fontFamily = NunitoFontFamily) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Toggle password visibility",
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            label = {
                if (label != null) {
                    Text(text = label, fontFamily = NunitoFontFamily)
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFEFEFEF),
                focusedTextColor = Color(0xFF2A2A2A),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {  }
            ),
            isError = !isPasswordValid // Highlight error state if password is invalid
        )

        // Show error message if password is invalid
        if (!isPasswordValid && passwordErrorMessage.isNotEmpty()) {
            Text(
                text = passwordErrorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 16.dp, top = 4.dp) // Adjusting padding for better alignment
                    .align(Alignment.Start) // Align the error message to the start
            )
        }
    }
}

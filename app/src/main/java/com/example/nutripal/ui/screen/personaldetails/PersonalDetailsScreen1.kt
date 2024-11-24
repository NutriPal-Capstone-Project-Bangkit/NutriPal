@file:Suppress("FunctionName")

package com.example.nutripal.ui.screen.personaldetails

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.ui.component.*
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.custom.personaldetails.CustomGenderDropdown
import com.example.nutripal.ui.custom.personaldetails.CustomNameInput
import com.example.nutripal.ui.custom.personaldetails.CustomProfilePhotoSelector
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.viewmodel.PersonalDetailsViewModel

@Composable
fun PersonalDetailsScreen1(
    viewModel: PersonalDetailsViewModel = viewModel(),
    navController: NavController,
) {

    MainStatusBar()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        PersonalDetailsHeader(currentPage = 0, pageCount = 2)

        Spacer(modifier = Modifier.height(32.dp))

        // Header Text
        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF000000),
                    fontSize = 16.sp
                )
            ) {
                append("Isi Profil Kamu\n")
            }
        }

        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            lineHeight = 20.sp,
            textAlign = TextAlign.Left
        )

        Text(
            text = "Lengkapi data dirimu agar sistem bisa memberikan pengalaman terbaik!",
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color(0xFF2A2A2A)
            ),
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Name Input Field
        CustomNameInput(
            value = viewModel.name.value,
            onValueChange = { viewModel.updateName(it) },
            isError = viewModel.isError.value
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Gender Dropdown
        CustomGenderDropdown(
            selectedGender = viewModel.selectedGender.value,
            isExpanded = viewModel.isDropdownExpanded.value,
            onExpandedChange = { viewModel.toggleDropdown() },
            onGenderSelected = { viewModel.updateGender(it) },
            onDismiss = { viewModel.dismissDropdown() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Photo Selector
        CustomProfilePhotoSelector(
            onPhotoClick = { /* Handle photo selection */ }
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Lanjut Button
        Column(
            modifier = Modifier.padding(top = 48.dp)
        ) {
            ToggleGreenButton(
                text = "Lanjut",
                enabled = viewModel.isFormValid.value,
                onClick = { navController.navigate("personal_details_screen_2") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalDetailsScreen1Preview() {
    val navController = rememberNavController()
    PersonalDetailsScreen1(viewModel = viewModel(), navController)
}

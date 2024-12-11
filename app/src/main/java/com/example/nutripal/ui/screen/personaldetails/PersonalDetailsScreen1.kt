@file:Suppress("FunctionName")

package com.example.nutripal.ui.screen.personaldetails

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.nutripal.ui.custom.personaldetails.CustomAgeInput
import com.example.nutripal.ui.custom.personaldetails.CustomGenderDropdown
import com.example.nutripal.ui.custom.personaldetails.CustomNameInput
import com.example.nutripal.ui.custom.personaldetails.CustomProfilePhotoSelector
import com.example.nutripal.ui.custom.personaldetails.HeightInput
import com.example.nutripal.ui.custom.personaldetails.WeightInput
import com.example.nutripal.ui.theme.NunitoFontFamily

@Composable
fun PersonalDetailsScreen1(
    viewModel: PersonalDetailsViewModel = viewModel(),
    navController: NavController,
) {
    val isExpanded = remember { mutableStateOf(false) }

    // Get the selected profile photo URI from ViewModel
    val profilePictureUri = viewModel.profilePictureUri.value

    MainStatusBar()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            PersonalDetailsHeader(currentPage = 0, pageCount = 2)

            Spacer(modifier = Modifier.height(18.dp))

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
                lineHeight = 8.sp,
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
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Name Input Field
            CustomNameInput(
                value = viewModel.name.value,
                onValueChange = { viewModel.updateName(it) },
                isError = viewModel. isError.value
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomAgeInput(
                value = viewModel.age.value,
                onValueChange = { viewModel.updateAge(it) },
                isError = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Berat Badan dan Tinggi Badan dalam Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                WeightInput(
                    modifier = Modifier.weight(1f),
                    value = viewModel.weight.value,
                    onValueChange = { viewModel.updateWeight(it) },
                    isError = false
                )
                HeightInput(
                    modifier = Modifier.weight(1f),
                    value = viewModel.height.value,
                    onValueChange = { viewModel.updateHeight(it) },
                    isError = false
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gender Dropdown
            CustomGenderDropdown(
                modifier = Modifier.fillMaxWidth(),
                selectedGender = viewModel.selectedGender.value,
                isExpanded = isExpanded.value,
                onExpandedChange = { isExpanded.value = !isExpanded.value },
                onGenderSelected = { gender ->
                    viewModel.updateGender(gender)
                },
                onDismiss = { isExpanded.value = false }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Photo Selector
            CustomProfilePhotoSelector(
                modifier = Modifier.fillMaxWidth(),
                imageUri = profilePictureUri,
                onPhotoSelected = { uri ->
                    viewModel.updateProfilePicture(uri)
                    Log.d("PersonalDetailsScreen", "Photo selected: $uri")
                }
            )

            // Additional space at the bottom to ensure the button doesn't overlap content
            Spacer(modifier = Modifier.height(80.dp))
        }

        // Floating Button at the Bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White)
                .padding(vertical = 16.dp)
                .padding(horizontal = 16.dp)
        ) {
            ToggleGreenButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Lanjut",
                enabled = viewModel.isFormValid.value,
                onClick = {
                    viewModel.updatePage(1)
                    val profilePictureString = profilePictureUri?.toString() ?: ""
                    navController.navigate(
                        "personal_details_screen_2?" +
                                "name=${viewModel.name.value}" +
                                "&gender=${viewModel.selectedGender.value}" +
                                "&profilePicture=$profilePictureString" +
                                "&age=${viewModel.age.value}" +
                                "&weight=${viewModel.weight.value}" +
                                "&height=${viewModel.height.value}"
                    )
                }
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PersonalDetailsScreen1Preview() {
    val navController = rememberNavController()
    PersonalDetailsScreen1(viewModel(), navController)
}

package com.example.nutripal.ui.screen.personaldetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.data.model.FrameData
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.PersonalDetailsHeader
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.custom.personaldetails.CustomFrame

@Composable
fun PersonalDetailsScreen2(
    viewModel: PersonalDetailsViewModel = viewModel(),
    navController: NavController,
    name: String?,
    gender: String?,
    profilePicture: String?,
    age: String?,
    weight: String?,
    height: String?
) {
    val selectedFrameId = remember { mutableIntStateOf(-1) }
    val isButtonEnabled = selectedFrameId.intValue != -1

    // Define frames outside of any inner block to ensure it's accessible
    val frames = listOf(
        FrameData(
            R.drawable.ic_santai,
            "Santai",
            "Sedikit atau tidak berolahraga sama sekali. Aktivitas fisik minimal sehari-hari."
        ),
        FrameData(
            R.drawable.ic_cukup_aktif,
            "Cukup Aktif",
            "Olahraga ringan atau aktivitas fisik 1-3 hari per minggu. Tetap bergerak dengan santai."
        ),
        FrameData(
            R.drawable.ic_aktif,
            "Aktif",
            "Berolahraga aktif secara fisik 3-5 hari per minggu. Pilihan pas untuk keseimbangan."
        ),
        FrameData(
            R.drawable.ic_rutin,
            "Rutin",
            "Berolahraga berat atau intens hingga 6-7 hari seminggu. Hidup penuh energi!"
        )
    )

    // Update viewModel with passed details
    viewModel.updateName(name ?: "")
    viewModel.updateGender(gender ?: "")
    viewModel.updateProfilePictureFromString(profilePicture)
    viewModel.updateAge(age ?: "")
    viewModel.updateHeight(height ?: "")
    viewModel.updateWeight(weight ?: "")


    MainStatusBar()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 64.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            PersonalDetailsHeader(currentPage = 1, pageCount = 2)

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Level Aktivitas Kamu",
                style = TextStyle(
                    color = Color(0xFF000000),
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Pilih level aktivitas harianmu agar dapat memberikan\nrekomendasi yang sesuai.",
                style = TextStyle(
                    color = Color(0xFF2A2A2A),
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Render CustomFrame for each item
            frames.forEachIndexed { index, frame ->
                CustomFrame(
                    image = frame.image,
                    title = frame.title,
                    subtitle = frame.subtitle,
                    frameId = index, // Passing the index as the unique frame ID
                    selectedFrameId = selectedFrameId.intValue, // Passing the currently selected frame ID
                    onFrameSelected = { selectedFrameId.intValue = it } // Update selected frame
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
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
                text = "Simpan",
                enabled = isButtonEnabled,
                onClick = {
                    // Tambahkan ini untuk mengupdate activity level
                    viewModel.updateActivityLevel(frames[selectedFrameId.intValue].title)

                    viewModel.saveProfile(
                        name = name ?: "",
                        gender = gender ?: "",
                        age = age ?: "",
                        weight = weight ?: "",
                        height = height ?: "",
                        activityLevel = frames[selectedFrameId.intValue].title,
                        profilePicture = profilePicture ?: ""
                    )

                    navController.navigate("personal_detail_saved")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalDetailsScreen2Preview() {
    val navController = rememberNavController()
    PersonalDetailsScreen2(
        viewModel = viewModel(),
        navController = navController,
        name = "",
        gender = "",
        age = "",
        weight = "",
        height = "",
        profilePicture = ""
    )
}
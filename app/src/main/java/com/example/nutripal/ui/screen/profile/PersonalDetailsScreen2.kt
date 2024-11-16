package com.example.nutripal.ui.screen.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import com.example.nutripal.ui.component.PersonalDetailsHeader
import com.example.nutripal.ui.component.ToggleGreenButton
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.viewmodel.PersonalDetailsViewModel
import com.example.nutripal.ui.custom.CustomFrame
import com.example.nutripal.ui.navigation.Screen

@Composable
fun PersonalDetailsScreen2(
    viewModel: PersonalDetailsViewModel = viewModel(),
    navController: NavController
) {
    val selectedFrameId = remember { mutableStateOf(-1) }
    val isButtonEnabled = selectedFrameId.value != -1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // shared header
        PersonalDetailsHeader(currentPage = 1, pageCount = 2)

        Spacer(modifier = Modifier.height(32.dp))

        // Gaya Hidup Kami
        Text(
            text = "Gaya Hidup Kamu",
            style = TextStyle(
                color = Color(0xFF000000),
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Pilih tipe yang paling sesuai dengan kebutuhan dan\ntujuan nutrisi kamu.",
            style = TextStyle(
                color = Color(0xFF2A2A2A),
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // List of frames with their data
        val frames = listOf(
            FrameData(R.drawable.ic_umum, "Umum", "Pengguna yang ingin menjaga kesehatan\nsecara keseluruhan tanpa tujuan khusus."),
            FrameData(R.drawable.ic_diet, "Diet", "Pengguna yang ingin menjaga berat badan\natau menjalankan pola makan seimbang."),
            FrameData(R.drawable.ic_atlet, "Atlet", "Pengguna yang aktif berolahraga dan\nmembutuhkan asupan gizi lebih.")
        )

        // Render CustomFrame for each item
        frames.forEachIndexed { index, frame ->
            CustomFrame(
                image = frame.image,
                title = frame.title,
                subtitle = frame.subtitle,
                frameId = index, // Passing the index as the unique frame ID
                selectedFrameId = selectedFrameId.value, // Passing the currently selected frame ID
                onFrameSelected = { selectedFrameId.value = it } // Update selected frame
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(64.dp))

        ToggleGreenButton(
            text = "Simpan",
            enabled = isButtonEnabled,
            onClick = { navController.navigate(Screen.Home.route) }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun PersonalDetailsScreen2Preview() {
    val navController = rememberNavController()
    PersonalDetailsScreen2(viewModel = viewModel(), navController)
}

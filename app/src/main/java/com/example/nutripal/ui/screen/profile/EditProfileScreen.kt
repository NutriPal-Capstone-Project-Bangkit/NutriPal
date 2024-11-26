package com.example.nutripal.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.custom.personaldetails.CustomGenderDropdown
import com.example.nutripal.ui.custom.personaldetails.CustomNameInput
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    name: String,
    gender: String,
    onNameChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    navController: NavController
)
{
    MainStatusBar()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Edit Profile",
                        color = Primary,
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick() // Fungsi untuk menangani klik back
                        navController.popBackStack() // Kembali ke stack sebelumnya menggunakan NavController
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back), // Ganti dengan icon back yang sesuai
                            contentDescription = "Back",
                            modifier = Modifier.size(18.dp) // Menentukan ukuran ikon menjadi lebih kecil
                        )
                    }
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(top = 16.dp)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            // Profile picture
            ProfilePicture(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )

            // CustomNameInput dengan padding yang menyesuaikan dengan text field
            CustomNameInput(
                value = name,
                onValueChange = onNameChange,
                isError = false,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CustomGenderDropdown dengan padding yang menyesuaikan dengan text field
            CustomGenderDropdown(
                selectedGender = gender,
                isExpanded = false,
                onExpandedChange = { /* handle dropdown expansion */ },
                onGenderSelected = onGenderChange,
                onDismiss = { /* handle dropdown dismissal */ },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save button
            ToggleGreenButton(
                text = "Simpan",
                modifier = Modifier.fillMaxWidth(),
                onClick = onSaveClick
            )
        }
    }
}


@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(128.dp)
            .clip(CircleShape)  // Memastikan bentuk lingkaran
            .background(Color.Gray)  // Memberikan background warna putih
    )
}


@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    val navController = rememberNavController()
    val onNameChange: (String) -> Unit = { newName -> }
    val onGenderChange: (String) -> Unit = { newGender -> }
    val onSaveClick: () -> Unit = {}
    val onBackClick: () -> Unit = {}

    EditProfileScreen(
        name = "John Doe",
        gender = "Laki-Laki",
        onNameChange = onNameChange,
        onGenderChange = onGenderChange,
        onSaveClick = onSaveClick,
        onBackClick = onBackClick,
        navController = navController
    )
}

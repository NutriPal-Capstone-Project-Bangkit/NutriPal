package com.example.nutripal.ui.screen.profile.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.nutripal.R
import com.example.nutripal.data.remote.response.Profile
import com.example.nutripal.data.remote.retrofit.ApiConfig
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.custom.personaldetails.CustomGenderDropdown
import com.example.nutripal.ui.custom.personaldetails.CustomNameInput
import com.example.nutripal.ui.custom.profile.CustomDropdownLifestyle
import com.example.nutripal.ui.navigation.Screen
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.ui.theme.Primary
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel,
    onBackClick: () -> Unit,
    navController: NavController
) {
    // Collect profile state from ViewModel
    val profileState by viewModel.profileState.collectAsState()
    val updateStatus by viewModel.updateStatus.collectAsState()
    val context = LocalContext.current

    // Local states to manage input fields
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var lifestyle by remember { mutableStateOf("Umum") }
    var profilePicture by remember { mutableStateOf("") }
    var originalProfileState by remember { mutableStateOf<Profile?>(null) }
    val isDataChanged by viewModel.isDataChanged.collectAsState()



    // Dropdown states
    val genderIsExpanded = remember { mutableStateOf(false) }
    val lifestyleIsExpanded = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarStatus by remember { mutableStateOf("") }

    // Fetch profile when screen is opened
    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.fetchCurrentUserProfile()
    }

    // Update local states when profile state changes
    LaunchedEffect(profileState) {
        profileState?.let { profile ->
            name = profile.name ?: ""
            gender = profile.gender ?: ""
            lifestyle = profile.lifestyle ?: "Umum"
            profilePicture = profile.profilePicture ?: ""
        }
    }

    LaunchedEffect(name, gender, lifestyle, profilePicture) {
        viewModel.checkIfDataChanged(name, gender, lifestyle, profilePicture)
    }


    LaunchedEffect(updateStatus) {
        if (updateStatus == "success" || updateStatus == "failure") {
            showSnackbar = true
            snackbarStatus = updateStatus as String

            // Hide snackbar after 2 seconds
            launch {
                delay(2000)
                showSnackbar = false
                // Reset update status to prevent re-triggering
                viewModel.resetUpdateStatus()
            }
        }
    }

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
                        onBackClick()
                        navController.navigate(Screen.Profile.route)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                // Profile picture
                ProfilePicture(
                    viewModel = viewModel,
                    profilePictureUrl = profilePicture,
                    onProfilePictureClick = {
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)

                )

                // Name input
                CustomNameInput(
                    value = name,
                    onValueChange = { name = it },
                    isError = false,
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Gender dropdown
                CustomGenderDropdown(
                    selectedGender = gender,
                    isExpanded = genderIsExpanded.value,
                    onExpandedChange = { genderIsExpanded.value = !genderIsExpanded.value },
                    onGenderSelected = { gender = it },
                    onDismiss = { genderIsExpanded.value = false },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Lifestyle dropdown
                CustomDropdownLifestyle(
                    selectedOption = lifestyle,
                    isExpanded = lifestyleIsExpanded.value,
                    onExpandedChange = { lifestyleIsExpanded.value = !lifestyleIsExpanded.value },
                    onOptionSelected = { lifestyle = it },
                    onDismiss = { lifestyleIsExpanded.value = false }
                )
            }

            // Floating Snackbar
            if (updateStatus == "success" || updateStatus == "failure") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    AsyncImage(
                        model = if (updateStatus == "success") R.drawable.ic_snackbar_berhasil else R.drawable.ic_snackbar_gagal,
                        contentDescription = if (updateStatus == "success") "Berhasil Diperbarui" else "Gagal Diperbarui",
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .height(48.dp) // Adjust height as needed
                    )
                }
            }

            // Save button at the bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
            ) {
                ToggleGreenButton(
                    text = "Simpan",
                    enabled = isDataChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    onClick = {
                        viewModel.updateProfile(name, gender, lifestyle, profilePicture)
                    },
                )
            }
        }
    }
}

@Composable
fun ProfilePicture(
    viewModel: EditProfileViewModel,
    profilePictureUrl: String,
    onProfilePictureClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(128.dp)
            .clip(CircleShape)
            .clickable { onProfilePictureClick() },
        contentAlignment = Alignment.Center
    ) {
        // Display profile picture
        if (profilePictureUrl.isNotEmpty()) {
            AsyncImage(
                model = profilePictureUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .fillMaxSize()  // Ensures the image fills the circular container
                    .clip(CircleShape)  // Ensures the image is clipped to a circle
            )
        } else {
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .background(Color.Gray) // Default placeholder color if no image
            )
        }

        // Edit icon overlay
        Icon(
            painter = painterResource(id = R.drawable.ic_pen),
            contentDescription = "Edit Profile Picture",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (-15).dp, y = (-15).dp)
                .zIndex(Float.MAX_VALUE)
        )
    }
}


@Preview
@Composable
fun EditPreview() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid ?: ""
    val navController = rememberNavController()

    val apiService = ApiConfig.getProfileApiService()

    val viewModel = EditProfileViewModel(apiService = apiService, uid = uid)

    EditProfileScreen(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        navController = navController
    )
}
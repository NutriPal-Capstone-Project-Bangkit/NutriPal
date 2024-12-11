@file:Suppress("DEPRECATION")

package com.example.nutripal.ui.screen.profile.edit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.example.nutripal.R
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.auth.ToggleGreenButton
import com.example.nutripal.ui.custom.personaldetails.CustomAgeInput
import com.example.nutripal.ui.custom.personaldetails.CustomGenderDropdown
import com.example.nutripal.ui.custom.personaldetails.CustomNameInput
import com.example.nutripal.ui.custom.personaldetails.HeightInput
import com.example.nutripal.ui.custom.personaldetails.WeightInput
import com.example.nutripal.ui.custom.profile.CustomDropdownActivityLevel
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
    val profileState by viewModel.profileState.collectAsState()
    val updateStatus by viewModel.updateStatus.collectAsState()

    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var activityLevel by remember { mutableStateOf("") }
    var profilePicture by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    val isDataChanged by viewModel.isDataChanged.collectAsState()

    val genderIsExpanded = remember { mutableStateOf(false) }
    val activityLevelIsExpanded = remember { mutableStateOf(false) }

    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarStatus by remember { mutableStateOf("") }

    val temporaryProfilePicture by viewModel.temporaryProfilePicture.collectAsState()

    // Add a state to track loading of profile picture
    var isProfilePictureLoading by remember { mutableStateOf(false) }

    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            result.uriContent?.let { croppedUri ->
                // Set loading to true when uploading
                isProfilePictureLoading = true
                // Upload cropped image to Firebase Storage
                viewModel.uploadProfilePicture(croppedUri)
            }
        }
    }

    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Launch image cropper
            val cropOptions = CropImageContractOptions(
                uri,
                CropImageOptions(
                    guidelines = CropImageView.Guidelines.ON,
                    aspectRatioX = 1,
                    aspectRatioY = 1,
                    cropShape = CropImageView.CropShape.OVAL
                )
            )
            cropImageLauncher.launch(cropOptions)
        }
    }

    // Listen for temporary profile picture changes to manage loading state
    LaunchedEffect(temporaryProfilePicture) {
        if (temporaryProfilePicture != null) {
            isProfilePictureLoading = false
        }
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.fetchCurrentUserProfile()
    }

    // Update local states when profile state changes
    LaunchedEffect(profileState) {
        profileState?.let { profile ->
            name = profile.name ?: ""
            gender = profile.gender ?: ""
            activityLevel = profile.activityLevel ?: "Santai"
            profilePicture = profile.profilePicture ?: ""
            age = profile.age
            weight = profile.weight
            height = profile.height
        }
    }

    LaunchedEffect(name, gender, activityLevel, profilePicture, temporaryProfilePicture, age, weight, height) {
        viewModel.checkIfDataChanged(
            name,
            gender,
            activityLevel,
            profilePicture,
            temporaryProfilePicture,
            age,
            weight,
            height
        )
    }

    LaunchedEffect(updateStatus) {
        if (updateStatus == "success" || updateStatus == "failure") {
            showSnackbar = true
            snackbarStatus = updateStatus as String

            // Hide snackbar after 2 seconds
            launch {
                delay(2000)
                showSnackbar = false
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
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                // Profile picture
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                ) {
                    ProfilePicture(
                        profilePictureUrl = temporaryProfilePicture ?: profilePicture,
                        onProfilePictureClick = {
                            // Open photo picker
                            photoPicker.launch("image/*")
                        },
                        modifier = Modifier
                            .size(128.dp)
                    )

                    // Add loading indicator
                    if (isProfilePictureLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(128.dp)
                                .align(Alignment.Center),
                            color = Primary
                        )
                    }
                }

                // Name input
                CustomNameInput(
                    value = name,
                    onValueChange = { name = it },
                    isError = false,
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.height(16.dp))

                //age
                CustomAgeInput(
                    value = age,
                    onValueChange = {age = it},
                    isError = false,
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    WeightInput(
                        modifier = Modifier.weight(1f),
                        value = weight,
                        onValueChange = {weight = it},
                        isError = false
                    )
                    HeightInput(
                        modifier = Modifier.weight(1f),
                        value = height,
                        onValueChange = {height = it},
                        isError = false
                    )
                }

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

                // activityLevel dropdown
                CustomDropdownActivityLevel(
                    selectedOption = activityLevel,
                    isExpanded = activityLevelIsExpanded.value,
                    onExpandedChange = { activityLevelIsExpanded.value = !activityLevelIsExpanded.value },
                    onOptionSelected = { activityLevel = it },
                    onDismiss = { activityLevelIsExpanded.value = false }
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
                        // If there's a temporary profile picture, save it first
                        temporaryProfilePicture?.let { tempUrl ->
                            viewModel.updateProfilePictureUrl(tempUrl)
                        }

                        // Proceed with other profile updates
                        viewModel.updateProfile(
                            name,
                            gender,
                            activityLevel,
                            profilePicture,
                            age,
                            weight,
                            height
                        )
                    }
                )
                LaunchedEffect(Unit) {
                    viewModel.resetTemporaryProfilePicture()
                }
            }
        }
    }
}

@Composable
fun ProfilePicture(
    profilePictureUrl: String,
    onProfilePictureClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
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
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
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

    val viewModel = EditProfileViewModel( uid = uid)

    EditProfileScreen(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        navController = navController
    )
}
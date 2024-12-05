package com.example.nutripal.ui.custom.personaldetails

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberImagePainter
import com.example.nutripal.R
import com.example.nutripal.ui.theme.Disabled
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.yalantis.ucrop.UCrop
import java.io.File

@Composable
fun CustomProfilePhotoSelector(
    onPhotoClick: () -> Unit = {},
    onPhotoSelected: (Uri) -> Unit,
    imageUri: Uri? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // UCrop result launcher
    val cropImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            resultUri?.let { croppedUri ->
                Log.d("ProfilePhotoSelector", "Cropped image URI: $croppedUri")
                onPhotoSelected(croppedUri)
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = result.data?.let { UCrop.getError(it) }
            cropError?.let {
                Log.e("ProfilePhotoSelector", "Crop error: ${it.message}")
            } ?: Log.e("ProfilePhotoSelector", "No crop error data available")
        }
    }

    // Image picker launcher
    val getContentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            // Start UCrop for image cropping
            val destinationUri = Uri.fromFile(createTemporaryFile(context))
            val uCropIntent = UCrop.of(selectedUri, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(1000, 1000)
                .getIntent(context)

            // Use cropImageLauncher to launch the cropping activity
            cropImageLauncher.launch(uCropIntent)
        }
    }

    Column {
        Text(
            text = "Foto Profil",
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(0.5.dp, color = Disabled, shape = RoundedCornerShape(8.dp))
                .clickable {
                    getContentLauncher.launch("image/*")
                },
            contentAlignment = Alignment.Center
        ) {
            // Display selected/cropped image or default upload icon
            if (imageUri != null) {
                Image(
                    painter = rememberImagePainter(imageUri),
                    contentDescription = "Selected Profile Photo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.upload_profile),
                    contentDescription = "Upload Profile Icon",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                        .zIndex(1f),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

fun createTemporaryFile(context: Context): File {
    val tempFile = File.createTempFile("temp", ".jpg", context.cacheDir)
    tempFile.deleteOnExit()
    return tempFile
}

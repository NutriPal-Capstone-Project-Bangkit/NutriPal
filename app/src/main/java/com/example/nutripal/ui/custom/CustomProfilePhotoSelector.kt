package com.example.nutripal.ui.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.nutripal.R
import com.example.nutripal.ui.theme.Disabled
import com.example.nutripal.ui.theme.NunitoFontFamily

@Composable
fun CustomProfilePhotoSelector(
    onPhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = "Foto Profile",
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = Color(0xFF000000),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .height(150.dp)
                .border(0.5.dp, color = Disabled, shape = MaterialTheme.shapes.medium)
                .clickable(onClick = onPhotoClick),
            contentAlignment = Alignment.Center
        ) {
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
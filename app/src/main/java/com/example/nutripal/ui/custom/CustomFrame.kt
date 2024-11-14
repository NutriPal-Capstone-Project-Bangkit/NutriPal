package com.example.nutripal.ui.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutripal.R
import com.example.nutripal.data.model.FrameData
import com.example.nutripal.ui.theme.Disabled
import com.example.nutripal.ui.theme.Primary

// Global state to manage the selected frame
@Composable
fun CustomFrame(
    image: Int?,
    title: String,
    subtitle: String,
    frameId: Int, // Unique identifier for each frame
    selectedFrameId: Int, // The ID of the currently selected frame
    onFrameSelected: (Int) -> Unit // Function to update the selected frame
) {
    val backgroundColor = Color.Transparent

    // Check if this frame is selected
    val isSelected = frameId == selectedFrameId

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (isSelected) 2.dp else 0.5.dp, // Make border thicker when selected
                color = if (isSelected) Primary else Disabled,  // Change color to Primary when selected
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                // When clicked, select this frame
                onFrameSelected(frameId)
            }
    ) {
        // Add the image, title, and subtitle
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start  // Menjadikan konten kiri
        ) {
            // Image (if provided)
            image?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }

            // Title and Subtitle
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                )
            }
        }
    }
}

@Composable
fun FrameSelector() {
    // State to keep track of which frame is selected
    val selectedFrameId = remember { mutableStateOf(-1) } // -1 means no frame selected

    // Example frames as a list of FrameData objects
    val frames = listOf(
        FrameData(R.drawable.ic_umum, "Umum", "Pengguna yang ingin menjaga kesehatan\nsecara keseluruhan tanpa tujuan khusus."),
        FrameData(R.drawable.ic_diet, "Diet", "Pengguna yang ingin menjaga berat badan\natau menjalankan pola makan seimbang."),
        FrameData(R.drawable.ic_atlet, "Atlet", "Pengguna yang aktif berolahraga dan\nmembutuhkan asupan gizi lebih.")
    )

    Column {
        // Render frames
        frames.forEachIndexed { index, frame ->
            CustomFrame(
                image = frame.image,
                title = frame.title,
                subtitle = frame.subtitle,
                frameId = index,
                selectedFrameId = selectedFrameId.value, // Pass the currently selected frame ID
                onFrameSelected = { selectedFrameId.value = it } // Update the selected frame
            )
            Spacer(modifier = Modifier.height(16.dp)) // Add space between frames
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomFramePreview() {
    FrameSelector()
}

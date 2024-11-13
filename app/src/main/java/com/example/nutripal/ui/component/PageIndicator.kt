@file:Suppress("FunctionName")

package com.example.nutripal.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutripal.ui.theme.Primary

@Composable
fun PageIndicator(
    currentPage: Int,
    pageCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(110.dp, 11.dp)
                    .background(
                        if (index == currentPage) Primary else Color(0xFFB2D3B2),
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}

@Suppress("FunctionName")
@Preview(showBackground = true)
@Composable
fun PageIndicatorPreview() {
    PageIndicator(currentPage = 0, pageCount = 2)
}

package com.example.nutripal.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nutripal.R
import com.example.nutripal.data.model.PageData
import com.example.nutripal.ui.component.PageIndicator
import com.example.nutripal.ui.custom.CustomNextButton
import com.example.nutripal.ui.theme.NunitoFontFamily
import com.example.nutripal.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel, navController: NavController) {
    val currentPage by viewModel.currentPage.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F3F4)),
        verticalArrangement = Arrangement.Top
    ) {
        PageIndicator(currentPage)

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPage(viewModel, viewModel.getPageData(page), page)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .offset(y = ((-70).dp))
        ) {
            CustomNextButton(
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage < pagerState.pageCount - 1) {
                            pagerState.scrollToPage(pagerState.currentPage + 1)
                        } else {
                            navController.navigate("login")
                        }
                    }
                }
            )
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.updatePage(pagerState.currentPage)
    }
}

@Composable
private fun OnboardingPage(viewModel: OnboardingViewModel, pageData: PageData, pageIndex: Int) {
    val imageRes = when (pageIndex) {
        1 -> R.drawable.onboarding_2
        2 -> R.drawable.onboarding_3
        else -> R.drawable.onboarding_1
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val annotatedTitle = viewModel.processTitleText(pageData, pageIndex)

        Text(
            text = annotatedTitle,
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = pageData.description,
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        )
    }
}
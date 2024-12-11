@file:Suppress("FunctionName")

package com.example.nutripal.ui.screen.onboarding

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.data.local.preferences.OnboardingPreferences
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.component.PageIndicator
import com.example.nutripal.ui.custom.onboarding.CustomNextButton
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel, navController: NavController, context: Context) {

    val onboardingPreferences = OnboardingPreferences(context)

    LaunchedEffect(Unit) {
        if (onboardingPreferences.isOnboardingCompleted()) {
            navController.navigate("login") {
                popUpTo("onboarding") { inclusive = true }
            }
        }
    }

    // Render the onboarding screen only if onboarding is not completed yet
    val shouldShowOnboarding by remember { mutableStateOf(!onboardingPreferences.isOnboardingCompleted()) }

    if (shouldShowOnboarding) {
        MainStatusBar()

        val pagerState = rememberPagerState(pageCount = { 3 })
        val scope = rememberCoroutineScope()

        val currentPage by viewModel.currentPage.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F3F4)),
            verticalArrangement = Arrangement.Top
        ) {
            PageIndicator(currentPage = currentPage, pageCount = 3)

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
                    .offset(y = (-70).dp)
            ) {
                CustomNextButton(
                    onClick = {
                        scope.launch {
                            if (pagerState.currentPage < pagerState.pageCount - 1) {
                                pagerState.scrollToPage(pagerState.currentPage + 1)
                            } else {
                                onboardingPreferences.setOnboardingCompleted()
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
}

@Preview(showBackground = true)
@Composable
fun PreviewOnboardingScreen() {
    val context = LocalContext.current
    val navController = rememberNavController()
    OnboardingScreen(viewModel = OnboardingViewModel(), navController = navController, context)
}

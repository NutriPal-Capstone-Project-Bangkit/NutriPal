    package com.example.nutripal.ui.screen.profile

    import android.content.Context
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxHeight
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.offset
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.automirrored.filled.ExitToApp
    import androidx.compose.material.icons.filled.Edit
    import androidx.compose.material.icons.filled.History
    import androidx.compose.material.icons.filled.Info
    import androidx.compose.material.icons.filled.Person
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.Card
    import androidx.compose.material3.CardDefaults
    import androidx.compose.material3.Icon
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.vector.ImageVector
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.compose.ui.zIndex
    import androidx.navigation.NavController
    import androidx.navigation.compose.rememberNavController
    import com.example.nutripal.R
    import com.example.nutripal.ui.component.home.HomeBottomNavigation
    import com.example.nutripal.ui.component.home.HomeStatusBar
    import com.example.nutripal.ui.component.home.ScannerButton
    import com.example.nutripal.ui.component.profile.ProfileHeader
    import com.example.nutripal.ui.navigation.Screen
    import com.example.nutripal.ui.theme.NunitoFontFamily
    import com.example.nutripal.ui.theme.Primary
    import com.google.firebase.auth.FirebaseAuth
    import dagger.hilt.android.qualifiers.ApplicationContext

    @Composable
    fun ProfileScreen(navController: NavController, @ApplicationContext context: Context) {
        val red = Color(0xFFE53935)
        val currentRoute by remember { mutableStateOf("profile") }
        val auth = FirebaseAuth.getInstance()

        HomeStatusBar()

        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)
                    .background(Primary)
                    .zIndex(0f)
            )

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
                    .verticalScroll(rememberScrollState())
                    .zIndex(1f)
            ) {
                // Header text
                ProfileHeader()

                // Profile Section with floating image
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                        .clip(RoundedCornerShape(12.dp))

                ) {
                    // Floating profile image
                    Box(
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )

                    // Profile info
                    Text(
                        text = "Bangkit People",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Text(
                        text = "m375232@gmail.com",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Menu items
                MenuListItem(
                    icon = Icons.Default.History,
                    title = "Riwayat Scan",
                    onClick = { navController.navigate("edit_profile") }
                )

                MenuListItem(
                    icon = Icons.Default.Edit,
                    title = "Edit Profile",
                    onClick = { /* Handle click */ }
                )

                MenuListItem(
                    icon = Icons.Default.Person,
                    title = "Pengaturan Akun",
                    onClick = { /* Handle click */ }
                )

                MenuListItem(
                    icon = Icons.Default.Info,
                    title = "Tentang Aplikasi",
                    onClick = { /* Handle click */ }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Logout button
                Button(
                    onClick = {
                        // Firebase sign-out
                        auth.signOut()

                        // Clear login data from SharedPreferences
                        val sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit().clear().apply()

                        // Navigasi ke halaman login dan hapus semua back stack
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) // Menghapus semua back stack
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = red),
                    shape = MaterialTheme.shapes.small
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Keluar",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Keluar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Scanner button
            ScannerButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 15.dp)
                    .zIndex(2f),
                onScanResult = { /* Handle scan result */ }
            )

            // Bottom navigation
            HomeBottomNavigation(
                currentRoute = currentRoute,
                navController = navController,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(1f)
            )
        }
    }

    @Composable
    fun MenuListItem(
        icon: ImageVector,
        title: String,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clickable { onClick() },
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Normal
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_continue),
                    contentDescription = "Arrow",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun HomeScreenPreview(){
        val navController = rememberNavController()
    val context = LocalContext.current
    ProfileScreen(navController, context)
    }
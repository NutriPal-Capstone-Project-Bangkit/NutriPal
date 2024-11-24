package com.example.nutripal.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.R
import com.example.nutripal.ui.component.MainStatusBar
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.util.chatbotFormatText
import com.example.nutripal.viewmodel.ChatViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel = viewModel(), navController: NavController) {
    val chatHistory by viewModel.chatHistory.collectAsState()
    var userInput by remember { mutableStateOf("") }

    MainStatusBar()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nutri AI", color = Primary)
                }
            },
            backgroundColor = Color.White,
            navigationIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        )

        // Scrollable Chat History
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.ic_nutriai),
                    contentDescription = "NutriPal AI",
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Transparent)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Display chat history
            items(chatHistory.size) { index ->
                val message = chatHistory[index]
                if (message.startsWith("User:")) {
                    UserChat(
                        text = message.removePrefix("User: "),
                        backgroundColor = Primary,
                        textColor = Color.White
                    )
                } else {
                    BotChat(
                        text = message.removePrefix("Bot: "),
                        backgroundColor = Color(0xFF52CC52).copy(alpha = 0.1f),
                        textColor = Color(0xFF52CC52)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Input chat area
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            TextField(
                value = userInput,
                onValueChange = { userInput = it },
                placeholder = { Text("Tanyakan sesuatu...") },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(32.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFF52CC52).copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (userInput.isNotEmpty()) {
                        viewModel.sendMessage(userInput)
                        userInput = ""
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "Send",
                    tint = Primary
                )
            }
        }
    }
}


@Composable
fun BotChat(text: String, backgroundColor: Color, textColor: Color) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .padding(8.dp)
                .wrapContentWidth()
                .wrapContentHeight()
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = chatbotFormatText(text),
                color = textColor,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun UserChat(text: String, backgroundColor: Color = Primary, textColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .padding(8.dp)
                .wrapContentWidth()
                .wrapContentHeight()
                .align(Alignment.CenterVertically)
        ) {
            Text(text = text, color = textColor, fontSize = 14.sp, lineHeight = 20.sp)
        }
    }
}

@Preview
@Composable
fun ChatbotScreenPreview(){
    val navController = rememberNavController()
    ChatScreen(viewModel(), navController)
}
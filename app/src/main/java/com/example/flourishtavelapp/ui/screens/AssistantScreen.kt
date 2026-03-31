package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*

// Data class for Chat History (Mock)
data class ChatMessage(val message: String, val time: String, val isUser: Boolean)

@Composable
fun AssistantScreen(
    modifier: Modifier = Modifier, 
    onBack: () -> Unit,
    onProfileClick: () -> Unit
) {
    // Chat States
    val chatMessages = remember { mutableStateListOf<ChatMessage>() }
    var inputText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    // Auto-scroll to bottom when list changes
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = DarkTextColor)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Trợ lý AI",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor
                )
            }
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onProfileClick() },
                shape = CircleShape,
                color = PrimaryGreen
            ) {
                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.padding(8.dp))
            }
        }

        // Scrollable Chat Area
        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Pre-existing messages
                AiChatBubble(
                    message = "Chào bạn! Tôi là hướng dẫn viên Flourish của bạn. Bạn đã sẵn sàng khám phá những viên ngọc ẩn giấu quanh Kyoto hôm nay chưa? 🍵",
                    time = "AI FL • VỪA XONG"
                )

                Spacer(modifier = Modifier.height(16.dp))

                UserChatBubble(
                    message = "Tôi đang tìm kiếm một nơi nào đó yên tĩnh. Có lẽ là một quán trà hoặc một địa điểm chụp ảnh đẹp mà không quá đông đúc.",
                    time = "BẠN • 2 PHÚT TRƯỚC"
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "Gợi ý cho tâm trạng của bạn",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                RecommendationCard(
                    title = "Phuket Thái Lan",
                    description = "Điểm đến ưu tiên hàng đầu cho một mùa hè mát mẻ đó chính là đi Biển, đi hít hà \"Flourish\", nạp lại năng lượng sau một quãng thời gian dài chống dịch và bận rộn với công việc. Trong đó, du lịch Phuket (du lịch Thái Lan) trở thành lựa chọn đầu tiên cho những ai yêu thích biển xanh, cát trắng, nắng vàng... Để Flourish đề cử cho bạn vài bãi biển đẹp tại Phuket vừa tránh nóng, vừa du lịch nghỉ dưỡng nào!.",
                    tag = "GẦN ĐÂY",
                    tagColor = Color(0xFF00BFA5),
                    imageResId = com.example.flourishtavelapp.R.drawable.phuket_bg
                )

                Spacer(modifier = Modifier.height(16.dp))

                RecommendationCard(
                    title = "Chùa Wat Rong Khun",
                    description = "Wat Rong Khun (tiếng Thái: วัดร่องขุ่น), còn được gọi là Chùa Trắng, là một ngôi chùa Phật giáo và là một trong những ngôi chùa dễ nhận biết nhất ở Pa O Don Chai, huyện Mueang, Chiang Rai, Thái Lan.",
                    tag = "ĐIỂM CHỤP ẢNH",
                    tagColor = Color(0xFFFF8A80),
                    imageResId = com.example.flourishtavelapp.R.drawable.awat_bg
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Render dynamic messages from list
                chatMessages.forEach { msg ->
                    if (msg.isUser) {
                        UserChatBubble(message = msg.message, time = msg.time)
                    } else {
                        AiChatBubble(message = msg.message, time = msg.time)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Quick Actions (always at bottom of messages)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionChip("VÂNG, LÀM ƠN")
                    QuickActionChip("GIÁ BAO NHIÊU?")
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Fixed Chat Input Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .imePadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Hỏi trợ lý AI của bạn...", fontSize = 14.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            chatMessages.add(ChatMessage(inputText, "BẠN • BÂY GIỜ", true))
                            val currentInput = inputText
                            inputText = ""
                            // Mock Response
                            chatMessages.add(ChatMessage("Rất vui được nói chuyện với bạn.", "AI FL • BÂY GIỜ", false))
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = PrimaryGreen)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun AiChatBubble(message: String, time: String) {
    Column(
        modifier = Modifier.fillMaxWidth(0.85f),
        horizontalAlignment = Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 24.dp, bottomEnd = 24.dp, bottomStart = 24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(20.dp),
                color = DarkTextColor,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = time,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = SecondaryTextColor.copy(alpha = 0.7f),
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun UserChatBubble(message: String, time: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 4.dp, bottomEnd = 24.dp, bottomStart = 24.dp),
            color = PrimaryGreen,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(20.dp),
                color = Color.White,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = time,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = SecondaryTextColor.copy(alpha = 0.7f),
            modifier = Modifier.padding(end = 4.dp)
        )
    }
}

@Composable
fun RecommendationCard(title: String, description: String, tag: String, tagColor: Color, imageResId: Int? = null) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                if (imageResId != null) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE0E0E0)))
                }
                
                Surface(
                    modifier = Modifier.padding(12.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = tagColor
                ) {
                    Text(
                        text = tag,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DarkTextColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = SecondaryTextColor,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun QuickActionChip(label: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = LightGreenBackground, // Light greenish gray
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = DarkTextColor
        )
    }
}

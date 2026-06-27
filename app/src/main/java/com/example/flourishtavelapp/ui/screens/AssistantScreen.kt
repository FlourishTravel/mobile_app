package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.ChatbotRequest
import com.example.flourishtravelapp.data.model.ChatbotTourCard
import com.example.flourishtravelapp.data.session.SessionManager
import com.example.flourishtravelapp.ui.theme.DarkTextColor
import com.example.flourishtravelapp.ui.theme.LightGreenBackground
import com.example.flourishtravelapp.ui.theme.NatureGreenBackground
import com.example.flourishtravelapp.ui.theme.PrimaryGreen
import com.example.flourishtravelapp.ui.theme.SecondaryTextColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

private const val FLORA_AVATAR_ASSET = "file:///android_asset/assets/Flora-AI.png"
private val FloraMessageIndent = 48.dp

private data class FloraChatMessage(
    val message: String,
    val time: String,
    val isUser: Boolean,
    val tours: List<ChatbotTourCard> = emptyList()
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AssistantScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onProfileClick: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()
    val sessionId = remember { "mobile-${UUID.randomUUID()}" }

    val chatMessages = remember {
        mutableStateListOf(
            FloraChatMessage(
                message = "Chào bạn, Flora đây! Mình sẽ đồng hành cùng bạn để chuyến đi thuận tiện, vui vẻ và phù hợp với sở thích của bạn hơn nhé.",
                time = "Flora • VỪA XONG",
                isUser = false
            )
        )
    }
    var inputText by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var lastState by remember { mutableStateOf<Map<String, Any>?>(null) }
    var quickReplies by remember { mutableStateOf<List<String>>(emptyList()) }
    val scrollState = rememberScrollState()

    fun sendChat(text: String) {
        if (text.isBlank() || loading) return
        quickReplies = emptyList()
        chatMessages.add(FloraChatMessage(text, "BẠN • BÂY GIỜ", true))
        inputText = ""
        loading = true
        scope.launch {
            try {
                val user = sessionManager.getUserInfo()
                val request = ChatbotRequest(
                    content = text,
                    sessionId = sessionId,
                    userId = user?.id,
                    state = lastState
                )
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.chatbotApiService.sendMessage(request)
                }
                if (response.isSuccessful) {
                    val body = response.body()
                    val chatData = body?.data
                    if (body?.success == true && chatData != null) {
                        lastState = chatData.state
                        quickReplies = chatData.quickReplies?.mapNotNull { qr ->
                            when {
                                !qr.payload.isNullOrBlank() -> qr.payload
                                qr.label.isNotBlank() -> qr.label
                                else -> null
                            }
                        } ?: emptyList()
                        chatMessages.add(
                            FloraChatMessage(
                                message = chatData.reply,
                                time = "Flora • BÂY GIỜ",
                                isUser = false,
                                tours = chatData.tours ?: emptyList()
                            )
                        )
                    } else {
                        chatMessages.add(
                            FloraChatMessage(body?.message ?: "Không nhận được phản hồi", "Flora", false)
                        )
                    }
                } else {
                    chatMessages.add(FloraChatMessage("Lỗi kết nối (${response.code()})", "Flora", false))
                }
            } catch (e: Exception) {
                chatMessages.add(FloraChatMessage("Lỗi: ${e.message ?: "mạng"}", "Flora", false))
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(chatMessages.size, loading, quickReplies.size) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkTextColor)
                }
                FloraAvatar(modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Flora AI",
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
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                for (msg in chatMessages) {
                    if (msg.isUser) {
                        UserChatBubble(message = msg.message, time = msg.time)
                    } else {
                        AiChatBubble(message = msg.message, time = msg.time)
                        for (tour in msg.tours) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(modifier = Modifier.padding(start = FloraMessageIndent)) {
                                ChatbotTourCardItem(tour = tour)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (loading) {
                    AiChatBubble(message = "Đang suy nghĩ...", time = "Flora")
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (quickReplies.isNotEmpty()) {
                    Box(modifier = Modifier.padding(start = FloraMessageIndent)) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (label in quickReplies.take(3)) {
                                QuickActionChip(label = label, onClick = { sendChat(label) })
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

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
                    placeholder = {
                        Text(
                            text = "Hỏi Flora về tour, lịch trình hoặc gợi ý chuyến đi...",
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    maxLines = 3,
                    enabled = !loading
                )
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    onClick = { sendChat(inputText.trim()) },
                    enabled = !loading && inputText.isNotBlank(),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = PrimaryGreen)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        }
    }
}

@Composable
private fun FloraAvatar(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(FLORA_AVATAR_ASSET)
                .crossfade(true)
                .build(),
            contentDescription = "Flora AI",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(LightGreenBackground),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ChatbotTourCardItem(tour: ChatbotTourCard) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            val imageUrl = tour.imageUrl
            if (!imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = tour.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = tour.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkTextColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${tour.durationDays} ngày • ${String.format("%,d", tour.price)}đ",
                    color = SecondaryTextColor,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun AiChatBubble(message: String, time: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        FloraAvatar(modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.widthIn(max = 280.dp)) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 24.dp,
                    bottomEnd = 24.dp,
                    bottomStart = 24.dp
                ),
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    color = DarkTextColor,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = time,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = SecondaryTextColor.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
private fun UserChatBubble(message: String, time: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 4.dp,
                bottomEnd = 24.dp,
                bottomStart = 24.dp
            ),
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
private fun QuickActionChip(label: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = LightGreenBackground,
        modifier = Modifier
            .padding(vertical = 2.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = DarkTextColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

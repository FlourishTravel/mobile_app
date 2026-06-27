package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.ChatMessageViewDto
import com.example.flourishtravelapp.data.model.SendChatMessageRequest
import com.example.flourishtravelapp.data.model.TourChatContextDto
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun BookingGroupChatScreen(
    bookingId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var context by remember(bookingId) { mutableStateOf<TourChatContextDto?>(null) }
    var messages by remember(bookingId) { mutableStateOf<List<ChatMessageViewDto>>(emptyList()) }
    var isLoading by remember(bookingId) { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var messageInput by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    fun refreshMessages() {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.chatApiService.getBookingChatMessages(bookingId)
                if (response.isSuccessful && response.body()?.success == true) {
                    messages = response.body()?.data.orEmpty()
                }
            } catch (_: Exception) {
            }
        }
    }

    LaunchedEffect(bookingId) {
        isLoading = true
        loadError = null
        try {
            val ctxResponse = RetrofitClient.chatApiService.getTourChatContext(bookingId)
            if (ctxResponse.isSuccessful && ctxResponse.body()?.success == true) {
                context = ctxResponse.body()?.data
            }
            val msgResponse = RetrofitClient.chatApiService.getBookingChatMessages(bookingId)
            if (msgResponse.isSuccessful && msgResponse.body()?.success == true) {
                messages = msgResponse.body()?.data.orEmpty()
            } else {
                loadError = msgResponse.body()?.message ?: "Không tải được tin nhắn"
            }
        } catch (e: Exception) {
            loadError = e.localizedMessage ?: "Lỗi kết nối"
        } finally {
            isLoading = false
        }
    }

    val canChat = context?.canChat == true

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
    ) {
        Surface(color = Color.White, shadowElevation = 2.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = context?.roomName ?: context?.tourTitle ?: "Chat đoàn",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = DarkTextColor
                    )
                    val subtitle = buildList {
                        context?.guideName?.takeIf { it.isNotBlank() }?.let { add("HDV: $it") }
                        context?.sessionStartDate?.let { add("Khởi hành: $it") }
                    }.joinToString(" • ")
                    if (subtitle.isNotBlank()) {
                        Text(subtitle, fontSize = 11.sp, color = SecondaryTextColor)
                    }
                }
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
            return
        }

        if (!canChat) {
            Box(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = context?.denyReason ?: loadError ?: "Chưa thể mở chat cho đơn này.",
                    color = SecondaryTextColor,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
            return
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Text(
                        "Chưa có tin nhắn. Hãy chào đoàn!",
                        color = SecondaryTextColor,
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
            items(messages, key = { it.id ?: it.hashCode() }) { message ->
                ChatBubble(message = message)
            }
        }

        Surface(color = Color.White, shadowElevation = 8.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageInput,
                    onValueChange = { messageInput = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Nhập tin nhắn...") },
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilledIconButton(
                    onClick = {
                        val text = messageInput.trim()
                        if (text.isBlank() || isSending) return@FilledIconButton
                        coroutineScope.launch {
                            isSending = true
                            try {
                                val response = RetrofitClient.chatApiService.sendBookingChatMessage(
                                    bookingId,
                                    SendChatMessageRequest(text)
                                )
                                if (response.isSuccessful && response.body()?.success == true) {
                                    messageInput = ""
                                    refreshMessages()
                                }
                            } finally {
                                isSending = false
                            }
                        }
                    },
                    enabled = messageInput.isNotBlank() && !isSending,
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = PrimaryGreen)
                ) {
                    if (isSending) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessageViewDto) {
    val isGuide = message.senderRole?.equals("TOUR_GUIDE", ignoreCase = true) == true
        || message.senderRole?.equals("ADMIN", ignoreCase = true) == true
    val bubbleColor = if (isGuide) Color(0xFFE8F5E9) else Color.White
    val align = if (isGuide) Alignment.Start else Alignment.End

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = align
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = message.senderName.orEmpty(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isGuide) PrimaryGreen else SecondaryTextColor
            )
            if (message.isPinned == true) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.PushPin, null, tint = Color(0xFFFFB300), modifier = Modifier.size(12.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = bubbleColor,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(message.content.orEmpty(), fontSize = 14.sp, color = DarkTextColor)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    formatChatTime(message.createdAt),
                    fontSize = 10.sp,
                    color = SecondaryTextColor
                )
            }
        }
    }
}

private fun formatChatTime(iso: String?): String {
    if (iso.isNullOrBlank()) return ""
    return try {
        val instant = Instant.parse(iso)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (_: Exception) {
        iso.take(16).replace('T', ' ')
    }
}

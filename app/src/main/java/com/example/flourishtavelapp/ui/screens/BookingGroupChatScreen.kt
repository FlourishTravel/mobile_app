package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.ChatMessageViewDto
import com.example.flourishtravelapp.data.model.SendChatMessageRequest
import com.example.flourishtravelapp.data.model.ToggleChatReactionRequest
import com.example.flourishtravelapp.data.model.TourChatContextDto
import com.example.flourishtravelapp.data.session.SessionManager
import com.example.flourishtravelapp.ui.components.ChatReactionPicker
import com.example.flourishtravelapp.ui.components.ChatSenderAvatar
import com.example.flourishtravelapp.ui.components.TourChatBubble
import com.example.flourishtravelapp.ui.components.TourChatComposer
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch

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
    var replyTo by remember { mutableStateOf<ChatMessageViewDto?>(null) }
    var actionMessage by remember { mutableStateOf<ChatMessageViewDto?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val contextAndroid = LocalContext.current
    val currentUserId = remember { SessionManager(contextAndroid).getUserInfo()?.id }

    fun upsertMessage(dto: ChatMessageViewDto?) {
        if (dto?.id == null) return
        messages = messages.map { if (it.id == dto.id) dto else it }.let { list ->
            if (list.any { it.id == dto.id }) list else list + dto
        }
    }

    fun reactTo(message: ChatMessageViewDto, emoji: String) {
        val id = message.id ?: return
        coroutineScope.launch {
            try {
                val response = RetrofitClient.chatApiService.toggleChatReaction(
                    id,
                    ToggleChatReactionRequest(emoji)
                )
                if (response.isSuccessful && response.body()?.success == true) {
                    upsertMessage(response.body()?.data)
                }
            } catch (_: Exception) {
            }
        }
    }

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
                ChatSenderAvatar(
                    name = context?.guideName ?: context?.tourTitle,
                    avatarUrl = context?.guideAvatarUrl,
                    size = 40.dp
                )
                Spacer(Modifier.width(10.dp))
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
                TourChatBubble(
                    message = message,
                    currentUserId = currentUserId,
                    onReply = { replyTo = it },
                    onReact = { msg, emoji -> reactTo(msg, emoji) },
                    onLongPress = { actionMessage = it }
                )
            }
        }

        Surface(color = Color.White, shadowElevation = 8.dp) {
            TourChatComposer(
                members = context?.members.orEmpty(),
                currentUserId = currentUserId,
                replyTo = replyTo,
                onClearReply = { replyTo = null },
                value = messageInput,
                onValueChange = { messageInput = it },
                sending = isSending,
                enabled = true,
                onSend = {
                    val text = messageInput.trim()
                    if (text.isBlank() || isSending) return@TourChatComposer
                    coroutineScope.launch {
                        isSending = true
                        try {
                            val response = RetrofitClient.chatApiService.sendBookingChatMessage(
                                bookingId,
                                SendChatMessageRequest(text, replyTo?.id)
                            )
                            if (response.isSuccessful && response.body()?.success == true) {
                                messageInput = ""
                                replyTo = null
                                upsertMessage(response.body()?.data)
                                refreshMessages()
                            }
                        } finally {
                            isSending = false
                        }
                    }
                }
            )
        }

        if (actionMessage != null) {
            AlertDialog(
                onDismissRequest = { actionMessage = null },
                title = { Text("Thả icon hoặc trả lời") },
                text = {
                    Column {
                        ChatReactionPicker(
                            onPick = { emoji ->
                                actionMessage?.let { reactTo(it, emoji) }
                                actionMessage = null
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                        TextButton(onClick = {
                            replyTo = actionMessage
                            actionMessage = null
                        }) {
                            Text("Trả lời tin nhắn này")
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { actionMessage = null }) { Text("Đóng") }
                }
            )
        }
    }
}

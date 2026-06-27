package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.mapper.toGuestSessionData
import com.example.flourishtravelapp.data.model.ChatMessageViewDto
import com.example.flourishtravelapp.data.model.GuideSessionSummaryDto
import com.example.flourishtravelapp.data.model.SendChatMessageRequest
import com.example.flourishtravelapp.data.model.TourChatContextDto
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideCommunicationHubScreen(
    modifier: Modifier = Modifier,
    onOpenFullScreenChat: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var sessions by remember { mutableStateOf<List<GuideSessionSummaryDto>>(emptyList()) }
    var sessionId by remember { mutableStateOf("") }
    var bookings by remember { mutableStateOf<List<GuestBooking>>(emptyList()) }
    var bookingId by remember { mutableStateOf("") }
    var context by remember { mutableStateOf<TourChatContextDto?>(null) }
    var messages by remember { mutableStateOf<List<ChatMessageViewDto>>(emptyList()) }
    var loadingSessions by remember { mutableStateOf(true) }
    var loadingGuests by remember { mutableStateOf(false) }
    var chatLoading by remember { mutableStateOf(false) }
    var chatError by remember { mutableStateOf<String?>(null) }
    var messageInput by remember { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    fun loadChat() {
        if (bookingId.isBlank()) return
        scope.launch {
            chatLoading = true
            chatError = null
            try {
                val ctxResponse = RetrofitClient.chatApiService.getTourChatContext(bookingId)
                if (ctxResponse.isSuccessful && ctxResponse.body()?.success == true) {
                    context = ctxResponse.body()?.data
                }
                val msgResponse = RetrofitClient.chatApiService.getBookingChatMessages(bookingId)
                if (msgResponse.isSuccessful && msgResponse.body()?.success == true) {
                    messages = msgResponse.body()?.data.orEmpty()
                } else {
                    chatError = msgResponse.body()?.message ?: "Không tải được tin nhắn"
                }
            } catch (e: Exception) {
                chatError = e.localizedMessage ?: "Lỗi kết nối"
            } finally {
                chatLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadingSessions = true
        try {
            val response = RetrofitClient.guideApiService.getSessions()
            if (response.isSuccessful && response.body()?.success == true) {
                sessions = response.body()?.data.orEmpty()
                if (sessionId.isBlank()) sessionId = sessions.firstOrNull()?.sessionId.orEmpty()
            }
        } finally {
            loadingSessions = false
        }
    }

    LaunchedEffect(sessionId) {
        if (sessionId.isBlank()) return@LaunchedEffect
        loadingGuests = true
        bookingId = ""
        try {
            val response = RetrofitClient.guideApiService.getSessionGuests(sessionId)
            if (response.isSuccessful && response.body()?.success == true) {
                bookings = response.body()?.data?.toGuestSessionData()?.bookings.orEmpty()
                bookingId = bookings.firstOrNull()?.bookingId.orEmpty()
            } else {
                bookings = emptyList()
            }
        } catch (_: Exception) {
            bookings = emptyList()
        } finally {
            loadingGuests = false
        }
    }

    LaunchedEffect(bookingId) {
        if (bookingId.isNotBlank()) loadChat()
    }

    val canChat = context?.canChat == true
    val selectedBooking = bookings.find { it.bookingId == bookingId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        GuideScreenTitle(
            title = "Giao tiếp đoàn",
            subtitle = "Chat theo từng booking — đồng bộ với app khách."
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (loadingSessions) {
            GuideLoadingBox()
            return@Column
        }
        if (sessions.isEmpty()) {
            Text("Chưa có tour.", color = SecondaryTextColor)
            return@Column
        }

        GuideSessionPicker(sessions, sessionId, onSessionSelected = { sessionId = it })
        Spacer(modifier = Modifier.height(12.dp))

        if (loadingGuests) {
            GuideLoadingBox()
        } else if (bookings.isEmpty()) {
            Text("Chưa có booking trên chuyến này.", color = SecondaryTextColor)
        } else {
            Text("Chọn đơn", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = DarkTextColor)
            Spacer(modifier = Modifier.height(8.dp))
            bookings.forEach { b ->
                val selected = b.bookingId == bookingId
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { bookingId = b.bookingId },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected) LightGreenBackground else Color.White
                    ),
                    border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, PrimaryGreen) else null
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(b.travelerName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(
                            "${if (b.guestCount > 1) "${b.guestCount} khách · " else ""}${b.phone}",
                            fontSize = 12.sp,
                            color = SecondaryTextColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    selectedBooking?.travelerName ?: "Chat",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                TextButton(onClick = { if (bookingId.isNotBlank()) onOpenFullScreenChat(bookingId) }) {
                    Text("Toàn màn hình", fontSize = 12.sp)
                }
            }

            Card(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                if (chatLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryGreen)
                    }
                } else {
                    Column(Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            chatError?.let { item { Text(it, color = Color(0xFFC62828), fontSize = 12.sp) } }
                            if (context != null && !canChat) {
                                item {
                                    Text(
                                        context?.denyReason ?: "Chưa thể chat.",
                                        color = SecondaryTextColor,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            if (messages.isEmpty() && canChat) {
                                item {
                                    Text(
                                        "Chưa có tin nhắn — gửi lời chào đoàn!",
                                        color = SecondaryTextColor,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            items(messages, key = { it.id ?: it.hashCode() }) { msg ->
                                HubChatBubble(msg)
                            }
                        }
                        if (canChat) {
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
                                    placeholder = { Text("Nhắn tin đoàn...") },
                                    shape = RoundedCornerShape(24.dp),
                                    maxLines = 3
                                )
                                Spacer(Modifier.width(8.dp))
                                FilledIconButton(
                                    onClick = {
                                        val text = messageInput.trim()
                                        if (text.isBlank() || sending) return@FilledIconButton
                                        scope.launch {
                                            sending = true
                                            try {
                                                val response = RetrofitClient.chatApiService.sendBookingChatMessage(
                                                    bookingId,
                                                    SendChatMessageRequest(text)
                                                )
                                                if (response.isSuccessful && response.body()?.success == true) {
                                                    messageInput = ""
                                                    loadChat()
                                                }
                                            } finally {
                                                sending = false
                                            }
                                        }
                                    },
                                    enabled = messageInput.isNotBlank() && !sending,
                                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = PrimaryGreen)
                                ) {
                                    if (sending) {
                                        CircularProgressIndicator(Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                                    } else {
                                        Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun HubChatBubble(message: ChatMessageViewDto) {
    val isGuide = message.senderRole?.equals("TOUR_GUIDE", ignoreCase = true) == true
        || message.senderRole?.equals("ADMIN", ignoreCase = true) == true
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isGuide) Alignment.Start else Alignment.End
    ) {
        Text(
            message.senderName.orEmpty(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isGuide) PrimaryGreen else SecondaryTextColor
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isGuide) Color(0xFFE8F5E9) else Color(0xFFF5F5F5)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(message.content.orEmpty(), fontSize = 13.sp)
                Text(formatHubChatTime(message.createdAt), fontSize = 9.sp, color = SecondaryTextColor)
            }
        }
    }
}

private fun formatHubChatTime(iso: String?): String {
    if (iso.isNullOrBlank()) return ""
    return try {
        DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault())
            .format(Instant.parse(iso))
    } catch (_: Exception) {
        iso.take(16)
    }
}

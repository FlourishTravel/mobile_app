package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.NotificationDto
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var notifications by remember { mutableStateOf<List<NotificationDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var unreadOnly by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun loadNotifications() {
        coroutineScope.launch {
            isLoading = true
            loadError = null
            try {
                val response = RetrofitClient.notificationApiService.getNotifications(
                    unreadOnly = unreadOnly.takeIf { it },
                    limit = 50
                )
                if (response.isSuccessful && response.body()?.success == true) {
                    notifications = response.body()?.data?.content.orEmpty()
                } else {
                    loadError = response.body()?.message ?: "Không tải được thông báo"
                }
            } catch (e: Exception) {
                loadError = e.localizedMessage ?: "Lỗi kết nối"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(unreadOnly) {
        loadNotifications()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
    ) {
        Surface(color = Color.White) {
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
                Text(
                    "Thông báo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            RetrofitClient.notificationApiService.markAllRead()
                            loadNotifications()
                        }
                    }
                ) {
                    Icon(Icons.Default.DoneAll, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Đọc hết", fontSize = 12.sp)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = !unreadOnly,
                onClick = { unreadOnly = false },
                label = { Text("Tất cả") }
            )
            FilterChip(
                selected = unreadOnly,
                onClick = { unreadOnly = true },
                label = { Text("Chưa đọc") }
            )
        }

        when {
            isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
            loadError != null -> Box(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(loadError ?: "", color = Color(0xFFC62828))
            }
            notifications.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Không có thông báo.", color = SecondaryTextColor)
            }
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(notifications, key = { it.id ?: it.hashCode() }) { item ->
                    NotificationCard(
                        item = item,
                        onClick = {
                            val id = item.id ?: return@NotificationCard
                            coroutineScope.launch {
                                if (item.isRead != true) {
                                    RetrofitClient.notificationApiService.markRead(id)
                                }
                                loadNotifications()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(item: NotificationDto, onClick: () -> Unit) {
    val isUnread = item.isRead != true
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnread) Color.White else Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnread) 2.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (isUnread) LightGreenBackground else Color(0xFFECEFF1)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Notifications,
                        null,
                        tint = if (isUnread) PrimaryGreen else SecondaryTextColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.title.orEmpty(),
                    fontWeight = if (isUnread) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                item.body?.takeIf { it.isNotBlank() }?.let { body ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(body, fontSize = 12.sp, color = SecondaryTextColor, maxLines = 3, overflow = TextOverflow.Ellipsis)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    formatNotificationTime(item.createdAt),
                    fontSize = 10.sp,
                    color = SecondaryTextColor
                )
            }
        }
    }
}

private fun formatNotificationTime(iso: String?): String {
    if (iso.isNullOrBlank()) return ""
    return try {
        val instant = Instant.parse(iso)
        val formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm")
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (_: Exception) {
        iso.take(16).replace('T', ' ')
    }
}

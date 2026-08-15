package com.example.flourishtravelapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.model.ChatMemberViewDto
import com.example.flourishtravelapp.data.model.ChatMessageViewDto
import com.example.flourishtravelapp.ui.theme.PrimaryGreen
import com.example.flourishtravelapp.ui.theme.SecondaryTextColor

fun mentionTagFor(member: ChatMemberViewDto): String {
    if (member.flora || member.role.equals("FLORA", ignoreCase = true)) return "@Flora"
    val name = member.fullName?.trim().orEmpty()
    return if (name.isNotEmpty()) "@$name" else "@Thành viên"
}

fun memberRoleLabel(member: ChatMemberViewDto): String {
    if (member.flora || member.role.equals("FLORA", ignoreCase = true)) return "Trợ lý AI"
    return when (member.role?.uppercase()) {
        "TOUR_GUIDE" -> "Hướng dẫn viên"
        "ADMIN" -> "Quản trị"
        else -> "Thành viên đoàn"
    }
}

fun mentionQueryAtEnd(text: String): String? {
    val at = text.lastIndexOf('@')
    if (at < 0) return null
    if (at > 0 && !text[at - 1].isWhitespace()) return null
    val query = text.substring(at + 1)
    if (query.any { it.isWhitespace() || it == '\n' }) return null
    return query
}

fun applyMentionAtEnd(text: String, member: ChatMemberViewDto): String {
    val at = text.lastIndexOf('@')
    val prefix = if (at >= 0) text.substring(0, at) else text
    return prefix + mentionTagFor(member) + " "
}

@Composable
fun TourChatComposer(
    members: List<ChatMemberViewDto>,
    currentUserId: String?,
    replyTo: ChatMessageViewDto?,
    onClearReply: () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    sending: Boolean,
    enabled: Boolean,
    placeholder: String = "Nhắn đoàn, nhấn @ để gắn tên..."
) {
    val query = mentionQueryAtEnd(value)
    val suggestions = remember(members, query, currentUserId, value) {
        if (query == null) emptyList()
        else {
            val q = query.lowercase()
            members.filter { member ->
                val id = member.userId
                if (id.isNullOrBlank()) return@filter false
                if (!currentUserId.isNullOrBlank() && id == currentUserId && !member.flora) return@filter false
                if (q.isBlank()) return@filter true
                val name = member.fullName.orEmpty().lowercase()
                name.contains(q) || memberRoleLabel(member).lowercase().contains(q) || (member.flora && "flora ai".contains(q))
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (suggestions.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp)
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
            ) {
                suggestions.forEach { member ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onValueChange(applyMentionAtEnd(value, member)) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ChatSenderAvatar(name = member.fullName, avatarUrl = member.avatarUrl, size = 32.dp)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(member.fullName ?: "Thành viên", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(memberRoleLabel(member), fontSize = 11.sp, color = SecondaryTextColor)
                        }
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
        }
        if (replyTo != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF0FDF4))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Trả lời ${chatSenderLabel(replyTo, currentUserId)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                    Text(
                        replyTo.content.orEmpty(),
                        fontSize = 12.sp,
                        color = SecondaryTextColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                androidx.compose.material3.IconButton(onClick = onClearReply, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Hủy trả lời", tint = SecondaryTextColor)
                }
            }
            Spacer(Modifier.height(6.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text(placeholder) },
                shape = RoundedCornerShape(24.dp),
                maxLines = 3,
                enabled = enabled && !sending
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilledIconButton(
                onClick = onSend,
                enabled = value.isNotBlank() && enabled && !sending,
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = PrimaryGreen)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Gửi", tint = Color.White)
            }
        }
    }
}

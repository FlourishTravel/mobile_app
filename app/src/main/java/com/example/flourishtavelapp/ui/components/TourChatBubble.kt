package com.example.flourishtravelapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flourishtravelapp.data.model.ChatMessageViewDto
import com.example.flourishtravelapp.data.util.resolveMediaUrl
import com.example.flourishtravelapp.ui.theme.DarkTextColor
import com.example.flourishtravelapp.ui.theme.PrimaryGreen
import com.example.flourishtravelapp.ui.theme.SecondaryTextColor
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

val CHAT_REACTION_EMOJIS = listOf("👍", "❤️", "😂", "😮", "😢", "😡")

@Composable
fun ChatSenderAvatar(name: String?, avatarUrl: String?, size: androidx.compose.ui.unit.Dp = 36.dp) {
    val src = resolveMediaUrl(avatarUrl)
    val initial = name?.trim()?.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(0xFFE5E7EB)),
        contentAlignment = Alignment.Center
    ) {
        if (!src.isNullOrBlank()) {
            AsyncImage(
                model = src,
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(initial, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF374151))
        }
    }
}

fun chatSenderLabel(message: ChatMessageViewDto, currentUserId: String?): String {
    val mine = !currentUserId.isNullOrBlank() && currentUserId == message.senderId
    if (mine) return "Bạn"
    val role = message.senderRole.orEmpty().uppercase()
    val name = message.senderName?.trim().orEmpty()
    return when {
        role == "FLORA" -> "Flora"
        role == "TOUR_GUIDE" -> if (name.isNotEmpty()) "$name (HDV)" else "Hướng dẫn viên"
        role == "ADMIN" -> name.ifEmpty { "Quản trị" }
        name.isNotEmpty() -> name
        else -> "Thành viên"
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TourChatBubble(
    message: ChatMessageViewDto,
    currentUserId: String?,
    compact: Boolean = false,
    onReply: ((ChatMessageViewDto) -> Unit)? = null,
    onReact: ((ChatMessageViewDto, String) -> Unit)? = null,
    onLongPress: ((ChatMessageViewDto) -> Unit)? = null
) {
    val isMine = !currentUserId.isNullOrBlank() && currentUserId == message.senderId
    val isFlora = message.senderRole.equals("FLORA", ignoreCase = true)
    val isGuide = message.senderRole.equals("TOUR_GUIDE", ignoreCase = true)
        || message.senderRole.equals("ADMIN", ignoreCase = true)
    val bubbleColor = when {
        isMine -> PrimaryGreen
        isFlora -> Color(0xFFECFDF5)
        isGuide -> Color(0xFFE8F5E9)
        else -> Color.White
    }
    val nameColor = when {
        isMine -> Color.White.copy(alpha = 0.9f)
        isFlora -> PrimaryGreen
        isGuide -> PrimaryGreen
        else -> SecondaryTextColor
    }
    val textColor = if (isMine) Color.White else DarkTextColor
    val avatarSize = if (compact) 28.dp else 36.dp
    val label = chatSenderLabel(message, currentUserId)
    val reactions = message.reactions.orEmpty()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMine) {
            ChatSenderAvatar(name = message.senderName, avatarUrl = message.senderAvatarUrl, size = avatarSize)
            Spacer(Modifier.width(8.dp))
        }
        Column(
            modifier = Modifier.widthIn(max = 280.dp),
            horizontalAlignment = if (isMine) Alignment.End else Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = nameColor)
                if (message.isPinned == true) {
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.Default.PushPin, null, tint = Color(0xFFFFB300), modifier = Modifier.size(12.dp))
                }
            }
            Spacer(Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = bubbleColor,
                border = if (isMine) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.combinedClickable(
                    onClick = { onReply?.let { /* tap keeps bubble selectable via long-press */ } },
                    onLongClick = { onLongPress?.invoke(message) }
                )
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                    val quote = message.replyTo
                    if (quote != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isMine) Color.White.copy(alpha = 0.18f) else Color(0x14059669))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                quote.senderName ?: "Tin nhắn",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Text(
                                quote.content.orEmpty(),
                                fontSize = 12.sp,
                                color = textColor.copy(alpha = 0.85f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                    }
                    Text(message.content.orEmpty(), fontSize = if (compact) 13.sp else 14.sp, color = textColor)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        formatTourChatTime(message.createdAt),
                        fontSize = 10.sp,
                        color = if (isMine) Color.White.copy(alpha = 0.75f) else SecondaryTextColor
                    )
                }
            }
            if (reactions.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    reactions.forEach { reaction ->
                        val mineReact = reaction.reactedByMe
                        Text(
                            text = buildString {
                                append(reaction.type.orEmpty())
                                if (reaction.count > 1) {
                                    append(" ")
                                    append(reaction.count)
                                }
                            },
                            fontSize = 12.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (mineReact) Color(0xFFECFDF5) else Color.White)
                                .border(
                                    1.dp,
                                    if (mineReact) PrimaryGreen else Color(0xFFE5E7EB),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { reaction.type?.let { onReact?.invoke(message, it) } }
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
        if (isMine) {
            Spacer(Modifier.width(8.dp))
            ChatSenderAvatar(name = message.senderName, avatarUrl = message.senderAvatarUrl, size = avatarSize)
        }
    }
}

@Composable
fun ChatReactionPicker(
    onPick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(24.dp))
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        CHAT_REACTION_EMOJIS.forEach { emoji ->
            Text(
                text = emoji,
                fontSize = 20.sp,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onPick(emoji) }
                    .padding(6.dp)
            )
        }
    }
}

private fun formatTourChatTime(iso: String?): String {
    if (iso.isNullOrBlank()) return ""
    return try {
        DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault())
            .format(Instant.parse(iso))
    } catch (_: Exception) {
        iso.take(16).replace('T', ' ')
    }
}

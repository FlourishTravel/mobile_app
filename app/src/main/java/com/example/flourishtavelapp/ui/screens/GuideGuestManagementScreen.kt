package com.example.flourishtravelapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.flourishtravelapp.data.mapper.attendanceAtActivity
import com.example.flourishtravelapp.data.mapper.formatDt
import com.example.flourishtravelapp.data.mapper.toGuestSessionData
import com.example.flourishtravelapp.data.model.GuideCheckinRequest
import com.example.flourishtravelapp.data.model.GuideSessionSummaryDto
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideGuestManagementScreen(
    modifier: Modifier = Modifier,
    initialSessionId: String? = null,
    onOpenGroupChat: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var sessions by remember { mutableStateOf<List<GuideSessionSummaryDto>>(emptyList()) }
    var sessionId by remember { mutableStateOf("") }
    var guestData by remember { mutableStateOf<GuestSessionData?>(null) }
    var loadingSessions by remember { mutableStateOf(true) }
    var loadingGuests by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedStopId by remember { mutableStateOf<String?>(null) }
    var busyKey by remember { mutableStateOf<String?>(null) }

    fun reloadGuests() {
        if (sessionId.isBlank()) return
        scope.launch {
            loadingGuests = true
            error = null
            try {
                val response = RetrofitClient.guideApiService.getSessionGuests(sessionId)
                if (response.isSuccessful && response.body()?.success == true) {
                    guestData = response.body()?.data?.toGuestSessionData()
                } else {
                    guestData = null
                    error = response.body()?.message ?: "Không tải được danh sách khách"
                }
            } catch (e: Exception) {
                guestData = null
                error = e.localizedMessage ?: "Lỗi kết nối"
            } finally {
                loadingGuests = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadingSessions = true
        try {
            val response = RetrofitClient.guideApiService.getSessions()
            if (response.isSuccessful && response.body()?.success == true) {
                sessions = response.body()?.data.orEmpty()
                val preselect = initialSessionId?.takeIf { id ->
                    sessions.any { it.sessionId == id }
                }
                sessionId = preselect ?: sessions.firstOrNull()?.sessionId.orEmpty()
            }
        } catch (e: Exception) {
            error = e.localizedMessage
        } finally {
            loadingSessions = false
        }
    }

    LaunchedEffect(initialSessionId, sessions) {
        if (!initialSessionId.isNullOrBlank() && sessions.any { it.sessionId == initialSessionId }) {
            sessionId = initialSessionId
        }
    }

    LaunchedEffect(sessionId) {
        if (sessionId.isNotBlank()) reloadGuests()
    }

    val data = guestData
    val attendancePercent = if ((data?.totalGuestSlots ?: 0) > 0) {
        ((data!!.checkedInGuestSlots.toFloat() / data.totalGuestSlots) * 100).toInt().coerceIn(0, 100)
    } else 0

    val filteredBookings = remember(data, searchQuery) {
        val rows = data?.bookings.orEmpty()
        val q = searchQuery.trim().lowercase()
        if (q.isBlank()) rows
        else rows.filter { b ->
            listOf(b.travelerName, b.phone, b.email, b.pickupAddress, b.specialRequests)
                .joinToString(" ").lowercase().contains(q)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GuideScreenTitle(
                    title = "Quản lý khách",
                    subtitle = "Điểm danh tập trung, theo điểm dừng và từng người trong đoàn.",
                    modifier = Modifier.weight(1f)
                )
                FilledTonalButton(
                    onClick = {
                        Toast.makeText(context, "Quét QR điểm danh — đang phát triển", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(Icons.Default.QrCodeScanner, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Quét QR", fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (loadingSessions) {
                GuideLoadingBox()
                return@Column
            }
            if (sessions.isEmpty()) {
                Text("Chưa có chuyến tour.", color = SecondaryTextColor)
                return@Column
            }

            GuideSessionPicker(
                sessions = sessions,
                sessionId = sessionId,
                onSessionSelected = { sessionId = it }
            )
            Spacer(modifier = Modifier.height(12.dp))
            error?.let { GuideErrorText(it); Spacer(modifier = Modifier.height(8.dp)) }

            if (loadingGuests && data == null) {
                GuideLoadingBox()
            } else if (data != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(data.tourTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            "${data.checkedInGuestSlots}/${data.totalGuestSlots} đã check-in · ${data.checkedOutParticipants} đã trả",
                            fontSize = 12.sp,
                            color = SecondaryTextColor
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        GuideProgressBar(attendancePercent, "Tiến độ điểm danh")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Tìm tên, SĐT, điểm đón...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                if (data.itineraryStops.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Điểm dừng lịch trình", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(end = 4.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedStopId == null,
                                onClick = { selectedStopId = null },
                                label = { Text("Tất cả") }
                            )
                        }
                        items(data.itineraryStops, key = { it.activityId }) { stop ->
                            FilterChip(
                                selected = selectedStopId == stop.activityId,
                                onClick = {
                                    selectedStopId = if (selectedStopId == stop.activityId) null else stop.activityId
                                },
                                label = {
                                    Text(
                                        "${stop.title.take(12)}${if (stop.title.length > 12) "…" else ""} (${stop.checkedInAtStopCount})",
                                        fontSize = 11.sp
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        if (data != null && !loadingGuests) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
            ) {
                items(filteredBookings, key = { it.bookingId }) { booking ->
                    GuestBookingCard(
                        booking = booking,
                        sessionId = sessionId,
                        selectedStopId = selectedStopId,
                        busyKey = busyKey,
                        onBusy = { busyKey = it },
                        onDone = { busyKey = null; reloadGuests() },
                        onOpenChat = onOpenGroupChat
                    )
                }
            }
        } else if (loadingGuests) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        }
    }
}

@Composable
private fun GuestBookingCard(
    booking: GuestBooking,
    sessionId: String,
    selectedStopId: String?,
    busyKey: String?,
    onBusy: (String?) -> Unit,
    onDone: () -> Unit,
    onOpenChat: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = LightGreenBackground) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                booking.travelerName.take(1).uppercase(),
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreen
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(booking.travelerName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(booking.phone, fontSize = 12.sp, color = SecondaryTextColor)
                        if (booking.guestCount > 1) {
                            Text("${booking.guestCount} khách", fontSize = 11.sp, color = SecondaryTextColor)
                        }
                    }
                }
                if (booking.checkedInGathering) {
                    Icon(Icons.Default.CheckCircle, null, tint = PrimaryGreen, modifier = Modifier.size(22.dp))
                }
            }

            if (booking.pickupAddress.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Đón: ${booking.pickupAddress}", fontSize = 12.sp, color = SecondaryTextColor)
            }
            if (booking.specialRequests.isNotBlank()) {
                Text("Ghi chú: ${booking.specialRequests}", fontSize = 12.sp, color = Color(0xFFE65100))
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val gatheringBusy = busyKey == "g-${booking.bookingId}"
                if (!booking.checkedInGathering && !booking.travelerUserId.isNullOrBlank()) {
                    FilledTonalButton(
                        onClick = {
                            val uid = booking.travelerUserId ?: return@FilledTonalButton
                            onBusy("g-${booking.bookingId}")
                            scope.launch {
                                try {
                                    RetrofitClient.guideApiService.checkin(
                                        GuideCheckinRequest(sessionId, uid, "gathering")
                                    )
                                } finally {
                                    onDone()
                                }
                            }
                        },
                        enabled = !gatheringBusy,
                        modifier = Modifier.height(36.dp)
                    ) {
                        if (gatheringBusy) {
                            CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Check-in đơn", fontSize = 12.sp)
                        }
                    }
                }
                OutlinedButton(
                    onClick = { onOpenChat(booking.bookingId) },
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(Icons.Default.Chat, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Chat", fontSize = 12.sp)
                }
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Thu gọn" else "Chi tiết (${booking.participantAttendance.size})", fontSize = 12.sp)
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                booking.participantAttendance.forEach { p ->
                    ParticipantRow(
                        participant = p,
                        sessionId = sessionId,
                        selectedStopId = selectedStopId,
                        busyKey = busyKey,
                        onBusy = onBusy,
                        onDone = onDone
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ParticipantRow(
    participant: ParticipantAttendance,
    sessionId: String,
    selectedStopId: String?,
    busyKey: String?,
    onBusy: (String?) -> Unit,
    onDone: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pid = participant.participantId
    val pBusy = busyKey == "p-$pid"
    val hasCheckIn = participant.checkInAt != null
    val hasCheckOut = participant.checkOutAt != null

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAF9)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                "${participant.displayName} (${participant.role})",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
            if (participant.phone.isNotBlank()) {
                Text(participant.phone, fontSize = 11.sp, color = SecondaryTextColor)
            }
            participant.checkInAt?.let {
                Text("Check-in: ${formatDt(it)}", fontSize = 10.sp, color = PrimaryGreen)
            }
            participant.checkOutAt?.let {
                Text("Check-out: ${formatDt(it)}", fontSize = 10.sp, color = SecondaryTextColor)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 6.dp)) {
                if (!hasCheckIn) {
                    SmallActionButton("Điểm danh", pBusy) {
                        onBusy("p-$pid")
                        scope.launch {
                            try {
                                RetrofitClient.guideApiService.participantCheckIn(sessionId, pid)
                            } finally {
                                onDone()
                            }
                        }
                    }
                } else if (!hasCheckOut) {
                    SmallActionButton("Trả khách", pBusy) {
                        onBusy("p-$pid")
                        scope.launch {
                            try {
                                RetrofitClient.guideApiService.participantCheckOut(sessionId, pid)
                            } finally {
                                onDone()
                            }
                        }
                    }
                }
            }

            selectedStopId?.let { stopId ->
                val att = attendanceAtActivity(participant, stopId)
                val aKey = "a-$pid-$stopId"
                val aBusy = busyKey == aKey
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 4.dp)) {
                    if (att?.checkInAt == null) {
                        SmallActionButton("Tại điểm", aBusy) {
                            onBusy(aKey)
                            scope.launch {
                                try {
                                    RetrofitClient.guideApiService.participantActivityCheckIn(sessionId, pid, stopId)
                                } finally {
                                    onDone()
                                }
                            }
                        }
                    } else if (att.checkOutAt == null) {
                        SmallActionButton("Rời điểm", aBusy) {
                            onBusy(aKey)
                            scope.launch {
                                try {
                                    RetrofitClient.guideApiService.participantActivityCheckOut(sessionId, pid, stopId)
                                } finally {
                                    onDone()
                                }
                            }
                        }
                    } else {
                        Text("Đã hoàn thành điểm", fontSize = 10.sp, color = PrimaryGreen)
                    }
                }
            }
        }
    }
}

@Composable
private fun SmallActionButton(label: String, busy: Boolean, onClick: () -> Unit) {
    FilledTonalButton(onClick = onClick, enabled = !busy, modifier = Modifier.height(32.dp)) {
        if (busy) {
            CircularProgressIndicator(Modifier.size(14.dp), strokeWidth = 2.dp)
        } else {
            Text(label, fontSize = 11.sp)
        }
    }
}

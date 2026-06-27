package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.mapper.formatGuideDateRange
import com.example.flourishtravelapp.data.mapper.formatTimeHm
import com.example.flourishtravelapp.data.mapper.toGuestSessionData
import com.example.flourishtravelapp.data.model.GuideSessionSummaryDto
import com.example.flourishtravelapp.data.model.SessionScheduleActivityDto
import com.example.flourishtravelapp.data.model.SessionScheduleViewDto
import com.example.flourishtravelapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideOperationsScreen(
    modifier: Modifier = Modifier,
    onOpenGuestsTab: () -> Unit = {},
    onTourClick: (GuideTour) -> Unit = {}
) {
    var sessions by remember { mutableStateOf<List<GuideSessionSummaryDto>>(emptyList()) }
    var sessionId by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf<SessionScheduleViewDto?>(null) }
    var guestCount by remember { mutableIntStateOf(0) }
    var activeDay by remember { mutableIntStateOf(1) }
    var loadingSessions by remember { mutableStateOf(true) }
    var loadingSchedule by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val activeSessions = remember(sessions) {
        sessions.filter { it.status == "ongoing" || it.status == "upcoming" }
    }
    val displaySessions = if (activeSessions.isNotEmpty()) activeSessions else sessions
    val selected = displaySessions.find { it.sessionId == sessionId } ?: displaySessions.firstOrNull()

    LaunchedEffect(Unit) {
        loadingSessions = true
        try {
            val response = RetrofitClient.guideApiService.getSessions()
            if (response.isSuccessful && response.body()?.success == true) {
                sessions = response.body()?.data.orEmpty()
                val preferred = sessions.firstOrNull { it.status == "ongoing" }
                    ?: sessions.firstOrNull { it.status == "upcoming" }
                    ?: sessions.firstOrNull()
                sessionId = preferred?.sessionId.orEmpty()
            }
        } catch (e: Exception) {
            error = e.localizedMessage
        } finally {
            loadingSessions = false
        }
    }

    LaunchedEffect(sessionId) {
        if (sessionId.isBlank()) return@LaunchedEffect
        loadingSchedule = true
        error = null
        try {
            val schedResponse = RetrofitClient.guideApiService.getSessionSchedule(sessionId)
            if (schedResponse.isSuccessful && schedResponse.body()?.success == true) {
                schedule = schedResponse.body()?.data
                schedule?.days?.firstOrNull()?.dayNumber?.let { activeDay = it }
            } else {
                schedule = null
            }
            val guestResponse = RetrofitClient.guideApiService.getSessionGuests(sessionId)
            guestCount = if (guestResponse.isSuccessful && guestResponse.body()?.success == true) {
                guestResponse.body()?.data?.toGuestSessionData()?.totalGuestSlots ?: 0
            } else {
                selected?.currentParticipants ?: 0
            }
        } catch (e: Exception) {
            error = e.localizedMessage
            schedule = null
        } finally {
            loadingSchedule = false
        }
    }

    val dayRow = schedule?.days?.find { it.dayNumber == activeDay }
    val timeline = dayRow?.activities.orEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        GuideScreenTitle(
            title = "Vận hành: ${selected?.tourTitle ?: "Chọn tour"}",
            subtitle = selected?.let { formatGuideDateRange(it.startDate, it.endDate) }
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (loadingSessions) {
            GuideLoadingBox()
            return@Column
        }
        if (displaySessions.isEmpty()) {
            Text("Chưa có tour.", color = SecondaryTextColor)
            return@Column
        }

        if (displaySessions.size > 1) {
            GuideSessionPicker(
                sessions = displaySessions,
                sessionId = sessionId,
                onSessionSelected = { sessionId = it },
                label = "Chuyến đang vận hành"
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onOpenGuestsTab) {
                Icon(Icons.Default.Group, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Đoàn $guestCount khách")
            }
            selected?.let { s ->
                FilledTonalButton(onClick = {
                    onTourClick(
                        GuideTour(
                            sessionId = s.sessionId,
                            id = s.tourCode ?: s.sessionId.take(8),
                            name = s.tourTitle.orEmpty(),
                            destination = s.location.orEmpty(),
                            startDate = s.startDate.orEmpty(),
                            endDate = s.endDate.orEmpty(),
                            startDateIso = s.startDate,
                            endDateIso = s.endDate,
                            durationDays = 1,
                            totalCustomers = s.currentParticipants,
                            checkedInParticipants = s.checkedInParticipants,
                            status = com.example.flourishtravelapp.data.mapper.mapGuideStatus(s.status),
                            imageDescription = "",
                            meetingPoint = "",
                            itinerary = emptyList(),
                            customers = emptyList()
                        )
                    )
                }) {
                    Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Chi tiết tour")
                }
            }
        }

        error?.let { Spacer(Modifier.height(8.dp)); GuideErrorText(it) }
        Spacer(modifier = Modifier.height(16.dp))

        if (loadingSchedule) {
            GuideLoadingBox()
        } else if (schedule == null) {
            Text("Không tải được lịch vận hành.", color = SecondaryTextColor)
        } else {
            val days = schedule?.days.orEmpty()
            if (days.isNotEmpty()) {
                ScrollableTabRow(
                    selectedTabIndex = days.indexOfFirst { it.dayNumber == activeDay }.coerceAtLeast(0),
                    containerColor = Color.Transparent,
                    contentColor = PrimaryGreen,
                    edgePadding = 0.dp
                ) {
                    days.forEach { day ->
                        val dayNum = day.dayNumber ?: 1
                        Tab(
                            selected = activeDay == dayNum,
                            onClick = { activeDay = dayNum },
                            text = {
                                Text(
                                    "Ngày $dayNum",
                                    fontWeight = if (activeDay == dayNum) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (timeline.isEmpty()) {
                Text("Không có hoạt động trong ngày này.", color = SecondaryTextColor)
            } else {
                timeline.forEachIndexed { index, row ->
                    OperationTimelineItem(
                        row = row,
                        isFirst = index == 0,
                        isLast = index == timeline.lastIndex
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun OperationTimelineItem(
    row: SessionScheduleActivityDto,
    isFirst: Boolean,
    isLast: Boolean
) {
    val eff = row.effective
    val title = eff?.title ?: row.template?.title ?: "Hoạt động"
    val start = formatTimeHm(eff?.startTime ?: row.template?.startTime)
    val end = formatTimeHm(eff?.endTime ?: row.template?.endTime)
    val time = if (start.isNotBlank() && end.isNotBlank()) "$start – $end" else start.ifBlank { "—" }
    val location = eff?.locationName ?: row.template?.locationName.orEmpty()
    val cancelled = eff?.cancelled == true
    val status = eff?.scheduleStatus ?: row.template?.scheduleStatus

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(28.dp)
        ) {
            if (!isFirst) {
                Box(Modifier.width(2.dp).height(12.dp).background(Color(0xFFCCD5C7)))
            } else {
                Spacer(Modifier.height(12.dp))
            }
            Surface(
                modifier = Modifier.size(12.dp),
                shape = CircleShape,
                color = when {
                    cancelled -> Color(0xFFB0BEC5)
                    isFirst -> PrimaryGreen
                    else -> Color(0xFF81C784)
                }
            ) {}
            if (!isLast) {
                Box(Modifier.width(2.dp).height(48.dp).background(Color(0xFFCCD5C7)))
            }
        }
        Spacer(Modifier.width(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (cancelled) Color(0xFFF5F5F5) else Color.White
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(time, fontSize = 11.sp, color = PrimaryGreen, fontWeight = FontWeight.Bold)
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (cancelled) SecondaryTextColor else DarkTextColor
                )
                if (location.isNotBlank()) {
                    Text(location, fontSize = 12.sp, color = SecondaryTextColor)
                }
                row.override?.operationalNote?.takeIf { it.isNotBlank() }?.let {
                    Text("Ghi chú: $it", fontSize = 11.sp, color = Color(0xFFE65100))
                }
                status?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(it, fontSize = 10.sp, color = SecondaryTextColor)
                }
                if (cancelled) {
                    Text("Đã hủy", fontSize = 10.sp, color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

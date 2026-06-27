package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.mapper.formatTimeHm
import com.example.flourishtravelapp.data.mapper.toGuideTour
import com.example.flourishtravelapp.data.model.GuideCheckinRequest
import com.example.flourishtravelapp.data.model.SessionActivitySchedulePatchRequest
import com.example.flourishtravelapp.data.model.SessionScheduleActivityDto
import com.example.flourishtravelapp.data.model.SessionScheduleViewDto
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideTourDetailScreen(
    tour: GuideTour,
    onBack: () -> Unit,
    onCustomerListClick: () -> Unit,
    onOpenGroupChat: (String) -> Unit = {},
    onOpenOperations: () -> Unit = {},
    onOpenGuestsTab: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var detailTour by remember(tour.sessionId) { mutableStateOf(tour) }
    var sessionSchedule by remember(tour.sessionId) { mutableStateOf<SessionScheduleViewDto?>(null) }
    var isLoading by remember(tour.sessionId) { mutableStateOf(true) }
    var scheduleBusy by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var activeDay by remember { mutableIntStateOf(1) }
    var editActivity by remember { mutableStateOf<SessionScheduleActivityDto?>(null) }
    var editTitle by remember { mutableStateOf("") }
    var editLocation by remember { mutableStateOf("") }
    var editStartTime by remember { mutableStateOf("") }
    var editEndTime by remember { mutableStateOf("") }
    var editNote by remember { mutableStateOf("") }
    var editStatus by remember { mutableStateOf("CONFIRMED") }
    var editGathering by remember { mutableStateOf(false) }
    var checkInBusyId by remember { mutableStateOf<String?>(null) }

    fun reloadAll() {
        scope.launch {
            isLoading = true
            error = null
            try {
                val response = RetrofitClient.guideApiService.getSession(tour.sessionId)
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { detailTour = it.toGuideTour() }
                }
                val guestsResponse = RetrofitClient.guideApiService.getSessionGuests(tour.sessionId)
                if (guestsResponse.isSuccessful && guestsResponse.body()?.success == true) {
                    guestsResponse.body()?.data?.let { detailTour = it.toGuideTour(detailTour) }
                }
                val schedResponse = RetrofitClient.guideApiService.getSessionSchedule(tour.sessionId)
                if (schedResponse.isSuccessful && schedResponse.body()?.success == true) {
                    sessionSchedule = schedResponse.body()?.data
                    sessionSchedule?.days?.firstOrNull()?.dayNumber?.let { activeDay = it }
                }
            } catch (e: Exception) {
                error = e.localizedMessage
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(tour.sessionId) { reloadAll() }

    val displayTour = detailTour
    val chatBookingId = displayTour.customers.firstOrNull()?.id?.takeIf { it.isNotBlank() }
    val statusColor = Color(displayTour.status.color)
    val scheduleDay = sessionSchedule?.days?.find { it.dayNumber == activeDay }

    val expandedDays = remember(displayTour.sessionId, displayTour.itinerary) {
        mutableStateMapOf<Int, Boolean>().apply {
            displayTour.itinerary.forEach { day -> if (day.isCurrent) put(day.day, true) }
            if (isEmpty()) put(1, true)
        }
    }

    fun openEdit(row: SessionScheduleActivityDto) {
        val eff = row.effective
        editActivity = row
        editTitle = eff?.title ?: row.template?.title.orEmpty()
        editLocation = eff?.locationName ?: row.template?.locationName.orEmpty()
        editStartTime = formatTimeHm(eff?.startTime ?: row.template?.startTime)
        editEndTime = formatTimeHm(eff?.endTime ?: row.template?.endTime)
        editNote = row.override?.operationalNote.orEmpty()
        editStatus = eff?.scheduleStatus ?: row.template?.scheduleStatus ?: "CONFIRMED"
        editGathering = eff?.isGatheringEvent ?: row.template?.isGatheringEvent ?: false
    }

    fun buildPatchBody(): SessionActivitySchedulePatchRequest {
        val baseDate = displayTour.startDateIso?.take(10) ?: "2026-01-01"
        fun toOffset(timeStr: String): String? {
            if (timeStr.isBlank()) return null
            val parts = timeStr.split(":")
            if (parts.size < 2) return null
            return "${baseDate}T${parts[0].padStart(2, '0')}:${parts[1]}:00+07:00"
        }
        return SessionActivitySchedulePatchRequest(
            title = editTitle.ifBlank { null },
            locationName = editLocation.ifBlank { null },
            startAt = toOffset(editStartTime),
            endAt = toOffset(editEndTime),
            scheduleStatus = editStatus,
            operationalNote = editNote.ifBlank { null },
            isGatheringEvent = editGathering
        )
    }

    if (isLoading && displayTour.itinerary.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
        return
    }

    if (editActivity != null) {
        AlertDialog(
            onDismissRequest = { if (!scheduleBusy) editActivity = null },
            title = { Text("Chỉnh sửa hoạt động") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(editTitle, { editTitle = it }, label = { Text("Tiêu đề") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(editLocation, { editLocation = it }, label = { Text("Địa điểm") }, modifier = Modifier.fillMaxWidth())
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(editStartTime, { editStartTime = it }, label = { Text("Bắt đầu") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(editEndTime, { editEndTime = it }, label = { Text("Kết thúc") }, modifier = Modifier.weight(1f))
                    }
                    OutlinedTextField(editNote, { editNote = it }, label = { Text("Ghi chú vận hành") }, modifier = Modifier.fillMaxWidth())
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(editGathering, { editGathering = it })
                        Text("Sự kiện tập trung", fontSize = 13.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !scheduleBusy,
                    onClick = {
                        val actId = editActivity?.activityId ?: return@TextButton
                        scheduleBusy = true
                        scope.launch {
                            try {
                                val response = RetrofitClient.guideApiService.patchSessionActivitySchedule(
                                    tour.sessionId, actId, buildPatchBody()
                                )
                                if (response.isSuccessful && response.body()?.success == true) {
                                    sessionSchedule = response.body()?.data
                                    editActivity = null
                                } else {
                                    error = response.body()?.message ?: "Lưu thất bại"
                                }
                            } finally {
                                scheduleBusy = false
                            }
                        }
                    }
                ) { Text("Lưu nháp") }
            },
            dismissButton = {
                Row {
                    TextButton(
                        enabled = !scheduleBusy,
                        onClick = {
                            val actId = editActivity?.activityId ?: return@TextButton
                            scheduleBusy = true
                            scope.launch {
                                try {
                                    RetrofitClient.guideApiService.patchSessionActivitySchedule(
                                        tour.sessionId, actId, buildPatchBody()
                                    )
                                    val pub = RetrofitClient.guideApiService.publishSessionActivitySchedule(
                                        tour.sessionId, actId
                                    )
                                    if (pub.isSuccessful && pub.body()?.success == true) {
                                        sessionSchedule = pub.body()?.data
                                        editActivity = null
                                    }
                                } finally {
                                    scheduleBusy = false
                                }
                            }
                        }
                    ) { Text("Công bố") }
                    TextButton(onClick = { if (!scheduleBusy) editActivity = null }) { Text("Hủy") }
                }
            }
        )
    }

    Box(modifier = modifier.fillMaxSize().background(NatureGreenBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color(0xFF5A6E85))
                }
                Text(
                    "Chi tiết Tour",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = Color(0xFF00796B),
                    modifier = Modifier.weight(1f)
                )
                Surface(shape = RoundedCornerShape(10.dp), color = statusColor.copy(alpha = 0.12f)) {
                    Text(
                        displayTour.status.label,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            HorizontalDivider(color = Color(0xFFE2E8F0))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                error?.let {
                    Text(it, color = Color(0xFFC62828), fontSize = 12.sp, modifier = Modifier.padding(16.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))
                TourHeaderCard(displayTour)

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GuideTourInfoCard(Modifier.weight(1f), Icons.Default.CalendarMonth, "Khởi hành", displayTour.startDate)
                    GuideTourInfoCard(Modifier.weight(1f), Icons.Default.Timer, "Thời gian", "${displayTour.durationDays} ngày")
                    GuideTourInfoCard(Modifier.weight(1f), Icons.Default.Group, "Khách", "${displayTour.checkedInParticipants}/${displayTour.totalCustomers}")
                }

                if (displayTour.customers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Check-in tập trung", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    displayTour.customers.take(5).forEach { customer ->
                        val busy = checkInBusyId == customer.id
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(customer.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                    Text(customer.phone, fontSize = 11.sp, color = SecondaryTextColor)
                                }
                                if (customer.checkedInGathering) {
                                    Icon(Icons.Default.CheckCircle, null, tint = PrimaryGreen)
                                } else if (!customer.travelerUserId.isNullOrBlank()) {
                                    FilledTonalButton(
                                        onClick = {
                                            val uid = customer.travelerUserId ?: return@FilledTonalButton
                                            checkInBusyId = customer.id
                                            scope.launch {
                                                try {
                                                    RetrofitClient.guideApiService.checkin(
                                                        GuideCheckinRequest(tour.sessionId, uid, "gathering")
                                                    )
                                                    reloadAll()
                                                } finally {
                                                    checkInBusyId = null
                                                }
                                            }
                                        },
                                        enabled = !busy,
                                        modifier = Modifier.height(36.dp)
                                    ) {
                                        if (busy) CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                                        else Text("Check-in", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                sessionSchedule?.days?.let { days ->
                    if (days.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Lịch vận hành (chỉnh sửa)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            TextButton(onClick = onOpenOperations) {
                                Text("Vận hành", fontSize = 12.sp)
                            }
                        }
                        ScrollableTabRow(
                            selectedTabIndex = days.indexOfFirst { it.dayNumber == activeDay }.coerceAtLeast(0),
                            containerColor = Color.Transparent,
                            contentColor = PrimaryGreen,
                            edgePadding = 16.dp
                        ) {
                            days.forEach { day ->
                                val dn = day.dayNumber ?: 1
                                Tab(selected = activeDay == dn, onClick = { activeDay = dn }, text = { Text("Ngày $dn") })
                            }
                        }
                        scheduleDay?.activities.orEmpty().forEach { row ->
                            val eff = row.effective
                            val cancelled = eff?.cancelled == true
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(Modifier.weight(1f)) {
                                        Text(
                                            formatTimeHm(eff?.startTime ?: row.template?.startTime),
                                            fontSize = 11.sp,
                                            color = PrimaryGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            eff?.title ?: row.template?.title ?: "Hoạt động",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            color = if (cancelled) SecondaryTextColor else DarkTextColor
                                        )
                                        eff?.locationName?.let {
                                            Text(it, fontSize = 12.sp, color = SecondaryTextColor)
                                        }
                                    }
                                    IconButton(onClick = { openEdit(row) }) {
                                        Icon(Icons.Default.Edit, null, tint = PrimaryGreen)
                                    }
                                    if (!cancelled && row.activityId != null) {
                                        IconButton(onClick = {
                                            val actId = row.activityId ?: return@IconButton
                                            scope.launch {
                                                val r = RetrofitClient.guideApiService.cancelSessionActivitySchedule(
                                                    tour.sessionId, actId
                                                )
                                                if (r.isSuccessful && r.body()?.success == true) {
                                                    sessionSchedule = r.body()?.data
                                                }
                                            }
                                        }) {
                                            Icon(Icons.Default.Cancel, null, tint = Color(0xFFC62828))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (displayTour.meetingPoint.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PinDrop, null, tint = PrimaryGreen)
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("Điểm tập trung", fontSize = 11.sp, color = SecondaryTextColor)
                                Text(displayTour.meetingPoint, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Lịch trình tour", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(8.dp))
                displayTour.itinerary.forEachIndexed { index, day ->
                    val isExpanded = expandedDays[day.day] ?: false
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clickable { expandedDays[day.day] = !isExpanded },
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Ngày ${day.day}: ${day.title}", fontWeight = FontWeight.Bold)
                            if (isExpanded && day.description.isNotBlank()) {
                                Spacer(Modifier.height(6.dp))
                                Text(day.description, fontSize = 13.sp, color = SecondaryTextColor)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color.Transparent, NatureGreenBackground)))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onOpenGuestsTab, modifier = Modifier.weight(1f).height(48.dp)) {
                        Icon(Icons.Default.Group, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Khách", fontSize = 13.sp)
                    }
                    OutlinedButton(onClick = onOpenOperations, modifier = Modifier.weight(1f).height(48.dp)) {
                        Icon(Icons.Default.Schedule, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Vận hành", fontSize = 13.sp)
                    }
                }
                if (chatBookingId != null) {
                    OutlinedButton(
                        onClick = { onOpenGroupChat(chatBookingId) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        border = BorderStroke(1.5.dp, PrimaryGreen),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryGreen)
                    ) {
                        Icon(Icons.Default.Chat, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Chat đoàn", fontWeight = FontWeight.Bold)
                    }
                }
                Button(
                    onClick = onCustomerListClick,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Icon(Icons.Default.Group, null)
                    Spacer(Modifier.width(10.dp))
                    Text("Danh sách khách (${displayTour.totalCustomers})", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TourHeaderCard(displayTour: GuideTour) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            val imageRes = when {
                displayTour.name.contains("Đà Nẵng") -> com.example.flourishtravelapp.R.drawable.travel_bg
                displayTour.name.contains("Bali") -> com.example.flourishtravelapp.R.drawable.maya_bg
                else -> com.example.flourishtravelapp.R.drawable.bangkook_bg
            }
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(displayTour.id, fontSize = 11.sp, color = Color(0xFF757575))
                Text(displayTour.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = Color(0xFF9E9E9E), modifier = Modifier.size(13.dp))
                    Text(" ${displayTour.destination}", fontSize = 11.sp, color = Color(0xFF757575))
                }
            }
        }
    }
}

@Composable
private fun GuideTourInfoCard(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = DarkTextColor)
            Text(label, fontSize = 10.sp, color = SecondaryTextColor)
        }
    }
}

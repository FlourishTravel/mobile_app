package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.mapper.formatGuideDateRange
import com.example.flourishtravelapp.data.mapper.mapGuideStatus
import com.example.flourishtravelapp.data.mapper.toGuideTour
import com.example.flourishtravelapp.data.model.GuideSessionSummaryDto
import com.example.flourishtravelapp.ui.theme.*
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

private data class GuideTodo(
    val id: String,
    val text: String,
    val tag: String? = null,
    val done: Boolean = false,
    val action: () -> Unit = {}
)

@Composable
fun GuideDashboardContent(
    guide: GuideAccount,
    modifier: Modifier = Modifier,
    onTourClick: (GuideTour) -> Unit,
    onOpenGuestsTab: () -> Unit,
    onSessionsLoaded: (Int) -> Unit = {}
) {
    var tours by remember { mutableStateOf<List<GuideTour>>(emptyList()) }
    var sessions by remember { mutableStateOf<List<GuideSessionSummaryDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }

    val today = remember { LocalDate.now() }
    val dayName = remember(today) {
        today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("vi", "VN"))
    }
    val monthName = remember(today) {
        "Tháng ${today.monthValue}"
    }

    LaunchedEffect(Unit) {
        isLoading = true
        loadError = null
        try {
            val response = RetrofitClient.guideApiService.getSessions(
                month = today.monthValue,
                year = today.year
            )
            if (response.isSuccessful && response.body()?.success == true) {
                sessions = response.body()?.data.orEmpty()
                tours = sessions.map { it.toGuideTour() }
                onSessionsLoaded(tours.size)
            } else {
                loadError = response.body()?.message ?: "Không tải được danh sách tour"
            }
        } catch (e: Exception) {
            loadError = e.localizedMessage ?: "Lỗi kết nối"
        } finally {
            isLoading = false
        }
    }

    val ongoing = tours.filter { it.status == TourStatus.ONGOING }
    val upcoming = tours.filter { it.status == TourStatus.UPCOMING }
    val featured = ongoing.firstOrNull() ?: upcoming.firstOrNull()

    val monthTours = sessions.count { s ->
        val d = s.startDate?.take(10) ?: return@count false
        try {
            val ld = LocalDate.parse(d)
            ld.monthValue == today.monthValue && ld.year == today.year &&
                (s.status == "completed" || s.status == "ongoing")
        } catch (_: Exception) {
            false
        }
    }
    val totalGuests = sessions.sumOf { it.currentParticipants }

    val tourDays = remember(sessions) {
        sessions.mapNotNull { s ->
            s.startDate?.take(10)?.let { d ->
                try {
                    val ld = LocalDate.parse(d)
                    if (ld.monthValue == today.monthValue && ld.year == today.year) ld.dayOfMonth else null
                } catch (_: Exception) {
                    null
                }
            }
        }.toSet()
    }

    val todos = remember(featured, ongoing, upcoming) {
        val items = mutableListOf<GuideTodo>()
        featured?.let { f ->
            items.add(
                GuideTodo(
                    id = "guests",
                    text = "Kiểm tra danh sách khách — ${f.name}",
                    action = { onTourClick(f) }
                )
            )
            if (f.checkedInParticipants < f.totalCustomers) {
                items.add(
                    GuideTodo(
                        id = "checkin",
                        text = "Điểm danh tập trung (${f.checkedInParticipants}/${f.totalCustomers})",
                        tag = "Quan trọng",
                        action = onOpenGuestsTab
                    )
                )
            }
        }
        if (upcoming.isNotEmpty() && ongoing.isEmpty()) {
            val u = upcoming.first()
            items.add(
                GuideTodo(
                    id = "prep",
                    text = "Chuẩn bị tour sắp tới: ${u.name}",
                    action = { onTourClick(u) }
                )
            )
        }
        if (items.isEmpty()) {
            items.add(GuideTodo(id = "empty", text = "Chưa có tour được giao trong tháng này", done = true))
        }
        items
    }

    val progressPercent = featured?.let { f ->
        if (f.totalCustomers <= 0) 0
        else ((f.checkedInParticipants.toFloat() / f.totalCustomers) * 100).toInt().coerceIn(0, 100)
    } ?: 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(colors = listOf(Color(0xFF004D40), PrimaryGreen)))
                .statusBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 28.dp)
        ) {
            Column {
                Text(
                    "Xin chào, ${guide.name.split(" ").lastOrNull() ?: guide.name}!",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Chúc bạn một ngày dẫn tour thuận lợi.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "$dayName, ${today.dayOfMonth} $monthName ${today.year}",
                    color = Color(0xFF69F0AE),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GuideStatCard(Modifier.weight(1f), "$monthTours", "Tour tháng", Icons.Default.CalendarMonth)
                    GuideStatCard(Modifier.weight(1f), "$totalGuests", "Tổng khách", Icons.Default.Group)
                    GuideStatCard(Modifier.weight(1f), "${ongoing.size}", "Đang chạy", Icons.Default.DirectionsBus)
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            loadError?.let { GuideErrorText(it) }

            if (isLoading) {
                GuideLoadingBox()
            } else {
                featured?.let { tour ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onTourClick(tour) },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(shape = RoundedCornerShape(8.dp), color = LightGreenBackground) {
                                    Text(
                                        tour.status.label,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        color = PrimaryGreen,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text("Mã: ${tour.id}", fontSize = 11.sp, color = SecondaryTextColor)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(tour.name, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = DarkTextColor)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                formatGuideDateRange(tour.startDateIso, tour.endDateIso),
                                fontSize = 12.sp,
                                color = SecondaryTextColor
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            GuideProgressBar(
                                percent = progressPercent,
                                label = "Tiến độ điểm danh (${tour.checkedInParticipants}/${tour.totalCustomers})"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text("Việc cần làm", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
                Spacer(modifier = Modifier.height(8.dp))
                todos.forEach { todo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .then(if (!todo.done) Modifier.clickable { todo.action() } else Modifier),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (todo.done) Color(0xFFF5F5F5) else Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (todo.done) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                null,
                                tint = if (todo.done) SecondaryTextColor else PrimaryGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(todo.text, fontSize = 13.sp, color = DarkTextColor)
                                todo.tag?.let {
                                    Text(it, fontSize = 10.sp, color = Color(0xFFE65100), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text("Lịch tour tháng $monthName", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
                Spacer(modifier = Modifier.height(10.dp))
                CalendarHintsGrid(
                    year = today.year,
                    month = today.monthValue,
                    todayDay = today.dayOfMonth,
                    tourDays = tourDays
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CalendarHintsGrid(
    year: Int,
    month: Int,
    todayDay: Int,
    tourDays: Set<Int>
) {
    val firstDay = LocalDate.of(year, month, 1).dayOfWeek.value % 7
    val adjustedFirst = if (firstDay == 0) 6 else firstDay - 1
    val daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth()

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            val headers = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
            Row(modifier = Modifier.fillMaxWidth()) {
                headers.forEach { h ->
                    Text(
                        h,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontSize = 10.sp,
                        color = SecondaryTextColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            var day = 1
            val rows = (adjustedFirst + daysInMonth + 6) / 7
            repeat(rows) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    repeat(7) { col ->
                        val cellIndex = it * 7 + col
                        val showDay = cellIndex >= adjustedFirst && day <= daysInMonth
                        val d = if (showDay) day else null
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (d != null) {
                                val isToday = d == todayDay
                                val hasTour = tourDays.contains(d)
                                Surface(
                                    shape = CircleShape,
                                    color = when {
                                        isToday -> PrimaryGreen
                                        hasTour -> LightGreenBackground
                                        else -> Color.Transparent
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            "$d",
                                            fontSize = 12.sp,
                                            fontWeight = if (isToday || hasTour) FontWeight.Bold else FontWeight.Normal,
                                            color = when {
                                                isToday -> Color.White
                                                hasTour -> PrimaryGreen
                                                else -> DarkTextColor
                                            }
                                        )
                                    }
                                }
                                day++
                            }
                        }
                    }
                }
            }
        }
    }
}

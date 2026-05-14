package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// ─── Entry point ─────────────────────────────────────────────────────────────

@Composable
fun GuideHomeScreen(
    guide: GuideAccount,
    modifier: Modifier = Modifier,
    onTourClick: (GuideTour) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            GuideBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        containerColor = NatureGreenBackground
    ) { innerPadding ->
        when (selectedTab) {
            0 -> GuideTourListContent(
                guide = guide,
                modifier = Modifier.padding(innerPadding),
                onTourClick = onTourClick
            )
            1 -> GuideBookingPlaceholder(
                modifier = Modifier.padding(innerPadding)
            )
            2 -> GuideProfileContent(
                guide = guide,
                modifier = Modifier.padding(innerPadding),
                onLogout = onLogout
            )
        }
    }
}

// ─── Bottom Navigation ────────────────────────────────────────────────────────

@Composable
private fun GuideBottomNavigation(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(76.dp)
    ) {
        data class GuideNavItem(val label: String, val icon: ImageVector, val selectedIcon: ImageVector)
        val items = listOf(
            GuideNavItem("Tour", Icons.Outlined.Map, Icons.Filled.Map),
            GuideNavItem("Book lịch", Icons.Outlined.CalendarMonth, Icons.Filled.CalendarMonth),
            GuideNavItem("Hồ sơ", Icons.Outlined.Person, Icons.Filled.Person)
        )
        items.forEachIndexed { index, item ->
            val isSelected = selectedTab == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(height = 32.dp, width = 52.dp)
                            .background(
                                color = if (isSelected) LightGreenBackground else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected) PrimaryGreen else SecondaryTextColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                label = {
                    Text(
                        item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) PrimaryGreen else SecondaryTextColor
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}

// ─── Tab 0: Tour List ─────────────────────────────────────────────────────────

@Composable
private fun GuideTourListContent(
    guide: GuideAccount,
    modifier: Modifier = Modifier,
    onTourClick: (GuideTour) -> Unit
) {
    val ongoingTours = mockGuideTours.filter { it.status == TourStatus.ONGOING }
    val upcomingTours = mockGuideTours.filter { it.status == TourStatus.UPCOMING }
    val completedTours = mockGuideTours.filter { it.status == TourStatus.COMPLETED }
    val totalCustomersToday = ongoingTours.sumOf { it.totalCustomers }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header ──────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF004D40), PrimaryGreen)
                    )
                )
                .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 28.dp)
        ) {
            Column {
                // Greeting – no logout here anymore
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(8.dp),
                        shape = CircleShape,
                        color = Color(0xFF69F0AE)
                    ) {}
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "HƯỚNG DẪN VIÊN",
                        color = Color(0xFF69F0AE),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Xin chào, ${guide.name}!",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    guide.handle,
                    color = Color.White.copy(alpha = 0.70f),
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GuideStatCard(
                        modifier = Modifier.weight(1f),
                        value = "${ongoingTours.size}",
                        label = "Tour hôm nay",
                        icon = Icons.Default.DirectionsBus
                    )
                    GuideStatCard(
                        modifier = Modifier.weight(1f),
                        value = "$totalCustomersToday",
                        label = "Khách hôm nay",
                        icon = Icons.Default.Group
                    )
                    GuideStatCard(
                        modifier = Modifier.weight(1f),
                        value = "${guide.rating}★",
                        label = "Đánh giá",
                        icon = Icons.Default.Star
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            // Specialty badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = LightGreenBackground
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Explore, null, tint = PrimaryGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Chuyên môn: ${guide.specialty}",
                        color = PrimaryGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (ongoingTours.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                GuideSectionHeader(title = "Đang diễn ra", count = ongoingTours.size)
                Spacer(modifier = Modifier.height(12.dp))
                ongoingTours.forEach { tour ->
                    GuideTourCard(tour = tour, onClick = { onTourClick(tour) })
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            if (upcomingTours.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                GuideSectionHeader(title = "Sắp khởi hành", count = upcomingTours.size)
                Spacer(modifier = Modifier.height(12.dp))
                upcomingTours.forEach { tour ->
                    GuideTourCard(tour = tour, onClick = { onTourClick(tour) })
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            if (completedTours.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                GuideSectionHeader(title = "Đã hoàn thành", count = completedTours.size)
                Spacer(modifier = Modifier.height(12.dp))
                completedTours.forEach { tour ->
                    GuideTourCard(tour = tour, onClick = { onTourClick(tour) })
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ─── Tab 1: Book lịch ────────────────────────────────────────────────────────

@Composable
private fun GuideBookingPlaceholder(modifier: Modifier = Modifier) {
    val today = remember { LocalDate.now() }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDays by remember { mutableStateOf(setOf<LocalDate>()) }
    var bookedDays by remember { mutableStateOf(setOf<LocalDate>()) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showSuccessSnackbar by remember { mutableStateOf(false) }

    // Mock assigned days (days where system already assigned tours)
    val assignedDays = remember {
        mapOf(
            LocalDate.now().plusDays(2) to "Khám Phá Thái Lan Chill",
            LocalDate.now().plusDays(3) to "Khám Phá Thái Lan Chill",
            LocalDate.now().plusDays(4) to "Khám Phá Thái Lan Chill",
            LocalDate.now().plusDays(10) to "Bali – Thiên Đường Nhiệt Đới",
            LocalDate.now().plusDays(11) to "Bali – Thiên Đường Nhiệt Đới",
            LocalDate.now().plusDays(12) to "Bali – Thiên Đường Nhiệt Đới"
        )
    }

    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value % 7 // 0=Sun
    val monthName = currentMonth.month.getDisplayName(TextStyle.FULL, Locale("vi", "VN"))
        .replaceFirstChar { it.uppercase() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header ──────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF004D40), PrimaryGreen)
                    )
                )
                .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 28.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(8.dp),
                        shape = CircleShape,
                        color = Color(0xFF69F0AE)
                    ) {}
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "LỊCH CÔNG TÁC",
                        color = Color(0xFF69F0AE),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Book lịch làm việc",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Chọn ngày bạn có thể công tác, hệ thống sẽ tự động sắp xếp tour",
                    color = Color.White.copy(alpha = 0.70f),
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GuideStatCard(
                        modifier = Modifier.weight(1f),
                        value = "${bookedDays.size + selectedDays.size}",
                        label = "Ngày đã book",
                        icon = Icons.Default.EventAvailable
                    )
                    GuideStatCard(
                        modifier = Modifier.weight(1f),
                        value = "${assignedDays.count { it.key.month == currentMonth.month }}",
                        label = "Đã phân tour",
                        icon = Icons.Default.AssignmentTurnedIn
                    )
                    GuideStatCard(
                        modifier = Modifier.weight(1f),
                        value = "${selectedDays.size}",
                        label = "Đang chọn",
                        icon = Icons.Default.TouchApp
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            // ── Month Navigation ────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            currentMonth = currentMonth.minusMonths(1)
                        }) {
                            Icon(
                                Icons.Default.ChevronLeft, "Tháng trước",
                                tint = PrimaryGreen
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Tháng ${currentMonth.monthValue}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTextColor
                            )
                            Text(
                                "${currentMonth.year}",
                                fontSize = 13.sp,
                                color = SecondaryTextColor
                            )
                        }
                        IconButton(onClick = {
                            currentMonth = currentMonth.plusMonths(1)
                        }) {
                            Icon(
                                Icons.Default.ChevronRight, "Tháng sau",
                                tint = PrimaryGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Day-of-week headers
                    val dayLabels = listOf("CN", "T2", "T3", "T4", "T5", "T6", "T7")
                    Row(modifier = Modifier.fillMaxWidth()) {
                        dayLabels.forEach { label ->
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SecondaryTextColor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Calendar grid
                    val totalCells = firstDayOfWeek + daysInMonth
                    val rows = (totalCells + 6) / 7

                    for (row in 0 until rows) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            for (col in 0..6) {
                                val cellIndex = row * 7 + col
                                val day = cellIndex - firstDayOfWeek + 1

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (day in 1..daysInMonth) {
                                        val date = currentMonth.atDay(day)
                                        val isPast = date.isBefore(today)
                                        val isToday = date == today
                                        val isSelected = date in selectedDays
                                        val isBooked = date in bookedDays
                                        val isAssigned = date in assignedDays

                                        val bgColor = when {
                                            isAssigned -> Color(0xFF00796B)
                                            isBooked -> Color(0xFF1565C0)
                                            isSelected -> PrimaryGreen
                                            isToday -> LightGreenBackground
                                            else -> Color.Transparent
                                        }
                                        val textColor = when {
                                            isAssigned || isBooked || isSelected -> Color.White
                                            isPast -> SecondaryTextColor.copy(alpha = 0.4f)
                                            isToday -> PrimaryGreen
                                            else -> DarkTextColor
                                        }

                                        val clickable = !isPast && !isAssigned && !isBooked

                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(bgColor)
                                                .then(
                                                    if (isToday && !isSelected && !isBooked && !isAssigned)
                                                        Modifier.border(
                                                            2.dp, PrimaryGreen,
                                                            RoundedCornerShape(12.dp)
                                                        )
                                                    else Modifier
                                                )
                                                .then(
                                                    if (clickable)
                                                        Modifier.clickable {
                                                            selectedDays = if (date in selectedDays)
                                                                selectedDays - date
                                                            else
                                                                selectedDays + date
                                                        }
                                                    else Modifier
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    "$day",
                                                    fontSize = 14.sp,
                                                    fontWeight = if (isToday || isSelected || isBooked || isAssigned)
                                                        FontWeight.Bold else FontWeight.Normal,
                                                    color = textColor
                                                )
                                                if (isAssigned) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(4.dp)
                                                            .background(
                                                                Color(0xFF69F0AE),
                                                                CircleShape
                                                            )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Legend ──────────────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Chú thích",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextColor
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ScheduleLegendItem("Đang chọn", PrimaryGreen)
                        ScheduleLegendItem("Đã book", Color(0xFF1565C0))
                        ScheduleLegendItem("Có tour", Color(0xFF00796B))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Assigned tours for selected month ──────────────────────────
            val monthAssignments = assignedDays.filter {
                it.key.month == currentMonth.month && it.key.year == currentMonth.year
            }
            if (monthAssignments.isNotEmpty()) {
                Text(
                    "Tour đã được phân trong tháng",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor
                )
                Spacer(modifier = Modifier.height(10.dp))

                val groupedByTour = monthAssignments.entries.groupBy { it.value }
                groupedByTour.forEach { (tourName, entries) ->
                    val days = entries.map { it.key }.sorted()
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF00796B).copy(alpha = 0.08f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF00796B).copy(alpha = 0.15f)
                            ) {
                                Icon(
                                    Icons.Default.Map, null,
                                    tint = Color(0xFF00796B),
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    tourName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = DarkTextColor
                                )
                                Text(
                                    "Ngày ${days.joinToString(", ") { "${it.dayOfMonth}" }}/${currentMonth.monthValue}",
                                    fontSize = 12.sp,
                                    color = SecondaryTextColor
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF00796B).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    "${days.size} ngày",
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    ),
                                    color = Color(0xFF00796B),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Confirm button ─────────────────────────────────────────────
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = selectedDays.isNotEmpty(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    disabledContainerColor = SecondaryTextColor.copy(alpha = 0.2f)
                )
            ) {
                Icon(Icons.Default.EventAvailable, null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    if (selectedDays.isEmpty()) "Chọn ngày để book lịch"
                    else "Xác nhận ${selectedDays.size} ngày công tác",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            // Success feedback
            if (showSuccessSnackbar) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2E7D32).copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle, null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Đã book lịch thành công! Hệ thống sẽ tự động sắp xếp tour cho bạn.",
                            color = Color(0xFF2E7D32),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // ── Confirm Dialog ──────────────────────────────────────────────────────
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
            icon = {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = LightGreenBackground
                ) {
                    Icon(
                        Icons.Default.CalendarMonth, null,
                        tint = PrimaryGreen,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            },
            title = {
                Text(
                    "Xác nhận book lịch",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Bạn sẽ đăng ký công tác ${selectedDays.size} ngày:",
                        color = SecondaryTextColor,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = LightGreenBackground
                    ) {
                        Text(
                            selectedDays.sorted().joinToString(", ") {
                                "${it.dayOfMonth}/${it.monthValue}"
                            },
                            modifier = Modifier.padding(12.dp),
                            color = PrimaryGreen,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Hệ thống sẽ tự động sắp xếp tour phù hợp cho bạn.",
                        color = SecondaryTextColor,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        bookedDays = bookedDays + selectedDays
                        selectedDays = emptySet()
                        showConfirmDialog = false
                        showSuccessSnackbar = true
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Xác nhận", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Hủy", color = SecondaryTextColor)
                }
            }
        )
    }
}

@Composable
private fun ScheduleLegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, fontSize = 11.sp, color = SecondaryTextColor)
    }
}

// ─── Tab 2: Hồ sơ ────────────────────────────────────────────────────────────

@Composable
private fun GuideProfileContent(
    guide: GuideAccount,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Profile header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF004D40), PrimaryGreen)
                    )
                )
                .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.size(88.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.25f)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(guide.name, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(guide.handle, color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFFFD54F), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${guide.rating} · ${guide.totalTours} tour", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(20.dp))

            // Info cards
            ProfileInfoRow(icon = Icons.Default.Phone, label = "Điện thoại", value = guide.phone)
            Spacer(modifier = Modifier.height(10.dp))
            ProfileInfoRow(icon = Icons.Default.Explore, label = "Chuyên môn", value = guide.specialty)
            Spacer(modifier = Modifier.height(10.dp))
            ProfileInfoRow(icon = Icons.Default.WorkHistory, label = "Tổng tour", value = "${guide.totalTours} tour đã dẫn")

            Spacer(modifier = Modifier.height(32.dp))

            // Logout button
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFEBEE),
                    contentColor = Color(0xFFC62828)
                )
            ) {
                Icon(Icons.Default.Logout, null)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Đăng xuất", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = LightGreenBackground
            ) {
                Icon(icon, null, tint = PrimaryGreen, modifier = Modifier.padding(10.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(label, fontSize = 11.sp, color = SecondaryTextColor)
                Text(value, fontSize = 14.sp, color = DarkTextColor, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// ─── Shared Sub-Composables ───────────────────────────────────────────────────

@Composable
private fun GuideStatCard(
    modifier: Modifier,
    value: String,
    label: String,
    icon: ImageVector
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.18f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.25f)
            ) {
                Icon(icon, null, tint = Color.White, modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Text(label, color = Color.White.copy(alpha = 0.8f), fontSize = 9.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun GuideSectionHeader(title: String, count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = DarkTextColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Surface(shape = CircleShape, color = PrimaryGreen) {
            Text(
                "$count",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun GuideTourCard(tour: GuideTour, onClick: () -> Unit) {
    val statusColor = Color(tour.status.color)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(16.dp),
                color = statusColor.copy(alpha = 0.12f)
            ) {
                Icon(
                    Icons.Default.Map,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        tour.status.label,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(tour.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkTextColor)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = SecondaryTextColor, modifier = Modifier.size(12.dp))
                    Text(" ${tour.destination}", color = SecondaryTextColor, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarMonth, null, tint = SecondaryTextColor, modifier = Modifier.size(12.dp))
                    Text(" ${tour.startDate} – ${tour.endDate}", color = SecondaryTextColor, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.ChevronRight, null, tint = SecondaryTextColor)
                Spacer(modifier = Modifier.height(4.dp))
                Surface(shape = RoundedCornerShape(8.dp), color = LightGreenBackground) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Group, null, tint = PrimaryGreen, modifier = Modifier.size(12.dp))
                        Text(" ${tour.totalCustomers}", color = PrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

package com.example.flourishtavelapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.flourishtavelapp.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// ─── Entry Point ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideHomeScreen(
    guide: GuideAccount,
    modifier: Modifier = Modifier,
    onTourClick: (GuideTour) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showProfileDialog by remember { mutableStateOf(false) }
    var showCalendarDialog by remember { mutableStateOf(false) }
    var showAISheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            GuideBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        floatingActionButton = {
            // Chatbot FAB matching the robot head in the screenshot
            FloatingActionButton(
                onClick = { showAISheet = true },
                containerColor = Color(0xFF00796B),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 12.dp, end = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = "AI Guide",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        containerColor = Color(0xFFF3F5F9) // Matching screen background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ── Pinned Top Header (Avatar, Title, Bell, Calendar) ──────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Clickable Avatar opens Guide Profile and Logout dialog!
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { showProfileDialog = true },
                    shape = CircleShape,
                    color = LightGreenBackground
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = PrimaryGreen,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title: Dynamic header title based on active tab
                Text(
                    text = when (selectedTab) {
                        0 -> "Tour Của Tôi"
                        1 -> "Lịch Công Tác"
                        2 -> "Tin Nhắn"
                        3 -> "Hồ Sơ"
                        else -> "Tour Của Tôi"
                    },
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = Color(0xFF00796B), // Green theme
                    modifier = Modifier.weight(1f)
                )

                // Calendar icon to access calendar work booking
                IconButton(onClick = { showCalendarDialog = true }) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = "Schedule",
                        tint = Color(0xFF5A6E85)
                    )
                }

                // Notification Bell icon
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = Color(0xFF5A6E85)
                    )
                }
            }

            // Divider underneath the header
            HorizontalDivider(thickness = 1.dp, color = Color(0xFFE2E8F0))

            // ── Main Content Tabs ──────────────────────────────────────────────────
            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTab) {
                    0 -> GuideTourListContent(
                        guide = guide,
                        onTourClick = onTourClick
                    )
                    1 -> GuideBookingCalendarContent()
                    2 -> GuideChatMockupScreen()
                    3 -> GuideProfileContent(
                        guide = guide,
                        onLogout = onLogout
                    )
                }
            }
        }
    }

    // ── Dialogs & Sheets ─────────────────────────────────────────────────────

    // Profile & Logout Dialog (Triggered by Avatar)
    if (showProfileDialog) {
        AlertDialog(
            onDismissRequest = { showProfileDialog = false },
            title = {
                Text(
                    text = "Thông tin hướng dẫn viên",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DarkTextColor
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(modifier = Modifier.size(56.dp), shape = CircleShape, color = LightGreenBackground) {
                            Icon(Icons.Default.Person, null, tint = PrimaryGreen, modifier = Modifier.padding(12.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(guide.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
                            Text(guide.handle, fontSize = 13.sp, color = SecondaryTextColor)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("📞 Số điện thoại: ${guide.phone}", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("⭐ Đánh giá: ${guide.rating}★", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("✈️ Chuyên môn: ${guide.specialty}", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("🧳 Tổng số tour đã dẫn: ${guide.totalTours} tour", fontSize = 14.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showProfileDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) {
                    Text("Đăng xuất", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showProfileDialog = false }) {
                    Text("Đóng", color = SecondaryTextColor)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }

    // Calendar Sheet Dialog (Triggered by Calendar Icon in Header)
    if (showCalendarDialog) {
        Dialog(onDismissRequest = { showCalendarDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f),
                shape = RoundedCornerShape(28.dp),
                color = Color.White
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Lịch công tác",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = DarkTextColor
                        )
                        IconButton(onClick = { showCalendarDialog = false }) {
                            Icon(Icons.Default.Close, "Close", tint = SecondaryTextColor)
                        }
                    }
                    HorizontalDivider()
                    Box(modifier = Modifier.weight(1f)) {
                        GuideBookingCalendarContent()
                    }
                }
            }
        }
    }

    // AI Guide Floating Sheet (Triggered by Chatbot FAB)
    if (showAISheet) {
        Dialog(onDismissRequest = { showAISheet = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f),
                shape = RoundedCornerShape(28.dp),
                color = Color.White
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.SmartToy, null, tint = Color(0xFF00796B), modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI Guide Assistant",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = DarkTextColor
                            )
                        }
                        IconButton(onClick = { showAISheet = false }) {
                            Icon(Icons.Default.Close, "Close", tint = SecondaryTextColor)
                        }
                    }
                    HorizontalDivider()
                    Box(modifier = Modifier.weight(1f)) {
                        GuideAIScreen()
                    }
                }
            }
        }
    }
}

// ─── Bottom Navigation Bar (Check-in, Maps, Chat, AI Guide) ───────────────────

@Composable
private fun GuideBottomNavigation(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(72.dp)
    ) {
        data class NavigationItem(val label: String, val icon: ImageVector, val selectedIcon: ImageVector)
        val items = listOf(
            NavigationItem("Check-in", Icons.Outlined.QrCodeScanner, Icons.Filled.QrCodeScanner),
            NavigationItem("Lịch", Icons.Outlined.CalendarMonth, Icons.Filled.CalendarMonth),
            NavigationItem("Chat", Icons.Outlined.Chat, Icons.Filled.Chat),
            NavigationItem("Hồ sơ", Icons.Outlined.Person, Icons.Filled.Person)
        )
        items.forEachIndexed { index, item ->
            val isSelected = selectedTab == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(height = 30.dp, width = 50.dp)
                            .background(
                                color = if (isSelected) LightGreenBackground else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected) Color(0xFF00796B) else SecondaryTextColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color(0xFF00796B) else SecondaryTextColor
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}

// ─── Tab 0: Redesigned Tour List Content (Mockup-matched) ─────────────────────

@Composable
private fun GuideTourListContent(
    guide: GuideAccount,
    onTourClick: (GuideTour) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Tất cả") }

    // Filter Logic
    val filteredTours = remember(selectedFilter, searchQuery) {
        mockGuideTours.filter { tour ->
            val matchesFilter = when (selectedFilter) {
                "Đang diễn ra" -> tour.status == TourStatus.ONGOING
                "Sắp tới" -> tour.status == TourStatus.UPCOMING
                "Hoàn thành" -> tour.status == TourStatus.COMPLETED
                else -> true
            }
            val matchesQuery = tour.name.contains(searchQuery, ignoreCase = true) ||
                    tour.id.contains(searchQuery, ignoreCase = true)
            matchesFilter && matchesQuery
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp) // padding for FAB and padding
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Search Bar matching mockup
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Tìm ID, tên tour, khách hàng...", fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = Color(0xFF94A3B8)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color(0xFF00796B),
                unfocusedIndicatorColor = Color(0xFFCBD5E1),
                cursorColor = Color(0xFF00796B)
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Filter Chips row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val chips = listOf("Tất cả", "Đang diễn ra", "Sắp tới", "Hoàn thành")
            chips.forEach { chipName ->
                val isSelected = selectedFilter == chipName
                Surface(
                    modifier = Modifier.clickable { selectedFilter = chipName },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) Color(0xFF00796B) else Color.White,
                    border = BorderStroke(1.dp, if (isSelected) Color(0xFF00796B) else Color(0xFFCBD5E1)),
                    shadowElevation = if (isSelected) 2.dp else 0.dp
                ) {
                    Text(
                        text = chipName,
                        color = if (isSelected) Color.White else Color(0xFF1E293B),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Tours Render List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            filteredTours.forEach { tour ->
                if (tour.status == TourStatus.ONGOING) {
                    // Ongoing Large Detailed Card matching the screenshot
                    OngoingTourCard(tour = tour, onClick = { onTourClick(tour) })
                } else {
                    // Upcoming Small Card matching the screenshot
                    UpcomingTourCard(tour = tour, onClick = { onTourClick(tour) })
                }
            }
        }
    }
}

// ─── Ongoing Large Tour Card Composable ───────────────────────────────────────

@Composable
fun OngoingTourCard(
    tour: GuideTour,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column {
            // Image section with overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                // High-quality image resource (bangkook_bg)
                Image(
                    painter = painterResource(id = com.example.flourishtavelapp.R.drawable.bangkook_bg),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay to make overlaid white text readable
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f)),
                                startY = 100f
                            )
                        )
                )

                // Top-left status badge: ● Đang diễn ra
                Surface(
                    color = Color(0xFF4CAF50), // Green dot/badge
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color.White, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Đang diễn ra",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }

                // Overlaid text in bottom left corner
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = tour.id,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = tour.name,
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // Details section below image
            Column(modifier = Modifier.padding(16.dp)) {
                // Check-in and Day info row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF00796B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Check-in: 26/28 khách",
                            color = Color(0xFF00796B),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Text(
                        text = "Day 2/5",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = 2f / 5f, // Day 2 of 5
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF00796B),
                    trackColor = Color(0xFFE2E8F0),
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Time and Bus details grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Left Time box
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF1F7F4), RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = null,
                            tint = Color(0xFF00796B),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("THỜI GIAN", fontSize = 9.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                            Text("01/06 - 05/06", fontSize = 13.sp, color = Color(0xFF1E293B), fontWeight = FontWeight.ExtraBold)
                        }
                    }

                    // Right Bus number box
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF1F7F4), RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DirectionsBus,
                            contentDescription = null,
                            tint = Color(0xFF00796B),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("SỐ XE", fontSize = 9.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                            Text("51B-12345", fontSize = 13.sp, color = Color(0xFF1E293B), fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Current Location Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF00796B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Vị trí: Grand Palace",
                        color = Color(0xFF1E293B),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Next Step Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFFE65100), // Warning/Orange
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Tiếp theo: Safari World (14:00)",
                        color = Color(0xFF1E293B),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ─── Upcoming Tour Card Composable ────────────────────────────────────────────

@Composable
fun UpcomingTourCard(
    tour: GuideTour,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Destination Thumbnail Image
            val imageRes = if (tour.name.contains("Đà Nẵng")) {
                com.example.flourishtavelapp.R.drawable.travel_bg
            } else {
                com.example.flourishtavelapp.R.drawable.maya_bg
            }
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Text Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tour.id,
                        color = Color(0xFF64748B),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    // Upcoming orange badge matching screenshot
                    Surface(
                        color = Color(0xFFFFF3E0), // Light peach
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "SẮP TỚI",
                            color = Color(0xFFE65100), // Dark peach/orange
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = tour.name,
                    color = Color(0xFF1E293B),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Date row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${tour.startDate} – ${tour.endDate}/2026",
                        color = Color(0xFF64748B),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Customer count row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Group,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${tour.totalCustomers} khách",
                        color = Color(0xFF64748B),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// Tab 1 (Maps Screen Mockup) code was removed and replaced by Direct Calendar Booking Content in the main navigation flow.

// ─── Tab 2: Group & Operator Chat Mockup Screen ───────────────────────────────

@Composable
fun GuideChatMockupScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F5F9))
    ) {
        Text(
            text = "Kênh thảo luận đoàn",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFF5A6E85),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column {
                ChatListItem(
                    title = "Đoàn Bangkok - Pattaya (28)",
                    subtitle = "Khách Nguyễn Văn Bảo: Dạ vâng ạ.",
                    time = "14:02",
                    unreadCount = 2
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                ChatListItem(
                    title = "Hỗ trợ Flourish Travel (Operator)",
                    subtitle = "Operator: Nhờ HDV cập nhật check-in chiều nay.",
                    time = "11:30",
                    unreadCount = 0
                )
            }
        }
    }
}

@Composable
fun ChatListItem(title: String, subtitle: String, time: String, unreadCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(modifier = Modifier.size(44.dp), shape = CircleShape, color = LightGreenBackground) {
            Icon(Icons.Default.Chat, null, tint = Color(0xFF00796B), modifier = Modifier.padding(12.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 12.sp, color = Color(0xFF64748B), maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(time, fontSize = 11.sp, color = Color(0xFF94A3B8))
            Spacer(modifier = Modifier.height(4.dp))
            if (unreadCount > 0) {
                Surface(color = Color(0xFFE53935), shape = CircleShape) {
                    Text(
                        text = "$unreadCount",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

// ─── Tab 3 / FAB: Guide AI assistant screen ──────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideAIScreen() {
    var chatMessage by remember { mutableStateOf("") }
    val chatHistory = remember {
        mutableStateListOf(
            ChatMessage("Xin chào HDV Minh Quân! Tôi là trợ lý AI hỗ trợ bạn điều hành đoàn. Hôm nay tôi có thể hỗ trợ gì cho bạn?", "08:00", false),
            ChatMessage("Cho tôi xem lại lưu ý quan trọng của khách hàng đoàn THA-2026-001?", "14:15", true),
            ChatMessage("Đoàn có khách hàng Trần Thị Mai bị dị ứng hải sản nặng, nhờ HDV làm việc kỹ với nhà hàng chiều nay tại Pattaya. Ngoài ra khách hàng Bùi Thị Ngọc có yêu cầu phòng tầng cao.", "14:15", false)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Chat messages window
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            chatHistory.forEach { msg ->
                val alignment = if (msg.isUser) Alignment.End else Alignment.Start
                val bgColor = if (msg.isUser) Color(0xFF00796B) else Color(0xFFE2E8F0)
                val textColor = if (msg.isUser) Color.White else Color(0xFF1E293B)
                val shape = if (msg.isUser) RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp) else RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)

                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
                    Surface(
                        color = bgColor,
                        shape = shape,
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Text(
                            text = msg.message,
                            color = textColor,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(msg.time, fontSize = 9.sp, color = Color(0xFF94A3B8))
                }
            }
        }

        // Input row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = chatMessage,
                onValueChange = { chatMessage = it },
                placeholder = { Text("Hỏi AI Guide...", fontSize = 14.sp) },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF1F5F9),
                    unfocusedContainerColor = Color(0xFFF1F5F9),
                    focusedIndicatorColor = Color(0xFF00796B),
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (chatMessage.isNotBlank()) {
                        chatHistory.add(ChatMessage(chatMessage, "14:38", true))
                        val question = chatMessage
                        chatMessage = ""
                        // Mock reply
                        val reply = when {
                            question.contains("tiếp theo", ignoreCase = true) -> "Điểm đến tiếp theo là Safari World lúc 14:00. Lộ trình di chuyển mất khoảng 45 phút bằng xe bus."
                            question.contains("số xe", ignoreCase = true) -> "Xe di chuyển của đoàn hôm nay có biển số: 51B-12345 do tài xế Somchai điều khiển."
                            else -> "Dạ, tôi đã nhận được câu hỏi. Tôi đang kiểm tra hệ thống và sẽ phản hồi ngay lập tức."
                        }
                        chatHistory.add(ChatMessage(reply, "14:38", false))
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFF00796B), CircleShape)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}

// ─── Shared Components & Calendars ────────────────────────────────────────────

@Composable
fun GuideBookingCalendarContent() {
    val today = remember { LocalDate.now() }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDays by remember { mutableStateOf(setOf<LocalDate>()) }
    var bookedDays by remember { mutableStateOf(setOf<LocalDate>()) }
    var showSuccessSnackbar by remember { mutableStateOf(false) }

    // Dropdown Selectors State
    var selectedDestination by remember { mutableStateOf("Bangkok, Thái Lan") }
    var destinationMenuExpanded by remember { mutableStateOf(false) }
    var selectedTourType by remember { mutableStateOf("Tour Cao Cấp") }
    var tourTypeMenuExpanded by remember { mutableStateOf(false) }

    val assignedDays = remember {
        mapOf(
            LocalDate.now().plusDays(2) to "Bangkok - Pattaya 5N4Đ",
            LocalDate.now().plusDays(10) to "Bali - Thiên đường nghỉ dưỡng"
        )
    }

    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value % 7

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // --- 1. Destination & Tour Type Selectors Row ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Box 1: ĐIỂM ĐẾN
            Box(modifier = Modifier.weight(1f)) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .clickable { destinationMenuExpanded = true },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFEBEBEB))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "ĐIỂM ĐẾN",
                            color = SecondaryTextColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF00796B),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = selectedDestination,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DarkTextColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = SecondaryTextColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                DropdownMenu(
                    expanded = destinationMenuExpanded,
                    onDismissRequest = { destinationMenuExpanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    listOf("Bangkok, Thái Lan", "Chiang Mai, Thái Lan", "Phuket, Thái Lan", "Hồ Chí Minh, Việt Nam").forEach { dest ->
                        DropdownMenuItem(
                            text = { Text(dest, fontWeight = FontWeight.Bold, color = DarkTextColor) },
                            onClick = {
                                selectedDestination = dest
                                destinationMenuExpanded = false
                            }
                        )
                    }
                }
            }

            // Box 2: LOẠI TOUR
            Box(modifier = Modifier.weight(1f)) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .clickable { tourTypeMenuExpanded = true },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFEBEBEB))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "LOẠI TOUR",
                            color = SecondaryTextColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Explore,
                                    contentDescription = null,
                                    tint = Color(0xFF00796B),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = selectedTourType,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DarkTextColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = SecondaryTextColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                DropdownMenu(
                    expanded = tourTypeMenuExpanded,
                    onDismissRequest = { tourTypeMenuExpanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    listOf("Tour Cao Cấp", "Tour Tiêu Chuẩn", "Tour Sinh Thái", "Tour Giá Rẻ").forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type, fontWeight = FontWeight.Bold, color = DarkTextColor) },
                            onClick = {
                                selectedTourType = type
                                tourTypeMenuExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 2. Calendar wrapped in a rounded white Card block ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFEBEBEB)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Month Selector Title Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                        Icon(Icons.Default.ChevronLeft, "Prev", tint = Color(0xFF00796B))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Tháng ${currentMonth.monthValue}, ${currentMonth.year}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextColor
                        )
                    }
                    IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                        Icon(Icons.Default.ChevronRight, "Next", tint = Color(0xFF00796B))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Grid weekdays labels
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("CN", "T2", "T3", "T4", "T5", "T6", "T7").forEach { label ->
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SecondaryTextColor)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Day numbers cells
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
                                    val isSelected = date in selectedDays
                                    val isBooked = date in bookedDays
                                    val isAssigned = date in assignedDays

                                    val bgColor = when {
                                        isAssigned -> Color(0xFF00796B)
                                        isBooked -> Color(0xFF1565C0)
                                        isSelected -> Color(0xFF81C784)
                                        else -> Color.Transparent
                                    }
                                    val textColor = when {
                                        isAssigned || isBooked || isSelected -> Color.White
                                        isPast -> SecondaryTextColor.copy(alpha = 0.4f)
                                        else -> DarkTextColor
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(bgColor)
                                            .clickable(enabled = !isPast && !isAssigned && !isBooked) {
                                                selectedDays = if (date in selectedDays) selectedDays - date else selectedDays + date
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("$day", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. Booking Confirmation Button ---
        Button(
            onClick = {
                bookedDays = bookedDays + selectedDays
                selectedDays = emptySet()
                showSuccessSnackbar = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = selectedDays.isNotEmpty(),
            shape = RoundedCornerShape(26.dp), // Rounded pill button matching image
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE2E2E2), // Light gray disabled background
                contentColor = Color(0xFF7F7F7F),
                disabledContainerColor = Color(0xFFE2E2E2),
                disabledContentColor = Color(0xFF7F7F7F)
            )
        ) {
            // Apply green active colors if selectedDays has items
            val activeColor = if (selectedDays.isNotEmpty()) Color(0xFF00796B) else Color(0xFF7F7F7F)
            Text(
                text = "Xác nhận đăng ký lịch",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = if (selectedDays.isNotEmpty()) Color.White else activeColor
            )
        }

        if (showSuccessSnackbar) {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                Text("Đăng ký lịch thành công!", color = Color(0xFF2E7D32), fontSize = 12.sp, modifier = Modifier.padding(12.dp))
            }
        }
    }
}

// ─── Tab 3: Guide Profile Content ──────────────────────────────────────────

@Composable
private fun GuideProfileContent(
    guide: GuideAccount,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F5F9))
            .verticalScroll(rememberScrollState())
    ) {
        // Profile Header with Green Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF004D40), Color(0xFF00796B))
                    )
                )
                .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.size(96.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    border = BorderStroke(2.dp, Color.White.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = guide.name,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = guide.handle,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${guide.rating} · ${guide.totalTours} tour",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(24.dp))

            // Info rows styled inside card menus
            Text(
                text = "Thông tin cá nhân",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF5A6E85),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    ProfileInfoRowItem(
                        icon = Icons.Default.Phone,
                        label = "Điện thoại",
                        value = guide.phone
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    ProfileInfoRowItem(
                        icon = Icons.Default.Explore,
                        label = "Chuyên môn",
                        value = guide.specialty
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    ProfileInfoRowItem(
                        icon = Icons.Default.History,
                        label = "Tổng tour",
                        value = "${guide.totalTours} tour đã dẫn"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFEBEE),
                    contentColor = Color(0xFFC62828)
                ),
                border = BorderStroke(1.dp, Color(0xFFFFCDD2))
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
private fun ProfileInfoRowItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = Color(0xFFE8F5E9)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF00796B),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                color = Color(0xFF1E293B),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

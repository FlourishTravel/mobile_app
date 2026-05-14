package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*

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

// ─── Tab 1: Book lịch (placeholder) ──────────────────────────────────────────

@Composable
private fun GuideBookingPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            color = LightGreenBackground
        ) {
            Icon(
                Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.padding(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Book lịch",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkTextColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Tính năng đang được phát triển\nvà sẽ sớm ra mắt!",
            fontSize = 14.sp,
            color = SecondaryTextColor,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = LightGreenBackground
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Construction, null, tint = PrimaryGreen, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Sắp ra mắt", color = PrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
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

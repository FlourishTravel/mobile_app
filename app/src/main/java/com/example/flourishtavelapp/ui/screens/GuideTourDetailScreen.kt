package com.example.flourishtavelapp.ui.screens

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideTourDetailScreen(
    tour: GuideTour,
    onBack: () -> Unit,
    onCustomerListClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusColor = Color(tour.status.color)
    
    // Expanded map state for accordion (Default Day 1 is expanded, or active day is expanded)
    val expandedDays = remember {
        mutableStateMapOf<Int, Boolean>().apply {
            tour.itinerary.forEach { day ->
                if (day.isCurrent) put(day.day, true)
            }
            if (isEmpty()) put(1, true)
        }
    }

    Box(modifier = modifier.fillMaxSize().background(NatureGreenBackground)) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // ── 1. Synchronized Slim White Header ───────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF5A6E85)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Chi tiết Tour",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = Color(0xFF00796B), // Integrated Teal/Green color
                    modifier = Modifier.weight(1f)
                )

                // Status Badge at top right
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = statusColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = tour.status.label,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            HorizontalDivider(thickness = 1.dp, color = Color(0xFFE2E8F0))

            // ── 2. Scrollable Details Area ──────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // ── Tour Card (Formatted like passenger's booked tour card) ──────────
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFE5E5E5)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left rounded image
                        val imageRes = if (tour.name.contains("Đà Nẵng")) {
                            com.example.flourishtavelapp.R.drawable.travel_bg
                        } else if (tour.name.contains("Bali")) {
                            com.example.flourishtavelapp.R.drawable.maya_bg
                        } else {
                            com.example.flourishtavelapp.R.drawable.bangkook_bg
                        }
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(105.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )

                        // Right tour metadata
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tour.id,
                                color = Color(0xFF757575),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            
                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = tour.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1E272C),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Star Rating & Review (Like passenger's card)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Rating Star",
                                    tint = Color(0xFFFFB300),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "4.9 (128) • Đang diễn ra",
                                    color = Color(0xFF757575),
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            // Location Pin
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = "Location Pin",
                                    tint = Color(0xFF9E9E9E),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = tour.destination,
                                    color = Color(0xFF757575),
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Tags (pastel backgrounds like passenger's card)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFF3E5F5)
                                ) {
                                    Text(
                                        text = "HDV Tiếng Việt",
                                        color = Color(0xFF7B1FA2),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFE1F5FE)
                                ) {
                                    Text(
                                        text = "Đã xác nhận",
                                        color = Color(0xFF0288D1),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Info Cards Row (Khởi hành, Thời gian, Khách) ────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GuideTourInfoCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.CalendarMonth,
                        label = "Khởi hành",
                        value = tour.startDate
                    )
                    GuideTourInfoCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Timer,
                        label = "Thời gian",
                        value = "${tour.durationDays} ngày"
                    )
                    GuideTourInfoCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Group,
                        label = "Khách",
                        value = "${tour.totalCustomers} người"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Meeting Point Card ──────────────────────────────────────────────────
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = Color(0xFFE8F5E9)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.PinDrop,
                                    contentDescription = null,
                                    tint = Color(0xFF00796B),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Điểm tập trung",
                                fontSize = 11.sp,
                                color = SecondaryTextColor,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = tour.meetingPoint,
                                fontSize = 14.sp,
                                color = DarkTextColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Itinerary Section Header ───────────────────────────────────────────
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(4.dp, 20.dp)
                            .background(Color(0xFF00796B), RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Lịch trình chi tiết",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextColor
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Expandable Accordion Timeline (Redesigned matching passenger detail) ──
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    tour.itinerary.forEachIndexed { index, day ->
                        val isLast = index == tour.itinerary.lastIndex
                        val isExpanded = expandedDays[day.day] ?: false

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            // Left Timeline Column
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .width(36.dp)
                                    .fillMaxHeight()
                            ) {
                                // Top vertical line segment
                                if (index > 0) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(20.dp)
                                            .background(Color(0xFFCCD5C7))
                                    )
                                } else {
                                    Spacer(modifier = Modifier.height(20.dp))
                                }

                                // Circular indicator dot for the day
                                val dotColor = if (day.isCurrent) Color(0xFF00796B) else Color(0xFFB0BEC5)
                                val dotSize = if (day.isCurrent) 16.dp else 12.dp
                                Surface(
                                    modifier = Modifier.size(dotSize),
                                    shape = CircleShape,
                                    color = dotColor,
                                    border = if (day.isCurrent) BorderStroke(2.dp, Color.White) else null
                                ) {}

                                // Bottom vertical line segment
                                if (!isLast) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .weight(1f)
                                            .background(Color(0xFFCCD5C7))
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

                            // Right Expandable Day Accordion Card
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(bottom = 16.dp)
                                    .clickable {
                                        expandedDays[day.day] = !isExpanded
                                    },
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    // Header Row (Day title and chevron indicator)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Ngày ${day.day}: ${day.title}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color(0xFF1E272C)
                                        )
                                        
                                        Icon(
                                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Expand Accordion",
                                            tint = Color(0xFF00796B),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    // Expanded details withParsed Activities
                                    if (isExpanded) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        
                                        // Italic subtitle matching user photo mockup
                                        Text(
                                            text = when (day.day) {
                                                1 -> "Chào mừng bạn đến với thủ đô rực rỡ."
                                                2 -> "Khám phá lịch sử và kỳ quan thiên nhiên."
                                                3 -> "Tìm về không gian văn hóa chậm rãi."
                                                4 -> "Trải nghiệm nghệ thuật và tầm nhìn trên cao."
                                                else -> "Tạm biệt và hẹn gặp lại du khách."
                                            },
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                            color = SecondaryTextColor,
                                            fontSize = 13.sp
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Parsed list of activities
                                        val activities = remember(day.description) {
                                            day.description.split("\n").filter { it.isNotBlank() }
                                        }

                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            activities.forEachIndexed { actIndex, activity ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                                ) {
                                                    // Dynamic check circle indicators based on tour status
                                                    val isCompleted = day.isCompleted || (day.isCurrent && actIndex < 2)
                                                    val isCurrent = day.isCurrent && actIndex == 2
                                                    
                                                    if (isCompleted) {
                                                        Icon(
                                                            imageVector = Icons.Default.CheckCircle,
                                                            contentDescription = "Completed",
                                                            tint = Color(0xFF00796B),
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                    } else if (isCurrent) {
                                                        // Active dot ring
                                                        Box(
                                                            modifier = Modifier.size(20.dp),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Box(
                                                                modifier = Modifier
                                                                    .size(20.dp)
                                                                    .border(2.dp, Color(0xFF00796B), CircleShape)
                                                            )
                                                            Box(
                                                                modifier = Modifier
                                                                    .size(10.dp)
                                                                    .background(Color(0xFF00796B), CircleShape)
                                                            )
                                                        }
                                                    } else {
                                                        // Outline ring
                                                        Box(
                                                            modifier = Modifier
                                                                .size(20.dp)
                                                                .border(1.5.dp, Color(0xFF81C784), CircleShape)
                                                        )
                                                    }

                                                    Text(
                                                        text = activity,
                                                        fontWeight = FontWeight.Medium,
                                                        fontSize = 14.sp,
                                                        color = Color(0xFF2C3E50),
                                                        modifier = Modifier.weight(1f)
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

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // ── 3. Fixed Bottom Button (Xem danh sách khách) ───────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, NatureGreenBackground, NatureGreenBackground)
                    )
                )
                .padding(16.dp)
        ) {
            Button(
                onClick = onCustomerListClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Icon(Icons.Default.Group, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Xem danh sách khách (${tour.customers.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

// ── Sub-Composables ───────────────────────────────────────────────────────────

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
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = DarkTextColor)
            Text(label, fontSize = 10.sp, color = SecondaryTextColor)
        }
    }
}

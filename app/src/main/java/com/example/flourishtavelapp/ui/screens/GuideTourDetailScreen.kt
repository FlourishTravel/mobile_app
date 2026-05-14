package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*

@Composable
fun GuideTourDetailScreen(
    tour: GuideTour,
    onBack: () -> Unit,
    onCustomerListClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusColor = Color(tour.status.color)

    Box(modifier = modifier.fillMaxSize().background(NatureGreenBackground)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Hero Header ───────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF004D40), PrimaryGreen)
                        )
                    )
            ) {
                // Decorative circles
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .offset(x = 120.dp, y = (-40).dp)
                        .background(Color.White.copy(alpha = 0.05f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .offset(x = 220.dp, y = 80.dp)
                        .background(Color.White.copy(alpha = 0.07f), CircleShape)
                )

                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                    // Top Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = statusColor.copy(alpha = 0.3f)
                        ) {
                            Text(
                                tour.status.label,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "CHI TIẾT TOUR",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        tour.name,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, tint = Color.White.copy(alpha = 0.85f), modifier = Modifier.size(14.dp))
                        Text(" ${tour.destination}", color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(20.dp))

                // ── Info Cards Row ────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
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

                // ── Meeting Point ─────────────────────────────────────────
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
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
                            Icon(
                                Icons.Default.PinDrop, null, tint = PrimaryGreen,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Điểm tập trung", fontSize = 11.sp, color = SecondaryTextColor, fontWeight = FontWeight.SemiBold)
                            Text(tour.meetingPoint, fontSize = 13.sp, color = DarkTextColor, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Itinerary Header ──────────────────────────────────────
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(4.dp, 20.dp)
                            .background(PrimaryGreen, RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Lịch trình chi tiết",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextColor
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Itinerary Timeline ────────────────────────────────────
                tour.itinerary.forEachIndexed { index, day ->
                    val isLast = index == tour.itinerary.lastIndex
                    GuideDayTimelineItem(day = day, isLast = isLast)
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // ── Fixed Bottom Button ───────────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, NatureGreenBackground, NatureGreenBackground)
                    )
                )
                .padding(20.dp)
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
                    "Xem danh sách khách (${tour.customers.size})",
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
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkTextColor)
            Text(label, fontSize = 10.sp, color = SecondaryTextColor)
        }
    }
}

@Composable
private fun GuideDayTimelineItem(day: GuideTourDay, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Timeline column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(44.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = when {
                            day.isCompleted -> PrimaryGreen
                            day.isCurrent -> Color.White
                            else -> Color.White
                        },
                        shape = CircleShape
                    )
                    .then(
                        if (day.isCurrent) Modifier.border(2.dp, PrimaryGreen, CircleShape)
                        else if (day.isCompleted) Modifier
                        else Modifier.border(1.dp, Color.LightGray, CircleShape)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (day.isCompleted) {
                    Icon(
                        Icons.Default.Check, null, tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        "D${day.day}",
                        color = if (day.isCurrent) PrimaryGreen else Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(if (day.isCurrent) 0.dp else 16.dp)
                        .background(
                            if (day.isCompleted) PrimaryGreen else Color.LightGray,
                            RoundedCornerShape(1.dp)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Content
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    day.isCurrent -> Color.White
                    day.isCompleted -> LightGreenBackground.copy(alpha = 0.5f)
                    else -> Color.White.copy(alpha = 0.7f)
                }
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (day.isCurrent) 4.dp else 0.dp
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Ngày ${day.day}",
                        color = if (day.isCurrent) PrimaryGreen else SecondaryTextColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (day.isCurrent) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PrimaryGreen
                        ) {
                            Text(
                                "HÔM NAY",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    day.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DarkTextColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    day.description,
                    fontSize = 12.sp,
                    color = SecondaryTextColor,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

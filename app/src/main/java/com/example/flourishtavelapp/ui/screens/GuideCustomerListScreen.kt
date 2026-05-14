package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.flourishtavelapp.ui.theme.*

@Composable
fun GuideCustomerListScreen(
    tour: GuideTour,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val paidCount = tour.customers.count { it.paymentStatus == PaymentStatus.PAID }
    val depositCount = tour.customers.count { it.paymentStatus == PaymentStatus.DEPOSIT }
    val pendingCount = tour.customers.count { it.paymentStatus == PaymentStatus.PENDING }
    val totalPeople = tour.customers.sumOf { it.adultCount + it.childCount }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<PaymentStatus?>(null) }

    val filteredCustomers = tour.customers.filter { customer ->
        val matchesSearch = searchQuery.isEmpty() ||
                customer.name.contains(searchQuery, ignoreCase = true) ||
                customer.phone.contains(searchQuery)
        val matchesFilter = selectedFilter == null || customer.paymentStatus == selectedFilter
        matchesSearch && matchesFilter
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
    ) {
        // ── Header ────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF004D40), PrimaryGreen)
                    )
                )
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        }

        // ── Filter Chips ──────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomerFilterChip(
                label = "Tất cả",
                selected = selectedFilter == null,
                onClick = { selectedFilter = null }
            )
            CustomerFilterChip(
                label = "Đã TT ($paidCount)",
                selected = selectedFilter == PaymentStatus.PAID,
                onClick = { selectedFilter = if (selectedFilter == PaymentStatus.PAID) null else PaymentStatus.PAID },
                activeColor = Color(0xFF2E7D32)
            )
            CustomerFilterChip(
                label = "Cọc ($depositCount)",
                selected = selectedFilter == PaymentStatus.DEPOSIT,
                onClick = { selectedFilter = if (selectedFilter == PaymentStatus.DEPOSIT) null else PaymentStatus.DEPOSIT },
                activeColor = Color(0xFF1565C0)
            )
            CustomerFilterChip(
                label = "Chờ ($pendingCount)",
                selected = selectedFilter == PaymentStatus.PENDING,
                onClick = { selectedFilter = if (selectedFilter == PaymentStatus.PENDING) null else PaymentStatus.PENDING },
                activeColor = Color(0xFFE65100)
            )
        }

        // ── Search Bar ────────────────────────────────────────────────────
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, null, tint = SecondaryTextColor, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                if (searchQuery.isEmpty()) {
                    Text("Tìm theo tên hoặc SĐT...", color = SecondaryTextColor.copy(alpha = 0.6f), fontSize = 14.sp)
                }
                // Simple text input via BasicTextField would be ideal, using Text for simplicity
            }
        }

        // ── Customer List ─────────────────────────────────────────────────
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            item {
                Text(
                    "Hiển thị ${filteredCustomers.size} khách",
                    color = SecondaryTextColor,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            items(filteredCustomers) { customer ->
                CustomerCard(customer = customer)
            }
        }
    }
}

// ── Sub-Composables ───────────────────────────────────────────────────────────

@Composable
private fun CustomerStatChip(
    modifier: Modifier,
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.15f)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = color, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Text(label, color = Color.White.copy(alpha = 0.75f), fontSize = 9.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
private fun CustomerFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    activeColor: Color = PrimaryGreen
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) activeColor else Color.White,
        shadowElevation = if (selected) 2.dp else 0.dp
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            color = if (selected) Color.White else SecondaryTextColor,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun CustomerCard(customer: TourCustomer) {
    val statusColor = Color(customer.paymentStatus.color)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Name row + status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = PrimaryGreen.copy(alpha = 0.12f)
                    ) {
                        Icon(
                            if (customer.gender == "Nam") Icons.Default.PersonOutline
                            else Icons.Default.PersonOutline,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            customer.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = DarkTextColor
                        )
                        Text(
                            customer.gender,
                            color = SecondaryTextColor,
                            fontSize = 12.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        customer.paymentStatus.label,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))

            // Details grid
            Row(modifier = Modifier.fillMaxWidth()) {
                CustomerInfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Phone,
                    label = "Điện thoại",
                    value = customer.phone
                )
                CustomerInfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Badge,
                    label = "CCCD/CMND",
                    value = customer.idCard
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                CustomerInfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.PersonAdd,
                    label = "Người lớn",
                    value = "${customer.adultCount} người"
                )
                CustomerInfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.ChildCare,
                    label = "Trẻ em",
                    value = "${customer.childCount} người"
                )
            }

            // Note if exists
            if (customer.note.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFFF8E1)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info, null,
                            tint = Color(0xFFF57F17),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            customer.note,
                            color = Color(0xFFE65100),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Call button
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { /* Call intent */ },
                modifier = Modifier.fillMaxWidth().height(40.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryGreen),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryGreen.copy(alpha = 0.5f))
            ) {
                Icon(Icons.Default.Phone, null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Gọi điện", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun CustomerInfoItem(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = PrimaryGreen, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(label, color = SecondaryTextColor, fontSize = 10.sp)
            Text(value, color = DarkTextColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}

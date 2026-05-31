package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<PaymentStatus?>(null) }

    val filteredCustomers = tour.customers.filter { customer ->
        val matchesSearch = searchQuery.isEmpty() ||
                customer.name.contains(searchQuery, ignoreCase = true) ||
                customer.phone.contains(searchQuery)
        val matchesFilter = selectedFilter == null || customer.paymentStatus == selectedFilter
        matchesSearch && matchesFilter
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add Customer Mock */ },
                containerColor = Color(0xFF004D40), // Dark Green
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 12.dp, end = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Add Customer",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        bottomBar = {
            GuideCustomerBottomNavigation()
        },
        containerColor = NatureGreenBackground
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .background(NatureGreenBackground)
        ) {
            // ── Header (Gradient Box matching image) ────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF004D40), Color(0xFF00796B))
                        )
                    )
                    .statusBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Row 1: Back arrow, Title, Search Icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = "Quản lý khách hàng",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }
                    }
                    
                    // Row 2: Search field matching image
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Tìm theo tên hoặc SĐT...", fontSize = 14.sp, color = Color(0xFF94A3B8)) },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color(0xFF00796B)
                        ),
                        singleLine = true
                    )
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
                    onClick = { selectedFilter = if (selectedFilter == PaymentStatus.PAID) null else PaymentStatus.PAID }
                )
                CustomerFilterChip(
                    label = "Cọc ($depositCount)",
                    selected = selectedFilter == PaymentStatus.DEPOSIT,
                    onClick = { selectedFilter = if (selectedFilter == PaymentStatus.DEPOSIT) null else PaymentStatus.DEPOSIT }
                )
            }

            // ── Customer List ─────────────────────────────────────────────────
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
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
}

// ── Sub-Composables ───────────────────────────────────────────────────────────

@Composable
private fun CustomerFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    activeColor: Color = Color(0xFF004D40) // Dark Emerald Green
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (selected) activeColor else Color(0xFFE8EEF5)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            color = if (selected) Color.White else Color(0xFF4A5568),
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun CustomerCard(customer: TourCustomer) {
    val statusColor = Color(customer.paymentStatus.color)
    val statusBgColor = when (customer.paymentStatus) {
        PaymentStatus.PAID -> Color(0xFFE8F5E9)
        PaymentStatus.DEPOSIT -> Color(0xFFE3F2FD)
        PaymentStatus.PENDING -> Color(0xFFFFF3E0)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
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
                        color = Color(0xFFE8F5E9)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF00796B),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = customer.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = customer.gender,
                            color = Color(0xFF64748B),
                            fontSize = 12.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusBgColor
                ) {
                    Text(
                        text = customer.paymentStatus.label,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Details grid - Row 1
            Row(modifier = Modifier.fillMaxWidth()) {
                CustomerInfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.Phone,
                    label = "Điện thoại",
                    value = customer.phone
                )
                CustomerInfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.Badge,
                    label = "CCCD/CMND",
                    value = customer.idCard
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Details grid - Row 2
            Row(modifier = Modifier.fillMaxWidth()) {
                CustomerInfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.Group,
                    label = "Người lớn",
                    value = "${customer.adultCount} người"
                )
                CustomerInfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.ChildCare,
                    label = "Trẻ em",
                    value = "${customer.childCount} người"
                )
            }

            // Note if exists
            if (customer.note.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
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
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFFF57F17),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = customer.note,
                            color = Color(0xFFE65100),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Call button
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00796B)),
                border = BorderStroke(1.dp, Color(0xFF00796B))
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = Color(0xFF00796B),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Gọi điện",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CustomerInfoItem(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label.uppercase(),
                color = Color(0xFF94A3B8),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                color = Color(0xFF1E293B),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun GuideCustomerBottomNavigation() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(72.dp)
    ) {
        data class NavigationItem(val label: String, val icon: ImageVector, val isSelected: Boolean)
        val items = listOf(
            NavigationItem("Trang chủ", Icons.Default.Home, false),
            NavigationItem("Khách hàng", Icons.Default.People, true),
            NavigationItem("Lịch hẹn", Icons.Outlined.CalendarMonth, false),
            NavigationItem("Cài đặt", Icons.Default.Settings, false)
        )
        items.forEach { item ->
            NavigationBarItem(
                selected = item.isSelected,
                onClick = { },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(height = 30.dp, width = 50.dp)
                            .background(
                                color = if (item.isSelected) Color(0xFFE8F5E9) else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (item.isSelected) Color(0xFF004D40) else Color(0xFF757575),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (item.isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (item.isSelected) Color(0xFF004D40) else Color(0xFF757575)
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}

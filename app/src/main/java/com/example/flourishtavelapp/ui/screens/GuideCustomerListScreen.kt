package com.example.flourishtravelapp.ui.screens

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
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.mapper.toGuideTour
import com.example.flourishtravelapp.ui.theme.*

@Composable
fun GuideCustomerListScreen(
    tour: GuideTour,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var tourWithCustomers by remember(tour.sessionId) { mutableStateOf(tour) }
    var isLoading by remember(tour.sessionId) { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(tour.sessionId) {
        isLoading = true
        loadError = null
        try {
            val response = RetrofitClient.guideApiService.getSessionGuests(tour.sessionId)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let { tourWithCustomers = it.toGuideTour(tour) }
            } else {
                loadError = response.body()?.message ?: "Không tải được danh sách khách"
            }
        } catch (e: Exception) {
            loadError = e.localizedMessage ?: "Lỗi kết nối"
        } finally {
            isLoading = false
        }
    }

    val displayTour = tourWithCustomers
    val paidCount = displayTour.customers.count { it.paymentStatus == PaymentStatus.PAID }
    val depositCount = displayTour.customers.count { it.paymentStatus == PaymentStatus.DEPOSIT }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<PaymentStatus?>(null) }

    val filteredCustomers = displayTour.customers.filter { customer ->
        val matchesSearch = searchQuery.isEmpty() ||
                customer.name.contains(searchQuery, ignoreCase = true) ||
                customer.phone.contains(searchQuery)
        val matchesFilter = selectedFilter == null || customer.paymentStatus == selectedFilter
        matchesSearch && matchesFilter
    }

    Scaffold(containerColor = NatureGreenBackground) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .background(NatureGreenBackground)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(colors = listOf(Color(0xFF004D40), Color(0xFF00796B))))
                    .statusBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Quản lý khách hàng",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Tìm theo tên hoặc SĐT...", fontSize = 14.sp, color = Color(0xFF94A3B8)) },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp)) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
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

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomerFilterChip("Tất cả", selectedFilter == null, onClick = { selectedFilter = null })
                CustomerFilterChip("Đã TT ($paidCount)", selectedFilter == PaymentStatus.PAID, onClick = {
                    selectedFilter = if (selectedFilter == PaymentStatus.PAID) null else PaymentStatus.PAID
                })
                CustomerFilterChip("Cọc ($depositCount)", selectedFilter == PaymentStatus.DEPOSIT, onClick = {
                    selectedFilter = if (selectedFilter == PaymentStatus.DEPOSIT) null else PaymentStatus.DEPOSIT
                })
            }

            if (isLoading && displayTour.customers.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            } else {
                loadError?.let { error ->
                    Text(error, color = Color(0xFFC62828), fontSize = 13.sp, modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    item {
                        Text("Hiển thị ${filteredCustomers.size} khách", color = SecondaryTextColor, fontSize = 12.sp, modifier = Modifier.padding(bottom = 4.dp))
                    }
                    items(filteredCustomers) { customer -> CustomerCard(customer = customer) }
                }
            }
        }
    }
}

@Composable
private fun CustomerFilterChip(label: String, selected: Boolean, onClick: () -> Unit, activeColor: Color = Color(0xFF004D40)) {
    Surface(onClick = onClick, shape = RoundedCornerShape(16.dp), color = if (selected) activeColor else Color(0xFFE8EEF5)) {
        Text(label, modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), color = if (selected) Color.White else Color(0xFF4A5568), fontSize = 13.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium)
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
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = Color(0xFFE8F5E9)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Person, null, tint = Color(0xFF00796B), modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(customer.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
                        if (customer.checkedInGathering) {
                            Text("Đã check-in", fontSize = 11.sp, color = PrimaryGreen)
                        }
                    }
                }
                Surface(shape = RoundedCornerShape(8.dp), color = statusBgColor) {
                    Text(customer.paymentStatus.label, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                CustomerInfoItem(Modifier.weight(1f), Icons.Outlined.Phone, "Điện thoại", customer.phone)
                CustomerInfoItem(Modifier.weight(1f), Icons.Outlined.Badge, "CCCD/CMND", customer.idCard.ifBlank { "—" })
            }
            if (customer.pickupAddress.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Đón: ${customer.pickupAddress}", fontSize = 12.sp, color = SecondaryTextColor)
            }
            if (customer.note.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = Color(0xFFFFF8E1)) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, tint = Color(0xFFF57F17), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(customer.note, color = Color(0xFFE65100), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomerInfoItem(modifier: Modifier, icon: ImageVector, label: String, value: String) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(label.uppercase(), color = Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, color = Color(0xFF1E293B), fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}

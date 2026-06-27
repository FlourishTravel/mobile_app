package com.example.flourishtravelapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flourishtravelapp.R
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.TicketCardDto
import com.example.flourishtravelapp.data.util.resolveMediaUrl
import com.example.flourishtravelapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(
    slug: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var ticket by remember { mutableStateOf<TicketCardDto?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(slug) {
        isLoading = true
        error = null
        try {
            val response = RetrofitClient.catalogApiService.getTicketBySlug(slug)
            if (response.isSuccessful && response.body()?.success == true) {
                ticket = response.body()?.data
                if (ticket == null) {
                    error = "Không tìm thấy hoạt động"
                }
            } else {
                error = response.body()?.message ?: "Không tải được chi tiết"
            }
        } catch (e: Exception) {
            error = e.localizedMessage ?: "Lỗi kết nối"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết vé", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(error ?: "", color = Color(0xFFC62828))
                }
            }
            ticket != null -> {
                val detail = ticket!!
                val imageUrl = resolveMediaUrl(detail.imageUrl)
                val fallbackRes = R.drawable.travel_bg

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    ) {
                        if (!imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = detail.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(fallbackRes),
                                error = painterResource(fallbackRes)
                            )
                        } else {
                            Image(
                                painter = painterResource(fallbackRes),
                                contentDescription = detail.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(20.dp)) {
                        when {
                            detail.featured == true -> {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFE8F5E9)
                                ) {
                                    Text(
                                        "Nổi bật",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        color = PrimaryGreen,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(Modifier.height(10.dp))
                            }
                            detail.eTicket == true -> {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFE3F2FD)
                                ) {
                                    Text(
                                        "Vé điện tử",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        color = Color(0xFF1E88E5),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(Modifier.height(10.dp))
                            }
                        }

                        Text(
                            detail.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextColor
                        )

                        Spacer(Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, tint = SecondaryTextColor, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(
                                detail.destinationCity ?: detail.locationLabel ?: "Thái Lan",
                                color = SecondaryTextColor,
                                fontSize = 14.sp
                            )
                        }

                        detail.showTimeLabel?.takeIf { it.isNotBlank() }?.let {
                            Spacer(Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, null, tint = SecondaryTextColor, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(it, color = SecondaryTextColor, fontSize = 14.sp)
                            }
                        }

                        detail.routeLabel?.takeIf { it.isNotBlank() }?.let {
                            Spacer(Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Route, null, tint = SecondaryTextColor, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(it, color = SecondaryTextColor, fontSize = 14.sp)
                            }
                        }

                        detail.rating?.let { rating ->
                            Spacer(Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("$rating", fontWeight = FontWeight.SemiBold, color = DarkTextColor)
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        detail.shortDescription?.takeIf { it.isNotBlank() }?.let { desc ->
                            Text("Mô tả", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
                            Spacer(Modifier.height(6.dp))
                            Text(desc, color = SecondaryTextColor, fontSize = 14.sp, lineHeight = 20.sp)
                            Spacer(Modifier.height(16.dp))
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = LightGreenBackground)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Giá từ", fontSize = 12.sp, color = SecondaryTextColor)
                                    Text(
                                        detail.priceLabel ?: "Liên hệ",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFF5722)
                                    )
                                }
                                Button(
                                    onClick = { },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                                ) {
                                    Text("Liên hệ đặt vé")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

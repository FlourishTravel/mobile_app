package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.ReviewView
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(
    isLoggedIn: Boolean,
    onBack: () -> Unit,
    onRequiresLogin: () -> Unit
) {
    var tab by remember { mutableStateOf(0) } // 0 featured, 1 public, 2 me
    var reviews by remember { mutableStateOf<List<ReviewView>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(tab, isLoggedIn) {
        isLoading = true
        try {
            val resp = when (tab) {
                0 -> RetrofitClient.reviewApiService.getFeaturedReviews()
                1 -> RetrofitClient.reviewApiService.getPublicReviews()
                else -> RetrofitClient.reviewApiService.getMyReviews()
            }
            if (resp.isSuccessful) reviews = resp.body()?.data ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đánh giá", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground)
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding)) {
            TabRow(selectedTabIndex = tab, containerColor = NatureGreenBackground) {
                Tab(tab == 0, { tab = 0 }) { Text("Nổi bật", color = DarkTextColor) }
                Tab(tab == 1, { tab = 1 }) { Text("Cộng đồng", color = DarkTextColor) }
                Tab(tab == 2, { tab = 2 }) { Text("Của tôi", color = DarkTextColor) }
            }
            if (tab == 2 && !isLoggedIn) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = onRequiresLogin, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005b41))) {
                        Text("Đăng nhập để xem đánh giá của bạn", color = Color.White)
                    }
                }
            } else if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryGreen) }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(reviews) { r -> ReviewCard(r) }
                }
            }
        }
    }
}

@Composable
private fun ReviewCard(r: ReviewView) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(14.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(r.userName ?: "Khách", fontWeight = FontWeight.Bold, color = DarkTextColor)
                Text("⭐ ${r.rating ?: "-"}", color = Color(0xFFF59E0B), fontWeight = FontWeight.Bold)
            }
            r.tourTitle?.let { Text("Tour: $it", fontSize = 12.sp, color = Color(0xFF005b41)) }
            r.comment?.let {
                Spacer(Modifier.height(4.dp))
                Text(it, fontSize = 14.sp, color = Color(0xFF475569))
            }
        }
    }
}

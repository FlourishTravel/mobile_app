package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.DestinationDetail
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationDetailScreen(
    slug: String,
    onBack: () -> Unit,
    onTourClick: (String) -> Unit = {}
) {
    var detail by remember { mutableStateOf<DestinationDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(slug) {
        isLoading = true
        try {
            val response = RetrofitClient.destinationApiService.getDestinationDetail(slug)
            if (response.isSuccessful) detail = response.body()?.data
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(detail?.name ?: "Điểm đến", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground)
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else {
            val d = detail
            if (d == null) {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text("Không tìm thấy điểm đến", color = Color(0xFF64748B))
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(NatureGreenBackground)
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        AsyncImage(
                            model = d.heroImageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(18.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    item {
                        Text(d.name, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = DarkTextColor)
                        d.summary?.let {
                            Spacer(Modifier.height(6.dp))
                            Text(it, fontSize = 14.sp, color = Color(0xFF64748B))
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            InfoChip("⭐ ${d.rating ?: "-"}", Color(0xFFF59E0B))
                            InfoChip("🌡 ${d.avgTemperatureC ?: "?"}°C", Color(0xFFEF4444))
                            InfoChip("📅 ${d.idealDaysMin ?: "?"}-${d.idealDaysMax ?: "?"} ngày", Color(0xFF005b41))
                        }
                        d.weatherNow?.let {
                            Spacer(Modifier.height(8.dp))
                            Text("Thời tiết hiện tại: $it", fontSize = 13.sp, color = Color(0xFF475569))
                        }
                    }
                    d.description?.let {
                        item {
                            SectionTitle("Giới thiệu")
                            Text(it, fontSize = 14.sp, color = Color(0xFF475569))
                        }
                    }
                    d.attractions?.let { list ->
                        if (list.isNotEmpty()) items(list) { a ->
                            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                                Row {
                                    AsyncImage(model = a.imageUrl, contentDescription = null, modifier = Modifier.size(90.dp).clip(RoundedCornerShape(14.dp)), contentScale = ContentScale.Crop)
                                    Column(Modifier.padding(12.dp)) {
                                        Text(a.name, fontWeight = FontWeight.Bold, color = DarkTextColor)
                                        a.ticketPriceLabel?.let { Text(it, fontSize = 12.sp, color = Color(0xFF64748B)) }
                                        a.openHours?.let { Text("🕒 $it", fontSize = 12.sp, color = Color(0xFF64748B)) }
                                    }
                                }
                            }
                        }
                    }
                    d.costItems?.let { list ->
                        if (list.isNotEmpty()) item {
                            SectionTitle("Chi phí tham khảo")
                            list.forEach { c ->
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(c.label ?: c.category ?: "", fontSize = 13.sp, color = Color(0xFF475569))
                                    Text("${c.costMinMillion ?: "?"}–${c.costMaxMillion ?: "?"} triệu", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = DarkTextColor)
                                }
                                Spacer(Modifier.height(4.dp))
                            }
                        }
                    }
                    d.reviews?.let { list ->
                        if (list.isNotEmpty()) item {
                            SectionTitle("Đánh giá (${list.size})")
                            list.forEach { r ->
                                Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(r.authorName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkTextColor)
                                        Spacer(Modifier.width(6.dp))
                                        Text("⭐ ${r.rating}", fontSize = 12.sp, color = Color(0xFFF59E0B))
                                    }
                                    r.comment?.let { Text(it, fontSize = 13.sp, color = Color(0xFF475569)) }
                                }
                            }
                        }
                    }
                    d.suggestedTours?.let { list ->
                        if (list.isNotEmpty()) items(list) { t ->
                            Card(
                                Modifier.fillMaxWidth().clickable { t.slug?.let { onTourClick(it) } },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5))
                            ) {
                                Row {
                                    AsyncImage(model = t.thumbnailUrl, contentDescription = null, modifier = Modifier.size(80.dp), contentScale = ContentScale.Crop)
                                    Column(Modifier.padding(12.dp)) {
                                        Text(t.title, fontWeight = FontWeight.Bold, color = DarkTextColor)
                                        Text("${t.durationLabel ?: ""} • ${t.basePrice ?: ""}₫", fontSize = 12.sp, color = Color(0xFF005b41))
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

@Composable
private fun InfoChip(text: String, tint: Color) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = tint.copy(alpha = 0.12f)
    ) {
        Text(text, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), fontSize = 13.sp, color = tint, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
}

package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.flourishtravelapp.data.model.ThaiFestivalDetail
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FestivalDetailScreen(
    festivalSlug: String,
    onBack: () -> Unit,
    onDestinationClick: (String) -> Unit = {}
) {
    var detail by remember { mutableStateOf<ThaiFestivalDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(festivalSlug) {
        isLoading = true
        try {
            val response = RetrofitClient.destinationApiService.getFestivalDetail(festivalSlug)
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
                title = { Text(detail?.name ?: "Lễ hội", fontWeight = FontWeight.Bold, color = DarkTextColor) },
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
                    Text("Không tìm thấy lễ hội", color = Color(0xFF64748B))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        AsyncImage(model = d.imageUrl, contentDescription = null, modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(18.dp)), contentScale = ContentScale.Crop)
                    }
                    item {
                        Text(d.name, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = DarkTextColor)
                        d.monthLabel?.let {
                            Spacer(Modifier.height(4.dp))
                            Text(it, fontSize = 14.sp, color = Color(0xFF8B5CF6), fontWeight = FontWeight.Bold)
                        }
                        d.description?.let {
                            Spacer(Modifier.height(8.dp))
                            Text(it, fontSize = 14.sp, color = Color(0xFF475569))
                        }
                    }
                    d.longDescription?.let {
                        item {
                            Text(it, fontSize = 14.sp, color = Color(0xFF475569))
                        }
                    }
                    d.relatedDestinationName?.let {
                        item {
                            Card(
                                Modifier.fillMaxWidth().clickable { d.relatedDestinationSlug?.let { s -> onDestinationClick(s) } },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5))
                            ) {
                                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Place, null, tint = Color(0xFF005b41))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Điểm đến liên quan: $it", fontWeight = FontWeight.Bold, color = Color(0xFF005b41))
                                }
                            }
                        }
                    }
                    d.tips?.let { tips ->
                        if (tips.isNotEmpty()) item {
                            Text("💡 Mẹo tham gia", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
                            tips.forEach { t ->
                                Row(Modifier.padding(vertical = 3.dp)) {
                                    Text("• ", color = Color(0xFF005b41), fontWeight = FontWeight.Bold)
                                    Text(t, fontSize = 13.sp, color = Color(0xFF475569))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

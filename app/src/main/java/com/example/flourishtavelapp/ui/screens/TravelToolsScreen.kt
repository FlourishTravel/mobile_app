package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WbSunny
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
import com.example.flourishtravelapp.data.model.NearbyPlace
import com.example.flourishtravelapp.data.model.WeatherForecast
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelToolsScreen(
    isLoggedIn: Boolean,
    onBack: () -> Unit,
    onRequiresLogin: () -> Unit
) {
    var destination by remember { mutableStateOf("Bangkok") }
    var weather by remember { mutableStateOf<WeatherForecast?>(null) }
    var nearby by remember { mutableStateOf<NearbyPlace?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun load() {
        if (!isLoggedIn) { onRequiresLogin(); return }
        isLoading = true
        coroutineScope.launch {
            try {
                val w = RetrofitClient.chatbotApiService.getWeatherForecast(destination)
                if (w.isSuccessful) weather = w.body()?.data
                val n = RetrofitClient.chatbotApiService.getNearbyPlaces(destination)
                if (n.isSuccessful) nearby = n.body()?.data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) { load() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Công cụ du lịch", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground)
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Điểm đến") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF005b41))
            )
            Button(
                onClick = { load() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005b41)),
                enabled = !isLoading
            ) {
                Text("Tra cứu", color = Color.White, fontWeight = FontWeight.Bold)
            }

            if (isLoading) {
                CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            weather?.let { w ->
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WbSunny, null, tint = Color(0xFFF59E0B))
                            Spacer(Modifier.width(8.dp))
                            Text("Thời tiết${w.destination?.let { " - $it" } ?: ""}", fontWeight = FontWeight.Bold, color = DarkTextColor)
                        }
                        w.summary?.let { Text(it, fontSize = 13.sp, color = Color(0xFF475569), modifier = Modifier.padding(top = 4.dp)) }
                        w.days?.let { days ->
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.heightIn(max = 200.dp)) {
                                items(days) { d ->
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(d.date ?: "", fontSize = 13.sp, color = Color(0xFF475569))
                                        Text("${d.condition ?: ""} ${d.tempMin ?: ""}–${d.tempMax ?: ""}", fontSize = 13.sp, color = DarkTextColor)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            nearby?.let { n ->
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, tint = Color(0xFF005b41))
                            Spacer(Modifier.width(8.dp))
                            Text("Địa điểm gần đây", fontWeight = FontWeight.Bold, color = DarkTextColor)
                        }
                        n.name?.let { Text(it, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkTextColor, modifier = Modifier.padding(top = 4.dp)) }
                        n.type?.let { Text("Loại: $it", fontSize = 13.sp, color = Color(0xFF64748B)) }
                        n.distance?.let { Text("Khoảng cách: $it", fontSize = 13.sp, color = Color(0xFF64748B)) }
                        n.rating?.let { Text("⭐ $it", fontSize = 13.sp, color = Color(0xFFF59E0B)) }
                        n.address?.let { Text(it, fontSize = 13.sp, color = Color(0xFF64748B)) }
                    }
                }
            }
        }
    }
}

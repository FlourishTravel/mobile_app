package com.example.flourishtravelapp.ui.components

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.FloraJourneyDto
import com.example.flourishtravelapp.data.model.FloraNearbyItem
import com.example.flourishtravelapp.data.model.FloraNearbyRecommendationRequest
import com.example.flourishtravelapp.data.model.FloraNearbyRecommendationResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun rememberFloraJourneyState(bookingId: String): FloraJourneyUiState {
    var journey by remember(bookingId) { mutableStateOf<FloraJourneyDto?>(null) }
    var loading by remember(bookingId) { mutableStateOf(true) }
    var error by remember(bookingId) { mutableStateOf<String?>(null) }

    LaunchedEffect(bookingId) {
        loading = true
        error = null
        try {
            val response = RetrofitClient.floraApiService.getJourney(bookingId)
            if (response.isSuccessful && response.body()?.success == true) {
                journey = response.body()?.data
            } else {
                error = response.body()?.let { "Không tải được lịch Flora" } ?: "Không tải được lịch Flora"
            }
        } catch (e: Exception) {
            error = e.message ?: "Lỗi mạng"
        } finally {
            loading = false
        }
    }

    return FloraJourneyUiState(bookingId, journey, loading, error)
}

data class FloraJourneyUiState(
    val bookingId: String,
    val journey: FloraJourneyDto?,
    val loading: Boolean,
    val error: String?
)

@Composable
fun FloraJourneyPanel(
    state: FloraJourneyUiState,
    modifier: Modifier = Modifier
) {
    when {
        state.loading -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        state.error != null -> {
            Text(
                text = state.error,
                color = Color.Gray,
                modifier = modifier.padding(16.dp)
            )
        }
        state.journey != null -> FloraJourneyContent(state.bookingId, state.journey, modifier)
    }
}

@Composable
private fun FloraJourneyContent(bookingId: String, journey: FloraJourneyDto, modifier: Modifier = Modifier) {
    var tick by remember { mutableLongStateOf(0L) }
    val meeting = journey.nextMeeting
    val confirmed = meeting?.scheduleStatus == "CONFIRMED" && meeting.reminderEligible == true
    val showCountdown = confirmed && meeting?.minutesUntil != null && meeting.minutesUntil >= 0

    LaunchedEffect(meeting?.time, confirmed) {
        if (showCountdown) {
            while (true) {
                delay(60_000)
                tick++
            }
        }
    }

    @Suppress("UNUSED_VARIABLE")
    val refresh = tick

    val countdownMinutes = if (showCountdown) meeting?.minutesUntil else null

    var nearbyOpen by remember { mutableStateOf(false) }
    var nearbyLoading by remember { mutableStateOf(false) }
    var nearbyError by remember { mutableStateOf<String?>(null) }
    var nearbyData by remember { mutableStateOf<FloraNearbyRecommendationResponse?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    suspend fun fetchNearby(lat: Double?, lon: Double?) {
        nearbyLoading = true
        nearbyError = null
        try {
            val body = FloraNearbyRecommendationRequest(
                latitude = lat,
                longitude = lon
            )
            val response = RetrofitClient.floraApiService.postNearbyRecommendations(bookingId, body)
            if (response.isSuccessful && response.body()?.success == true) {
                nearbyData = response.body()?.data
            } else {
                nearbyError = response.body()?.message ?: "Không tải gợi ý gần đây"
            }
        } catch (e: Exception) {
            nearbyError = e.message ?: "Lỗi mạng"
        } finally {
            nearbyLoading = false
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        nearbyOpen = true
        scope.launch {
            if (granted) {
                // Foreground location only on explicit user action; coordinates sent in POST body.
                fetchNearby(null, null)
            } else {
                fetchNearby(null, null)
            }
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFECFDF5)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Flora AI — Hành trình", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            journey.currentActivity?.let { act ->
                Text("Đang diễn ra: ${act.title ?: "—"}", fontWeight = FontWeight.SemiBold)
                act.endAt?.let { Text("Đến $it", style = MaterialTheme.typography.bodySmall) }
            } ?: journey.currentScheduleItem?.title?.let {
                Text("Hôm nay: $it")
            }

            journey.nextActivity?.let { act ->
                Text("Tiếp theo: ${act.title ?: "—"}")
            } ?: journey.nextScheduleItem?.title?.let {
                Text("Tiếp theo (ngày): $it")
            }

            meeting?.locationName?.let { point ->
                Text("Điểm tập trung: $point")
            } ?: journey.meetingPoint?.let { Text("Điểm tập trung: $it") }

            ScheduleBadge(meeting?.scheduleStatus)

            when {
                showCountdown && countdownMinutes != null -> {
                    Text(
                        "Còn khoảng $countdownMinutes phút đến giờ tập trung",
                        color = Color(0xFF047857),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                meeting?.scheduleStatus == "UNAVAILABLE" || meeting == null -> {
                    Text(
                        "Chưa có lịch tập trung chính thức. Hãy theo HDV hoặc nhóm chat đoàn nhé.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            journey.warnings?.forEach { w ->
                Text(w, style = MaterialTheme.typography.bodySmall, color = Color(0xFF92400E))
            }

            Button(
                onClick = {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    if (hasPermission) {
                        nearbyOpen = true
                        scope.launch { fetchNearby(null, null) }
                    } else {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                enabled = !nearbyLoading
            ) {
                Text(if (nearbyLoading) "Đang tải gợi ý..." else "Gợi ý gần đây")
            }

            if (nearbyOpen) {
                NearbyRecommendationsBlock(
                    data = nearbyData,
                    error = nearbyError,
                    loading = nearbyLoading
                )
            }
        }
    }
}

@Composable
private fun NearbyRecommendationsBlock(
    data: FloraNearbyRecommendationResponse?,
    error: String?,
    loading: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Gợi ý gần đây", fontWeight = FontWeight.SemiBold)
        error?.let { Text(it, color = Color(0xFF92400E), style = MaterialTheme.typography.bodySmall) }
        data?.journeyContext?.let { ctx ->
            val timeText = if (ctx.canValidateSchedule == true && ctx.freeMinutesUntilMeeting != null) {
                "Thời gian còn lại: khoảng ${ctx.freeMinutesUntilMeeting} phút"
            } else {
                "Chưa thể xác nhận đủ thời gian"
            }
            Text(timeText, style = MaterialTheme.typography.bodySmall)
        }
        data?.warnings?.forEach { w ->
            Text(w, style = MaterialTheme.typography.bodySmall, color = Color(0xFF92400E))
        }
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.padding(8.dp))
        } else {
            data?.recommendations?.forEach { item ->
                NearbyItemCard(item)
            }
        }
    }
}

@Composable
private fun NearbyItemCard(item: FloraNearbyItem) {
    val context = LocalContext.current
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(item.name ?: "—", fontWeight = FontWeight.SemiBold)
            Text(
                if (item.fitsSchedule == true) "Phù hợp với thời gian còn lại"
                else "Chưa thể xác nhận đủ thời gian",
                color = if (item.fitsSchedule == true) Color(0xFF065F46) else Color(0xFF92400E),
                style = MaterialTheme.typography.labelSmall
            )
            val meta = buildList {
                item.category?.let { add(it) }
                item.straightLineDistanceMeters?.let { add("${it}m") }
                item.estimatedVisitMinutes?.let { add("~${it} phút") }
            }.joinToString(" · ")
            if (meta.isNotBlank()) Text(meta, style = MaterialTheme.typography.bodySmall)
            item.warnings?.forEach { w ->
                Text(w, style = MaterialTheme.typography.bodySmall, color = Color(0xFF92400E))
            }
            val lat = item.mapAction?.latitude ?: item.latitude
            val lon = item.mapAction?.longitude ?: item.longitude
            if (lat != null && lon != null) {
                TextButton(onClick = {
                    val uri = Uri.parse("geo:$lat,$lon?q=$lat,$lon(${Uri.encode(item.name ?: "Địa điểm")})")
                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                }) {
                    Text("Mở bản đồ")
                }
            }
        }
    }
}

@Composable
private fun ScheduleBadge(status: String?) {
    val (label, color) = when (status) {
        "CONFIRMED" -> "Đã xác nhận" to Color(0xFF065F46)
        "ESTIMATED" -> "Dự kiến" to Color(0xFF92400E)
        "UNAVAILABLE", null -> "Chưa có lịch tập trung" to Color(0xFF6B7280)
        else -> status to Color(0xFF6B7280)
    }
    Surface(color = color.copy(alpha = 0.12f), shape = RoundedCornerShape(8.dp)) {
        Text(label, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = color, style = MaterialTheme.typography.labelSmall)
    }
}

package com.example.flourishtravelapp.ui.components

import android.Manifest
import android.content.Intent
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
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.FloraJourneyDto
import com.example.flourishtravelapp.data.model.FloraNearbyItem
import com.example.flourishtravelapp.data.model.FloraNearbyRecommendationResponse
import com.example.flourishtravelapp.data.repository.FloraNearbyRepository
import com.example.flourishtravelapp.data.repository.FloraPreferenceRepository
import com.example.flourishtravelapp.data.repository.NearbyFetchResult
import com.example.flourishtravelapp.location.FusedForegroundLocationProvider
import com.example.flourishtravelapp.location.LocationPermissionHelper
import com.example.flourishtravelapp.location.LocationProvider
import com.example.flourishtravelapp.location.MobileLocationResult
import com.example.flourishtravelapp.location.NearbyLocationPolicy
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun rememberFloraJourneyState(bookingId: String): FloraJourneyUiState {
    var journey by remember(bookingId) { mutableStateOf<FloraJourneyDto?>(null) }
    var loading by remember(bookingId) { mutableStateOf(true) }
    var error by remember(bookingId) { mutableStateOf<String?>(null) }
    var refreshToken by remember(bookingId) { mutableLongStateOf(0L) }

    val reload: () -> Unit = {
        refreshToken++
    }

    LaunchedEffect(bookingId, refreshToken) {
        if (!NearbyLocationPolicy.isValidBookingId(bookingId)) {
            loading = false
            error = NearbyLocationPolicy.invalidBookingMessage()
            return@LaunchedEffect
        }
        loading = true
        error = null
        try {
            val response = RetrofitClient.floraApiService.getJourney(bookingId)
            if (response.isSuccessful && response.body()?.success == true) {
                journey = response.body()?.data
            } else {
                error = "Không tải được lịch Flora"
            }
        } catch (_: Exception) {
            error = "Không tải được lịch Flora"
        } finally {
            loading = false
        }
    }

    return FloraJourneyUiState(bookingId, journey, loading, error, reload)
}

data class FloraJourneyUiState(
    val bookingId: String,
    val journey: FloraJourneyDto?,
    val loading: Boolean,
    val error: String?,
    val reload: () -> Unit = {}
)

@Composable
fun FloraJourneyPanel(
    state: FloraJourneyUiState,
    modifier: Modifier = Modifier
) {
    when {
        state.loading -> {
            Row(
                modifier = modifier.fillMaxWidth().padding(16.dp),
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
        state.journey != null -> FloraJourneyContent(state.bookingId, state.journey, state.reload, modifier)
    }
}

@Composable
private fun FloraJourneyContent(
    bookingId: String,
    journey: FloraJourneyDto,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    locationProvider: LocationProvider? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val provider = remember { locationProvider ?: FusedForegroundLocationProvider(context) }
    val preferenceRepo = remember { FloraPreferenceRepository(RetrofitClient.floraApiService) }
    val nearbyRepo = remember { FloraNearbyRepository(RetrofitClient.floraApiService) }

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
    @Suppress("UNUSED_VARIABLE") val refresh = tick

    var nearbyOpen by remember { mutableStateOf(false) }
    var nearbyLoading by remember { mutableStateOf(false) }
    var nearbyError by remember { mutableStateOf<String?>(null) }
    var nearbyInfo by remember { mutableStateOf<String?>(null) }
    var nearbyData by remember { mutableStateOf<FloraNearbyRecommendationResponse?>(null) }
    var locationConsent by remember { mutableStateOf<Boolean?>(null) }
    var pendingNearbyAfterPermission by remember { mutableStateOf(false) }

    suspend fun runNearbyFlow(androidPermissionGranted: Boolean) {
        if (!NearbyLocationPolicy.isValidBookingId(bookingId)) {
            nearbyError = NearbyLocationPolicy.invalidBookingMessage()
            return
        }
        nearbyOpen = true
        nearbyLoading = true
        nearbyError = null
        nearbyInfo = null
        nearbyData = null

        val consent = locationConsent ?: preferenceRepo.getLocationConsent().also { locationConsent = it }

        val locationResult: MobileLocationResult? = when {
            consent != true -> null
            !androidPermissionGranted -> MobileLocationResult.PermissionDenied
            else -> provider.getForegroundLocation()
        }

        val decision = NearbyLocationPolicy.coordinatesForRequest(consent, locationResult)
        nearbyInfo = decision.infoMessage

        when (val result = nearbyRepo.fetchNearby(bookingId, decision.latitude, decision.longitude)) {
            is NearbyFetchResult.Success -> nearbyData = result.data
            is NearbyFetchResult.Empty -> {
                nearbyData = result.data
                if (result.data?.recommendations.isNullOrEmpty()) {
                    nearbyInfo = (nearbyInfo?.let { "$it\n" } ?: "") +
                        NearbyLocationPolicy.emptyRecommendationsMessage()
                }
            }
            is NearbyFetchResult.Error -> nearbyError = result.message
        }
        nearbyLoading = false
        pendingNearbyAfterPermission = false
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val granted = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true
            || grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (pendingNearbyAfterPermission) {
            scope.launch { runNearbyFlow(granted) }
        }
    }

    fun onNearbyTap() {
        if (!NearbyLocationPolicy.isValidBookingId(bookingId)) {
            nearbyOpen = true
            nearbyError = NearbyLocationPolicy.invalidBookingMessage()
            return
        }
        scope.launch {
            val consent = locationConsent ?: preferenceRepo.getLocationConsent().also { locationConsent = it }
            if (!NearbyLocationPolicy.shouldRequestAndroidPermission(consent)) {
                runNearbyFlow(androidPermissionGranted = false)
                return@launch
            }
            if (LocationPermissionHelper.hasForegroundLocationPermission(context)) {
                runNearbyFlow(androidPermissionGranted = true)
            } else {
                pendingNearbyAfterPermission = true
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFECFDF5)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Flora AI — Hành trình", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            val sessionOverride = journey.nextMeeting?.scheduleSource == "SESSION_OVERRIDE"
                || journey.currentActivity?.scheduleSource == "SESSION_OVERRIDE"
                || journey.nextActivity?.scheduleSource == "SESSION_OVERRIDE"
            if (sessionOverride) {
                Text(
                    "Lịch trình đã cập nhật",
                    color = Color(0xFF1D4ED8),
                    fontWeight = FontWeight.SemiBold
                )
                journey.nextMeeting?.lastUpdatedAt?.let {
                    Text("Cập nhật lúc $it", style = MaterialTheme.typography.bodySmall, color = Color(0xFF6B7280))
                }
                journey.nextMeeting?.locationName?.let {
                    Text("Điểm tập trung mới: $it", style = MaterialTheme.typography.bodySmall)
                }
                Text(
                    "Thông tin lịch trình có thể thay đổi theo điều kiện thực tế.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280)
                )
            }

            journey.currentActivity?.let { act ->
                Text("Đang diễn ra: ${act.title ?: "—"}", fontWeight = FontWeight.SemiBold)
                act.endAt?.let { Text("Đến $it", style = MaterialTheme.typography.bodySmall) }
            } ?: journey.currentScheduleItem?.title?.let { Text("Hôm nay: $it") }

            journey.nextActivity?.let { act ->
                Text("Tiếp theo: ${act.title ?: "—"}")
            } ?: journey.nextScheduleItem?.title?.let { Text("Tiếp theo (ngày): $it") }

            meeting?.locationName?.let { Text("Điểm tập trung: $it") }
                ?: journey.meetingPoint?.let { Text("Điểm tập trung: $it") }

            ScheduleBadge(meeting?.scheduleStatus)

            if (showCountdown && meeting?.minutesUntil != null) {
                Text(
                    "Còn khoảng ${meeting.minutesUntil} phút đến giờ tập trung",
                    color = Color(0xFF047857),
                    fontWeight = FontWeight.SemiBold
                )
            } else if (meeting?.scheduleStatus == "UNAVAILABLE" || meeting == null) {
                Text(
                    "Chưa có lịch tập trung chính thức. Hãy theo HDV hoặc nhóm chat đoàn nhé.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280)
                )
            }

            journey.warnings?.forEach { w ->
                Text(w, style = MaterialTheme.typography.bodySmall, color = Color(0xFF92400E))
            }

            TextButton(onClick = onRefresh) {
                Text("Làm mới hành trình")
            }

            Button(onClick = { onNearbyTap() }, enabled = !nearbyLoading) {
                Text(if (nearbyLoading) "Đang tải gợi ý..." else "Gợi ý gần đây")
            }

            if (nearbyOpen) {
                NearbyRecommendationsBlock(
                    data = nearbyData,
                    error = nearbyError,
                    info = nearbyInfo,
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
    info: String?,
    loading: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Gợi ý gần đây", fontWeight = FontWeight.SemiBold)

        if (loading) {
            Text(
                "Flora đang tìm các địa điểm phù hợp gần bạn…",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF047857)
            )
            CircularProgressIndicator(modifier = Modifier.padding(8.dp))
        }

        info?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = Color(0xFF92400E))
        }
        error?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = Color(0xFF92400E))
        }

        NearbyLocationPolicy.locationSourceLabel(data?.locationSource)?.let { label ->
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color(0xFF065F46))
        }
        data?.locationLabel?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = Color(0xFF5B7A75))
        }

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

        if (!loading) {
            val items = data?.recommendations.orEmpty()
            if (items.isEmpty() && error == null && info == null) {
                Text(
                    NearbyLocationPolicy.emptyRecommendationsMessage(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280)
                )
            } else {
                items.forEach { item -> NearbyItemCard(item) }
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
                item.estimatedVisitMinutes?.let { add("~${it} phút ghé thăm") }
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
        Text(
            label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = color,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

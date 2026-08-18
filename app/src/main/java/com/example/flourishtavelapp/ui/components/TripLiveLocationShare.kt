package com.example.flourishtravelapp.ui.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.FloraLocationRequest
import com.example.flourishtravelapp.location.FusedForegroundLocationProvider
import com.example.flourishtravelapp.location.LocationPermissionHelper
import com.example.flourishtravelapp.location.LocationProvider
import com.example.flourishtravelapp.location.MobileLocationResult
import com.example.flourishtravelapp.location.NearbyLocationPolicy
import kotlinx.coroutines.delay

/**
 * Gửi GPS cho HDV khi khách đang mở màn hình chuyến trong ngày tour.
 */
@Composable
fun TripLiveLocationShare(
    bookingId: String,
    enabled: Boolean,
    locationProvider: LocationProvider? = null
) {
    val context = LocalContext.current
    val provider = remember { locationProvider ?: FusedForegroundLocationProvider(context) }
    var status by remember(bookingId, enabled) { mutableStateOf("idle") }
    var permissionGranted by remember {
        mutableStateOf(LocationPermissionHelper.hasForegroundLocationPermission(context))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        permissionGranted = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true
            || grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (!permissionGranted) status = "denied"
    }

    LaunchedEffect(enabled, bookingId) {
        if (!enabled || !NearbyLocationPolicy.isValidBookingId(bookingId)) {
            status = "idle"
            return@LaunchedEffect
        }
        if (!LocationPermissionHelper.hasForegroundLocationPermission(context)) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            permissionGranted = true
        }
    }

    LaunchedEffect(enabled, bookingId, permissionGranted) {
        if (!enabled || !permissionGranted || !NearbyLocationPolicy.isValidBookingId(bookingId)) {
            return@LaunchedEffect
        }
        while (true) {
            when (val loc = provider.getForegroundLocation()) {
                is MobileLocationResult.Success -> {
                    try {
                        RetrofitClient.floraApiService.postLocation(
                            bookingId,
                            FloraLocationRequest(
                                latitude = loc.latitude,
                                longitude = loc.longitude
                            )
                        )
                        status = "sharing"
                    } catch (_: Exception) {
                        status = "error"
                    }
                }
                is MobileLocationResult.PermissionDenied -> status = "denied"
                is MobileLocationResult.ServiceDisabled -> status = "error"
                else -> status = "error"
            }
            delay(30_000)
        }
    }

    if (!enabled) return

    val text = when (status) {
        "denied" -> "Cần quyền vị trí để HDV thấy bạn trên bản đồ trong chuyến đi."
        "error" -> "Chưa gửi được vị trí. Bật GPS rồi mở lại màn hình chuyến đi."
        "sharing" -> "Đang chia sẻ vị trí với HDV trong chuyến đi."
        else -> "Đang chia sẻ vị trí với HDV trong chuyến đi."
    }
    Text(
        text = text,
        fontSize = 12.sp,
        color = Color(0xFF047857),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
    )
}

package com.example.flourishtavelapp.location

import android.content.Context
import android.location.LocationManager as AndroidLocationManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

class FusedForegroundLocationProvider(
    private val context: Context
) : LocationProvider {

    override suspend fun getForegroundLocation(timeoutMs: Long): MobileLocationResult =
        withContext(Dispatchers.IO) {
            if (!isLocationEnabled()) {
                return@withContext MobileLocationResult.ServiceDisabled
            }

            val fusedResult = withTimeoutOrNull(timeoutMs) {
                fetchFromFused()
            }
            when (fusedResult) {
                is MobileLocationResult.Success -> return@withContext fusedResult
                null -> {
                    fetchLastKnown()?.let { return@withContext it }
                    return@withContext MobileLocationResult.Timeout
                }
                else -> { /* continue to last known */ }
            }

            fetchLastKnown()?.let { return@withContext it }
            fusedResult ?: MobileLocationResult.Unavailable
        }

    private fun isLocationEnabled(): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as AndroidLocationManager
        return manager.isProviderEnabled(AndroidLocationManager.GPS_PROVIDER)
            || manager.isProviderEnabled(AndroidLocationManager.NETWORK_PROVIDER)
    }

    private suspend fun fetchFromFused(): MobileLocationResult = suspendCancellableCoroutine { cont ->
        val client = LocationServices.getFusedLocationProviderClient(context)
        val tokenSource = CancellationTokenSource()
        cont.invokeOnCancellation { tokenSource.cancel() }

        client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, tokenSource.token)
            .addOnSuccessListener { location ->
                if (location != null) {
                    cont.resume(MobileLocationResult.Success(location.latitude, location.longitude))
                } else {
                    cont.resume(MobileLocationResult.Unavailable)
                }
            }
            .addOnFailureListener {
                cont.resume(MobileLocationResult.Unavailable)
            }
    }

    private fun fetchLastKnown(): MobileLocationResult? {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as AndroidLocationManager
        val providers = listOf(
            AndroidLocationManager.GPS_PROVIDER,
            AndroidLocationManager.NETWORK_PROVIDER
        )
        for (provider in providers) {
            if (!manager.isProviderEnabled(provider)) continue
            @Suppress("MissingPermission")
            val location = manager.getLastKnownLocation(provider) ?: continue
            if (location.latitude != 0.0 || location.longitude != 0.0) {
                return MobileLocationResult.Success(location.latitude, location.longitude)
            }
        }
        return null
    }
}

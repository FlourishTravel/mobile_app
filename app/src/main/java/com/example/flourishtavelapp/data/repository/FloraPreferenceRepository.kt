package com.example.flourishtavelapp.data.repository

import com.example.flourishtavelapp.data.api.FloraApiService
import com.example.flourishtavelapp.data.model.TravelPreferencesDto
import com.example.flourishtavelapp.data.model.UpdateTravelPreferencesRequest
import com.example.flourishtavelapp.data.preferences.FloraPreferencesMapper
import java.io.IOException

class FloraPreferenceRepository(
    private val floraApiService: FloraApiService
) {
    suspend fun loadPreferences(): PreferenceLoadResult {
        return try {
            val response = floraApiService.getPreferences()
            when {
                response.code() == 401 ->
                    PreferenceLoadResult.Unauthorized
                response.isSuccessful && response.body()?.success == true -> {
                    PreferenceLoadResult.Success(
                        FloraPreferencesMapper.normalize(response.body()?.data)
                    )
                }
                else -> PreferenceLoadResult.Error(
                    response.body()?.message ?: "Flora chưa tải được cài đặt."
                )
            }
        } catch (_: IOException) {
            PreferenceLoadResult.Error("Flora chưa tải được cài đặt. Kiểm tra kết nối mạng.")
        } catch (_: Exception) {
            PreferenceLoadResult.Error("Flora chưa tải được cài đặt.")
        }
    }

    suspend fun updatePreferences(request: UpdateTravelPreferencesRequest): PreferenceSaveResult {
        return try {
            val response = floraApiService.updatePreferences(request)
            when {
                response.code() == 401 ->
                    PreferenceSaveResult.Unauthorized
                response.code() == 403 ->
                    PreferenceSaveResult.Error("Flora chưa có quyền cập nhật cài đặt này.")
                response.isSuccessful && response.body()?.success == true -> {
                    PreferenceSaveResult.Success(
                        FloraPreferencesMapper.normalize(response.body()?.data)
                    )
                }
                else -> PreferenceSaveResult.Error(
                    response.body()?.message ?: "Flora chưa thể lưu thay đổi. Bạn vui lòng thử lại nhé."
                )
            }
        } catch (_: IOException) {
            PreferenceSaveResult.Error("Flora chưa thể lưu thay đổi. Bạn vui lòng thử lại nhé.")
        } catch (_: Exception) {
            PreferenceSaveResult.Error("Flora chưa thể lưu thay đổi. Bạn vui lòng thử lại nhé.")
        }
    }

    suspend fun saveDraft(draft: TravelPreferencesDto): PreferenceSaveResult =
        updatePreferences(FloraPreferencesMapper.toPatchRequest(draft))

    suspend fun getLocationConsent(): Boolean? {
        return when (val result = loadPreferences()) {
            is PreferenceLoadResult.Success -> result.data.locationConsent
            else -> null
        }
    }
}

sealed class PreferenceLoadResult {
    data class Success(val data: TravelPreferencesDto) : PreferenceLoadResult()
    data object Unauthorized : PreferenceLoadResult()
    data class Error(val message: String) : PreferenceLoadResult()
}

sealed class PreferenceSaveResult {
    data class Success(val data: TravelPreferencesDto) : PreferenceSaveResult()
    data object Unauthorized : PreferenceSaveResult()
    data class Error(val message: String) : PreferenceSaveResult()
}

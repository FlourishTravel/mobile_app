package com.example.flourishtavelapp.data.repository

import com.example.flourishtavelapp.data.api.FloraApiService
import com.example.flourishtavelapp.data.api.ReviewApiService
import com.example.flourishtavelapp.data.model.CreateReviewRequest
import com.example.flourishtavelapp.data.model.FloraPostTourFeedbackContextDto
import com.example.flourishtavelapp.data.model.FloraPreferencePreviewDto
import com.example.flourishtavelapp.data.model.FloraPreferencePreviewRequest
import com.example.flourishtavelapp.data.model.UpdateTravelPreferencesRequest
import java.io.IOException

class FloraPostTourFeedbackRepository(
    private val floraApiService: FloraApiService,
    private val reviewApiService: ReviewApiService,
    private val preferenceRepository: FloraPreferenceRepository
) {
    suspend fun loadContext(bookingId: String): FeedbackLoadResult {
        return try {
            val response = floraApiService.getPostTourFeedback(bookingId)
            when {
                response.code() == 401 -> FeedbackLoadResult.Unauthorized
                response.code() == 404 -> FeedbackLoadResult.Error("Không tìm thấy chuyến đi.")
                response.isSuccessful && response.body()?.success == true ->
                    FeedbackLoadResult.Success(requireNotNull(response.body()?.data))
                else -> FeedbackLoadResult.Error(
                    response.body()?.message ?: "Flora chưa tải được phản hồi chuyến đi."
                )
            }
        } catch (_: IOException) {
            FeedbackLoadResult.Error("Flora chưa tải được phản hồi. Kiểm tra kết nối mạng.")
        } catch (_: Exception) {
            FeedbackLoadResult.Error("Flora chưa tải được phản hồi chuyến đi.")
        }
    }

    suspend fun previewPreferences(selectedTagIds: List<String>): FeedbackPreviewResult {
        return try {
            val response = floraApiService.previewFeedbackPreferences(
                FloraPreferencePreviewRequest(selectedTagIds)
            )
            when {
                response.code() == 401 -> FeedbackPreviewResult.Unauthorized
                response.isSuccessful && response.body()?.success == true ->
                    FeedbackPreviewResult.Success(requireNotNull(response.body()?.data))
                else -> FeedbackPreviewResult.Error(
                    response.body()?.message ?: "Flora chưa xem trước được sở thích."
                )
            }
        } catch (_: IOException) {
            FeedbackPreviewResult.Error("Flora chưa xem trước được sở thích. Kiểm tra kết nối mạng.")
        } catch (_: Exception) {
            FeedbackPreviewResult.Error("Flora chưa xem trước được sở thích.")
        }
    }

    suspend fun submitReview(
        bookingId: String,
        rating: Int,
        comment: String?,
        feedbackTags: List<String>?
    ): FeedbackSubmitResult {
        return try {
            val response = reviewApiService.createReview(
                CreateReviewRequest(
                    bookingId = bookingId,
                    rating = rating,
                    comment = comment?.takeIf { it.isNotBlank() },
                    feedbackTags = feedbackTags?.takeIf { it.isNotEmpty() }
                )
            )
            when {
                response.code() == 401 -> FeedbackSubmitResult.Unauthorized
                response.code() == 400 -> FeedbackSubmitResult.Error(
                    response.body()?.message ?: "Không gửi được đánh giá."
                )
                response.isSuccessful && response.body()?.success == true ->
                    FeedbackSubmitResult.Success
                else -> FeedbackSubmitResult.Error(
                    response.body()?.message ?: "Không gửi được đánh giá."
                )
            }
        } catch (_: IOException) {
            FeedbackSubmitResult.Error("Không gửi được đánh giá. Kiểm tra kết nối mạng.")
        } catch (_: Exception) {
            FeedbackSubmitResult.Error("Không gửi được đánh giá.")
        }
    }

    suspend fun savePreferences(patch: UpdateTravelPreferencesRequest): PreferenceSaveResult =
        preferenceRepository.updatePreferences(patch)
}

sealed class FeedbackLoadResult {
    data class Success(val data: FloraPostTourFeedbackContextDto) : FeedbackLoadResult()
    data object Unauthorized : FeedbackLoadResult()
    data class Error(val message: String) : FeedbackLoadResult()
}

sealed class FeedbackPreviewResult {
    data class Success(val data: FloraPreferencePreviewDto) : FeedbackPreviewResult()
    data object Unauthorized : FeedbackPreviewResult()
    data class Error(val message: String) : FeedbackPreviewResult()
}

sealed class FeedbackSubmitResult {
    data object Success : FeedbackSubmitResult()
    data object Unauthorized : FeedbackSubmitResult()
    data class Error(val message: String) : FeedbackSubmitResult()
}

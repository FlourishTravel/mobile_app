package com.example.flourishtravelapp.location

import org.junit.Assert.*
import org.junit.Test

class NearbyLocationPolicyTest {

  @Test
  fun validBookingId_acceptsUuid() {
    val id = "123e4567-e89b-12d3-a456-426614174000"
    assertTrue(NearbyLocationPolicy.isValidBookingId(id))
  }

  @Test
  fun tourId_isNotValidBookingId() {
    assertFalse(NearbyLocationPolicy.isValidBookingId("4"))
    assertFalse(NearbyLocationPolicy.isValidBookingId("tour-abc"))
  }

  @Test
  fun locationConsentDisabled_doesNotRequestPermission() {
    assertFalse(NearbyLocationPolicy.shouldRequestAndroidPermission(false))
    assertFalse(NearbyLocationPolicy.shouldRequestAndroidPermission(null))
  }

  @Test
  fun locationConsentEnabled_mayRequestPermission() {
    assertTrue(NearbyLocationPolicy.shouldRequestAndroidPermission(true))
  }

  @Test
  fun consentDisabled_sendsNoCoordinates() {
    val decision = NearbyLocationPolicy.coordinatesForRequest(false, null)
    assertNull(decision.latitude)
    assertNull(decision.longitude)
    assertTrue(decision.infoMessage!!.contains("chưa bật chia sẻ vị trí"))
  }

  @Test
  fun consentEnabledAndGpsSuccess_sendsCoordinates() {
    val decision = NearbyLocationPolicy.coordinatesForRequest(
      true,
      MobileLocationResult.Success(10.77, 106.70)
    )
    assertEquals(10.77, decision.latitude!!, 0.0001)
    assertEquals(106.70, decision.longitude!!, 0.0001)
    assertNull(decision.infoMessage)
  }

  @Test
  fun consentEnabledPermissionDenied_sendsNoCoordinates() {
    val decision = NearbyLocationPolicy.coordinatesForRequest(
      true,
      MobileLocationResult.PermissionDenied
    )
    assertNull(decision.latitude)
    assertNull(decision.longitude)
    assertTrue(decision.infoMessage!!.contains("lịch trình"))
  }

  @Test
  fun serviceDisabled_sendsNoCoordinatesWithMessage() {
    val decision = NearbyLocationPolicy.coordinatesForRequest(
      true,
      MobileLocationResult.ServiceDisabled
    )
    assertNull(decision.latitude)
    assertTrue(decision.infoMessage!!.contains("chưa lấy được vị trí"))
  }

  @Test
  fun timeout_sendsNoCoordinates() {
    val decision = NearbyLocationPolicy.coordinatesForRequest(true, MobileLocationResult.Timeout)
    assertNull(decision.latitude)
    assertNull(decision.longitude)
  }

  @Test
  fun locationSourceLabels() {
    assertEquals("Gợi ý theo vị trí hiện tại của bạn", NearbyLocationPolicy.locationSourceLabel("USER_LOCATION"))
    assertEquals("Gợi ý theo địa điểm trong lịch trình", NearbyLocationPolicy.locationSourceLabel("ACTIVITY_LOCATION"))
    assertEquals("Gợi ý theo khu vực điểm đến (dữ liệu dự kiến)", NearbyLocationPolicy.locationSourceLabel("DESTINATION_FALLBACK"))
  }

  @Test
  fun apiErrorMessages_doNotExposeRawExceptions() {
    assertTrue(NearbyLocationPolicy.apiErrorMessage(401, false).contains("đăng nhập"))
    assertTrue(NearbyLocationPolicy.apiErrorMessage(429, false).contains("thử lại"))
    assertTrue(NearbyLocationPolicy.apiErrorMessage(null, true).contains("kết nối"))
  }

  @Test
  fun messages_doNotContainRawCoordinates() {
    val messages = listOf(
      NearbyLocationPolicy.coordinatesForRequest(false, null).infoMessage,
      NearbyLocationPolicy.apiErrorMessage(403, false),
      NearbyLocationPolicy.emptyRecommendationsMessage()
    )
    messages.filterNotNull().forEach { msg ->
      assertFalse(msg.contains("10.7769"))
      assertFalse(msg.matches(Regex(".*\\d+\\.\\d{4,}.*")))
    }
  }
}

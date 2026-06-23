package com.example.flourishtavelapp.data.preferences

import com.example.flourishtavelapp.data.model.UpdateTravelPreferencesRequest
import org.junit.Assert.*
import org.junit.Test

class FloraPreferencesMapperTest {

    @Test
    fun toPatchRequest_preservesAllFieldsFromDraft() {
        val draft = FloraPreferencesMapper.defaults().copy(
            travelStyles = listOf("Biển", "Ẩm thực"),
            budgetLevel = "medium",
            favoriteDestinations = listOf("Đà Lạt"),
            favoriteFoods = listOf("cà phê"),
            foodDislikes = listOf("cay"),
            foodAllergies = listOf("đậu phộng"),
            preferredActivities = listOf("tắm biển"),
            avoidedActivities = listOf("leo núi"),
            travelPace = "vừa phải",
            travelingWithChildren = true,
            travelingWithElderly = false,
            notificationConsent = true,
            locationConsent = false,
            personalizationConsent = true
        )

        val patch = FloraPreferencesMapper.toPatchRequest(draft)
        assertEquals(listOf("Biển", "Ẩm thực"), patch.travelStyles)
        assertEquals("medium", patch.budgetLevel)
        assertEquals(listOf("Đà Lạt"), patch.favoriteDestinations)
        assertEquals(listOf("đậu phộng"), patch.foodAllergies)
        assertEquals(false, patch.locationConsent)
        assertEquals(true, patch.personalizationConsent)
        assertEquals(true, patch.notificationConsent)
    }

    @Test
    fun locationConsentToggle_sendsOnlyConsentFieldInPartialScenario() {
        val draft = FloraPreferencesMapper.defaults().copy(
            locationConsent = true,
            personalizationConsent = false
        )
        val patch = FloraPreferencesMapper.toPatchRequest(draft)
        assertEquals(true, patch.locationConsent)
        assertEquals(false, patch.personalizationConsent)
        assertNotNull(patch.travelStyles)
    }

    @Test
    fun parseCommaList_trimsAndFiltersEmpty() {
        assertEquals(listOf("a", "b"), FloraPreferencesMapper.parseCommaList("a, b, , "))
    }

    @Test
    fun normalize_appliesServerDefaults() {
        val normalized = FloraPreferencesMapper.normalize(null)
        assertEquals(false, normalized.locationConsent)
        assertEquals(true, normalized.personalizationConsent)
        assertTrue(normalized.travelStyles!!.isEmpty())
    }

    @Test
    fun patchDoesNotIncludeSensitiveLoggingInToString() {
        val patch = UpdateTravelPreferencesRequest(foodAllergies = listOf("hải sản"))
        val text = patch.toString()
        assertTrue(text.contains("foodAllergies"))
    }
}

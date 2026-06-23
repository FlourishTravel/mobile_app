package com.example.flourishtavelapp.data.preferences

import com.example.flourishtavelapp.data.model.TravelPreferencesDto
import com.example.flourishtavelapp.data.model.UpdateTravelPreferencesRequest

/**
 * Builds PATCH body from UI draft. Backend applies partial updates per non-null field.
 */
object FloraPreferencesMapper {

    fun defaults(): TravelPreferencesDto = TravelPreferencesDto(
        travelStyles = emptyList(),
        favoriteDestinations = emptyList(),
        favoriteFoods = emptyList(),
        foodDislikes = emptyList(),
        foodAllergies = emptyList(),
        preferredActivities = emptyList(),
        avoidedActivities = emptyList(),
        notificationConsent = true,
        locationConsent = false,
        personalizationConsent = true
    )

    fun normalize(dto: TravelPreferencesDto?): TravelPreferencesDto {
        val base = dto ?: defaults()
        return base.copy(
            travelStyles = base.travelStyles ?: emptyList(),
            favoriteDestinations = base.favoriteDestinations ?: emptyList(),
            favoriteFoods = base.favoriteFoods ?: emptyList(),
            foodDislikes = base.foodDislikes ?: emptyList(),
            foodAllergies = base.foodAllergies ?: emptyList(),
            preferredActivities = base.preferredActivities ?: emptyList(),
            avoidedActivities = base.avoidedActivities ?: emptyList(),
            notificationConsent = base.notificationConsent ?: true,
            locationConsent = base.locationConsent ?: false,
            personalizationConsent = base.personalizationConsent ?: true
        )
    }

    fun toPatchRequest(draft: TravelPreferencesDto): UpdateTravelPreferencesRequest =
        UpdateTravelPreferencesRequest(
            travelStyles = draft.travelStyles,
            budgetLevel = draft.budgetLevel?.trim()?.takeIf { it.isNotEmpty() },
            favoriteDestinations = draft.favoriteDestinations,
            favoriteFoods = draft.favoriteFoods,
            foodDislikes = draft.foodDislikes,
            foodAllergies = draft.foodAllergies,
            preferredActivities = draft.preferredActivities,
            avoidedActivities = draft.avoidedActivities,
            travelPace = draft.travelPace?.trim()?.takeIf { it.isNotEmpty() },
            travelingWithChildren = draft.travelingWithChildren,
            travelingWithElderly = draft.travelingWithElderly,
            notificationConsent = draft.notificationConsent,
            locationConsent = draft.locationConsent,
            personalizationConsent = draft.personalizationConsent
        )

    fun parseCommaList(text: String): List<String> =
        text.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    fun joinCommaList(list: List<String>?): String =
        list?.joinToString(", ") ?: ""
}

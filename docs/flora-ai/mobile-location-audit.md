# Flora AI — Mobile location audit (pre Phase 1.3.1)

Audit date: 2026-06-23. Package: `com.example.flourishtavelapp`.

## 1. Manifest permissions

| Permission | Declared before audit? |
|------------|------------------------|
| `ACCESS_FINE_LOCATION` | **No** |
| `ACCESS_COARSE_LOCATION` | **No** |
| `ACCESS_BACKGROUND_LOCATION` | **No** (must remain absent) |

Only `INTERNET` was declared.

## 2. Location provider

| Provider | Used? |
|----------|-------|
| `FusedLocationProviderClient` | **No** (added in Phase 1.3.1) |
| `LocationManager` | **No** |
| Custom helper | **No** |

Phase 1.3 `FloraJourneyPanel` requested `ACCESS_FINE_LOCATION` but never read device coordinates (always sent `null`).

## 3. Flourish `locationConsent`

| Item | Status |
|------|--------|
| Backend API | `GET/PATCH /api/flora/preferences/me` with `locationConsent` |
| Mobile API client | **Missing** before 1.3.1 |
| Dedicated settings screen | **No** on mobile; web `PrivacySettings` exists |
| Local duplicate preference | **None** |

## 4. `bookingId` for `FloraJourneyPanel`

- `ScheduleScreen.DetailedItineraryScreen`: `floraBookingId = if (tour.isBooked && isUuid(tour.id)) tour.id`
- Booked tours map `TourItem.id = booking.bookingId` (UUID), `catalogTourId = booking.tourId`
- Mock itinerary uses `catalogTourId`, not `tour.id`

## 5. Nearby request coordinates

Phase 1.3 sent `FloraNearbyRecommendationRequest(latitude = null, longitude = null)` even after permission grant.

## 6. Permission denial handling

- Permission launcher existed but both branches called API without GPS
- No consent check before Android permission dialog
- No user-facing fallback copy per consent/permission state

## 7. Android compatibility

- `minSdk 24`, `targetSdk 35`, `compileSdk 35`
- Runtime permissions required from API 23+ (covered)
- No background location (Android 10+ policy avoided)
- Google Play Services location dependency added (devices without GMS fall back to API without GPS)

## 8. Logging / persistence

| Concern | Finding |
|---------|---------|
| Persist GPS on device | **No** |
| Log coordinates | **Risk**: OkHttp `Level.BODY` logs full JSON including lat/lon |
| Show coords in UI | **No** |
| Analytics | **None** for location |

## 9. Package structure issue

Flora files were committed under `com.example.flourishtravelapp` (typo) while the app namespace is `com.example.flourishtavelapp`. Phase 1.3.1 consolidates into the correct package.

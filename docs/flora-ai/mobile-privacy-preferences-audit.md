# Flora AI — Mobile privacy & preferences audit (pre Phase 1.3.2)

Audit date: 2026-06-23. Package: `com.example.flourishtavelapp`.

## 1. Profile / account route

- Logged-in users reach **Profile** via bottom tab index `4` (`MainActivity` → `ProfileScreen`).
- Navigation uses `NavigationState` enum + `selectedTab`, not Navigation Compose routes.
- Profile is **not shown** when `isLoggedIn == false` (redirects to Login).

## 2. Best place for “Cài đặt Flora AI”

- **ProfileScreen** → card “Cài đặt”, new row **Cài đặt Flora AI** (alongside Ngôn ngữ, Thông báo).
- Opens new `NavigationState.FloraSettings` screen (authenticated only).

## 3. Backend DTO fields (`TravelPreferencesDto`)

| Field | Type |
|-------|------|
| travelStyles | List&lt;String&gt; |
| budgetLevel | String |
| favoriteDestinations | List&lt;String&gt; |
| favoriteFoods | List&lt;String&gt; |
| foodDislikes | List&lt;String&gt; |
| foodAllergies | List&lt;String&gt; |
| preferredActivities | List&lt;String&gt; |
| avoidedActivities | List&lt;String&gt; |
| travelPace | String |
| travelingWithChildren | Boolean |
| travelingWithElderly | Boolean |
| notificationConsent | Boolean |
| locationConsent | Boolean |
| personalizationConsent | Boolean |

## 4. PATCH semantics

**Partial update** — `UserTravelPreferenceService.update()` only applies non-null request fields. Sending `null` leaves existing DB values unchanged.

Mobile must still send a **merged** payload on Save when updating list fields the user edited, using last loaded server state as base.

## 5. Current mobile client capability

| Capability | Before 1.3.2 |
|------------|----------------|
| GET preferences | `FloraApiService.getPreferences()` — **consent fields only** in model |
| PATCH preferences | **Not implemented** |
| Settings screen | **None** |
| `FloraPreferenceRepository` | `getLocationConsent()` only |

## 6. Local duplicate preferences

- `ProfileScreen` has local `notificationEnabled` / `tempNotification` for generic profile UI — **not** Flora `notificationConsent`.
- No SharedPreferences copy of Flora travel preferences.

## 7. Authenticated API calls

- `RetrofitClient` auth interceptor adds `Authorization: Bearer {token}` from `SessionManager`.
- Flora endpoints use same client as user/booking APIs.

## 8. Reusable Compose components

- `ProfileCard`, `ProfileRowItem` (ProfileScreen.kt)
- Material3 `Switch`, `OutlinedTextField`, `FilterChip`
- Theme: `PrimaryGreen`, `NatureGreenBackground`, `SecondaryTextColor`

## 9. Location permission helper

- `LocationPermissionHelper.hasForegroundLocationPermission()` exists (Phase 1.3.1).
- Used read-only on Flora settings screen for device status display.

## 10. Risk of losing fields on update

- **Low** if mobile loads full DTO then PATCHes merged `UpdateTravelPreferencesRequest` with all user-visible fields.
- **High** if mobile PATCHes only one boolean without merging list fields — mitigated by Save button sending full draft from loaded state.

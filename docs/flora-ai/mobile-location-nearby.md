# Flora AI — Mobile foreground location for nearby recommendations (Phase 1.3.1)

## Policy

- **Foreground only** — location is read once after the user taps “Gợi ý gần đây”.
- **No background tracking** — no `ACCESS_BACKGROUND_LOCATION`, no services, no periodic updates.
- **No persistence** — coordinates are sent in a single POST body and not stored on device.

## Consent vs Android permission

| Flourish `locationConsent` | Android permission | Behavior |
|----------------------------|-------------------|----------|
| `false` / `null` | (not requested) | API called without lat/lon |
| `true` | Granted | One fused location read → coordinates sent |
| `true` | Denied / off / timeout | API without lat/lon, friendly fallback copy |

Consent is loaded from `GET /api/flora/preferences/me`. The app never writes `locationConsent` locally.

## Fallback order (backend)

1. `USER_LOCATION` — GPS + consent  
2. `ACTIVITY_LOCATION` — current activity coordinates  
3. `DESTINATION_FALLBACK` — destination geocode  
4. `UNAVAILABLE` — empty list  

Mobile always continues to call the API; backend chooses the source.

## Location provider

- **Primary:** `FusedLocationProviderClient` (`play-services-location:21.3.0`)
- **Priority:** `PRIORITY_BALANCED_POWER_ACCURACY`
- **Timeout:** 8 seconds (`LocationProvider.DEFAULT_TIMEOUT_MS`)
- **Fallback:** `LocationManager.getLastKnownLocation` when fused returns null

## Privacy

- OkHttp logs redact `latitude`, `longitude`, and `Authorization` values.
- UI never shows raw coordinates.
- `bookingId` must be a UUID from the booking record, never `catalogTourId`.

## User-visible messages

| Situation | Message |
|-----------|---------|
| Consent off | Flora sẽ gợi ý theo địa điểm trong lịch trình vì bạn chưa bật chia sẻ vị trí. |
| Permission denied / no fix | Flora đang gợi ý theo địa điểm trong lịch trình của bạn. |
| Location off | Flora chưa lấy được vị trí hiện tại, nên đang dùng địa điểm trong lịch trình. |
| Loading | Flora đang tìm các địa điểm phù hợp gần bạn… |
| 401 | Bạn cần đăng nhập để Flora hỗ trợ theo chuyến đi của mình. |
| 429 | Flora đang nhận khá nhiều yêu cầu… |

## Known limitations

- Devices without Google Play Services may not return fused location (falls back to last-known or API without GPS).
- Mobile does not yet expose a full privacy settings screen; consent is managed via web/backend.
- `HttpLoggingInterceptor` redacts common patterns but debug builds should still avoid sharing logs publicly.

See also: [mobile-location-audit.md](./mobile-location-audit.md)

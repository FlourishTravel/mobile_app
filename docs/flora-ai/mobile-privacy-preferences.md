# Flora AI — Mobile privacy & travel preferences (Phase 1.3.2)

## Purpose

Mobile screen **Cài đặt Flora AI** lets logged-in users manage Flora consent flags and travel preferences synced with the backend. No local duplicate preference store.

## API

| Method | Path |
|--------|------|
| GET | `/api/flora/preferences/me` |
| PATCH | `/api/flora/preferences/me` |

Authentication: Bearer token via `RetrofitClient` + `SessionManager`.

## PATCH behavior

Backend uses **partial update** — only non-null JSON fields are applied. Mobile loads full preferences, edits a draft in UI, then PATCHes all visible fields via `FloraPreferencesMapper.toPatchRequest()` on **Lưu cài đặt**.

## Consent meanings

| Field | Meaning |
|-------|---------|
| `personalizationConsent` | Use in-system travel style/history for suggestions |
| `locationConsent` | Allow GPS only when user taps “Gợi ý gần đây” |
| `notificationConsent` | In-app Flora reminders (not FCM push in this phase) |

## Flourish consent vs Android permission

- Turning **on** `locationConsent` updates backend only — **no** runtime location dialog on settings screen.
- Android `ACCESS_FINE/COARSE` is requested only from `FloraJourneyPanel` when user taps nearby recommendations.
- Settings screen shows read-only device status: “Quyền vị trí trên thiết bị: …”

## No background location

`ACCESS_BACKGROUND_LOCATION` is not declared. Flora does not track location in background.

## Food allergy limitation

UI displays:

> Thông tin này chỉ giúp Flora hạn chế gợi ý không phù hợp, không thay thế việc kiểm tra trực tiếp với nhà hàng.

Flora does not guarantee allergy safety.

## User messages

| State | Message |
|-------|---------|
| Save success | Đã lưu cài đặt Flora AI. |
| Save failure | Flora chưa thể lưu thay đổi. Bạn vui lòng thử lại nhé. |
| Unauthorized | Bạn cần đăng nhập để quản lý cài đặt Flora AI. |
| Empty prefs hint | Bạn có thể thêm sở thích để Flora gợi ý tour và địa điểm phù hợp hơn. |

## Logging

OkHttp logs redact coordinates, Bearer tokens, and `foodAllergies` arrays.

## Known limitations

- No dedicated FCM push; `notificationConsent` is for in-app Flora reminders only.
- Android 13+ `POST_NOTIFICATIONS` is not auto-requested from this screen.
- Travel preferences edited on web and mobile share the same backend record (last save wins).

See also: [mobile-privacy-preferences-audit.md](./mobile-privacy-preferences-audit.md)

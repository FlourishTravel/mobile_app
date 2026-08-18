# Backend API chưa được Mobile sử dụng

Tổng hợp các endpoint user-facing của Server (BE) có sẵn nhưng mobile app **chưa gọi**.

Tổng cộng: 51 route (đã loại trừ admin/finance/webhook/planner/payment-gateway).

> **TRẠNG THÁI (08/2026):** Đã implement ĐẦY ĐỦ (API + UI) cho TẤT CẢ 51 route này.
> - 13 route đầu → màn hình UI chi tiết (Gói A) + 38 route còn lại → màn hình UI cơ bản (Gói B).
> - Data layer: `GoidbApiService` (40 method) + `GoidbModels.kt` (ApiResponse generic + 24 model).
> - UI: `GoidbScreens.kt` (10 màn list/detail) + `GoidbForms.kt` (9 màn form). Vào từ Homepage (2 hàng chip "Gói A" + "Gói B").
> - Màn hình Gói B hiển thị dữ liệu raw (`data.toString()`) do model sinh tự động dùng field nullable + fallback — UI sau này có thể parse chi tiết hơn khi cần.


## auth

- `POST  ` `/auth/facebook`
- `POST  ` `/auth/oauth`
- `POST  ` `/auth/refresh`
- `POST  ` `/auth/reset-password`

## bookings

- `POST  ` `/bookings/{}/request-refund`

## tours

- `DELETE` `/tours/{}`
- `GET   ` `/tours/availability/check`
- `GET   ` `/tours/by-slug/{}`
- `GET   ` `/tours/{}/similar`
- `POST  ` `/tours`
- `PUT   ` `/tours/{}`

## catalog

- `GET   ` `/catalog`
- `GET   ` `/catalog/tours/{}/detail`
- `POST  ` `/catalog/flora-recommend`

## categories

- `DELETE` `/categories/{}`
- `GET   ` `/categories/archived`
- `GET   ` `/categories/{}`
- `POST  ` `/categories`
- `POST  ` `/categories/{}/restore`
- `PUT   ` `/categories/{}`

## destinations

- `GET   ` `/destinations`
- `GET   ` `/destinations/festivals`
- `GET   ` `/destinations/festivals/{}`
- `GET   ` `/destinations/{}`
- `GET   ` `/destinations/{}/map-stats`
- `POST  ` `/destinations/flora-match`

## reviews

- `GET   ` `/reviews/featured`
- `GET   ` `/reviews/me`
- `GET   ` `/reviews/public`

## chat

- `GET   ` `/chat/rooms/{}/messages`
- `PATCH ` `/chat/messages/{}/pin`
- `PATCH ` `/chat/messages/{}/unpin`
- `POST  ` `/chat/messages/{}/reactions`

## chatbot

- `GET   ` `/chatbot/config`
- `GET   ` `/chatbot/config/intents`
- `GET   ` `/chatbot/nearby-places`
- `GET   ` `/chatbot/weather-forecast`
- `POST  ` `/chatbot/config/import`

## flora

- `POST  ` `/flora/bookings/{}/location`

## guide

- `GET   ` `/guide/sessions/{}/members`

## users

- `GET   ` `/users/me/travel-preferences`
- `PATCH ` `/users/me/travel-preferences`

## content

- `GET   ` `/content`
- `GET   ` `/content/{}`

## waitlist

- `POST  ` `/waitlist`

## promotions

- `GET   ` `/promotions/active`

## health

- `GET   ` `/health`

## contact-requests

- `POST  ` `/contact-requests`
- `POST  ` `/contact-requests/newsletter`

## guides

- `GET   ` `/guides`
- `GET   ` `/guides/{}`
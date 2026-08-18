# Tính năng mới trên LoginScreen (tháng 08/2026)

## 1. Quên mật khẩu
- Nút "Quên mật khẩu?" (góc phải dưới trường mật khẩu) mở `AlertDialog` nhập email.
- Gọi `POST /auth/forgot-password` (backend đã có sẵn, body `{email}`).
- Thành công → Toast "Đã gửi link đặt lại mật khẩu tới email" + đóng dialog.
- Backend luôn trả 200 (không lộ email tồn tại hay không) → message mặc định:
  "Nếu email tồn tại, bạn sẽ nhận link đặt lại mật khẩu".

## 2. Đăng nhập Google
- Nút tròn Google (trong nhóm "Đăng nhập nhanh") gọi Android Credential Manager
  (thư viện `androidx.credentials` + `googleid`) để lấy **id_token**.
- Sau đó gọi `POST /auth/google` (body `{id_token}`) → backend trả `AuthResponse`
  giống hệt login thường.
- Xử lý kết quả: `SessionManager.saveSession(...)` + route theo role
  (tour_guide → `onGuideLoginSuccess`, ngược lại → `onLoginSuccess`).

### ⚠️ BƯỚC CẤU HÌNH BẮT BUỘC (chưa có client ID thật)
Backend `/auth/google` validate id_token bằng Google client ID. Mobile phải gửi
id_token được cấp bởi **cùng một Web client ID**. Anh cần:

1. Vào Google Cloud Console → APIs & Services → Credentials → tạo **OAuth 2.0 Client ID**
   loại **Web application** (không phải Android type, vì backend verify phía server).
2. Lấy chuỗi `....apps.googleusercontent.com`.
3. Mở `app/src/main/res/values/strings.xml`, thay thế:
   ```xml
   <string name="google_web_client_id">YOUR_GOOGLE_WEB_CLIENT_ID.apps.googleusercontent.com</string>
   ```
   bằng client ID thật.
4. (Nếu backend chạy local) nhớ sửa `VITE_API_URL` trong `.env` bỏ `/api` (xem
   `docs/API_BACKEND_UNUSED_BY_MOBILE.md` mục context-path).

Nếu chưa điền client ID thật → nút Google build OK nhưng lúc chạy sẽ không hiện
tài khoản / báo lỗi verify. Phần code đã hoàn chỉnh, chỉ chờ client ID.

## Các file đã sửa
- `gradle/libs.versions.toml` — thêm versions `credentials`, `googleid` + 3 library aliases.
- `app/build.gradle.kts` — thêm 3 implementation (credentials, credentials-play-services-auth, googleid).
- `data/model/AuthModels.kt` — thêm `ForgotPasswordRequest`, `GoogleLoginRequest`.
- `data/api/AuthApiService.kt` — thêm `forgotPassword`, `googleLogin`.
- `res/values/strings.xml` — thêm `google_web_client_id` (placeholder).
- `data/auth/GoogleSignInHelper.kt` — MỚI: wrapper Credential Manager lấy id_token (có nonce).
- `ui/screens/LoginScreen.kt` — dialog quên mật khẩu + luồng Google sign-in + refactor chung `handleAuthSuccess`.

## Build
```
export JAVA_HOME="C:\Users\truon\jdks\jdk-17.0.20+8"
./gradlew.bat assembleDebug
```
APK: `app/build/outputs/apk/debug/app-debug.apk` (38.5 MB). Build SUCCESSFUL.

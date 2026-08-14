package com.example.flourishtravelapp.data.auth

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import java.util.UUID

sealed class GoogleSignInResult {
    data class Success(val idToken: String) : GoogleSignInResult()
    data class Cancelled(val message: String = "Đã huỷ đăng nhập Google.") : GoogleSignInResult()
    data class Failed(val message: String) : GoogleSignInResult()
}

/**
 * Nút Đăng nhập Google phải dùng GetSignInWithGoogleOption (dialog chọn tài khoản).
 * GetGoogleIdOption là One Tap — máy chưa từng đăng nhập thì trả NoCredentialException, app cũ nuốt lỗi.
 */
object GoogleSignInHelper {

    private const val TAG = "GoogleSignInHelper"

    suspend fun signIn(context: Context, serverClientId: String): GoogleSignInResult {
        if (serverClientId.isBlank() || serverClientId.startsWith("YOUR_GOOGLE")) {
            return GoogleSignInResult.Failed("Chưa cấu hình Google Web client ID.")
        }
        val activity = context.findActivity()
            ?: return GoogleSignInResult.Failed("Không mở được cửa sổ Google (thiếu Activity).")
        return try {
            val option = GetSignInWithGoogleOption.Builder(serverClientId)
                .setNonce(UUID.randomUUID().toString())
                .build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(option)
                .build()
            val result = CredentialManager.create(activity).getCredential(activity, request)
            val idToken = extractIdToken(result.credential)
            if (idToken.isNullOrBlank()) {
                GoogleSignInResult.Failed("Google không trả id_token. Kiểm tra Web client ID.")
            } else {
                GoogleSignInResult.Success(idToken)
            }
        } catch (e: GetCredentialCancellationException) {
            GoogleSignInResult.Cancelled()
        } catch (e: NoCredentialException) {
            Log.w(TAG, "No Google credential", e)
            GoogleSignInResult.Failed("Máy chưa có tài khoản Google. Thêm tài khoản Google trong Cài đặt rồi thử lại.")
        } catch (e: GetCredentialException) {
            Log.w(TAG, "Google credential error: ${e.type} ${e.localizedMessage}", e)
            GoogleSignInResult.Failed(mapCredentialError(e))
        } catch (e: Exception) {
            Log.w(TAG, "Google sign-in failed", e)
            GoogleSignInResult.Failed(e.localizedMessage ?: "Đăng nhập Google thất bại.")
        }
    }

    private fun extractIdToken(credential: androidx.credentials.Credential): String? {
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return try {
                GoogleIdTokenCredential.createFrom(credential.data).idToken
            } catch (e: GoogleIdTokenParsingException) {
                Log.e(TAG, "Failed to parse Google ID token", e)
                null
            }
        }
        return null
    }

    private fun mapCredentialError(e: GetCredentialException): String {
        val raw = "${e.type} ${e.localizedMessage}".lowercase()
        return when {
            raw.contains("cancel") -> "Đã huỷ đăng nhập Google."
            raw.contains("developer_error") || raw.contains("10:") || raw.contains("audience") ->
                "Google từ chối app (sai SHA-1 / package / client ID). Dùng APK build trên máy đã khai SHA-1."
            raw.contains("network") -> "Mất mạng khi đăng nhập Google."
            else -> e.localizedMessage ?: "Đăng nhập Google thất bại."
        }
    }
}

private fun Context.findActivity(): Activity? {
    var current: Context = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return null
}

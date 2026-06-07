package com.example.flourishtavelapp.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flourishtavelapp.ui.theme.*
import com.example.flourishtavelapp.data.api.RetrofitClient
import com.example.flourishtavelapp.data.api.FileUtils
import com.example.flourishtavelapp.data.model.ChangePasswordRequest
import com.example.flourishtavelapp.data.model.UpdateProfileRequest
import com.example.flourishtavelapp.data.model.UserInfo
import com.example.flourishtavelapp.data.session.SessionManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userName: String,
    userHandle: String,
    userEmail: String,
    userPhone: String,
    userAddress: String,
    notificationEnabled: Boolean,
    onProfileUpdate: (String, String, String, String, String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    // Intercept back press to return to Homepage
    BackHandler {
        onBack()
    }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val coroutineScope = rememberCoroutineScope()

    // Control dialogs
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var shakeToReport by remember { mutableStateOf(false) }

    // Loader and API messages
    var isLoading by remember { mutableStateOf(false) }
    var apiErrorMessage by remember { mutableStateOf<String?>(null) }

    // Local profile state backed by API loaded values
    var currentName by remember { mutableStateOf(userName) }
    var currentHandle by remember { mutableStateOf(userHandle) }
    var currentEmail by remember { mutableStateOf(userEmail) }
    var currentPhone by remember { mutableStateOf(userPhone) }
    var currentAddress by remember { mutableStateOf(userAddress) }
    var currentNotification by remember { mutableStateOf(notificationEnabled) }
    var currentAvatarUrl by remember { mutableStateOf(sessionManager.getUserInfo()?.avatarUrl) }

    // Sync input states inside edit modal
    var tempName by remember(currentName, showEditDialog) { mutableStateOf(currentName) }
    var tempHandle by remember(currentHandle, showEditDialog) { mutableStateOf(currentHandle) }
    var tempEmail by remember(currentEmail, showEditDialog) { mutableStateOf(currentEmail) }
    var tempPhone by remember(currentPhone, showEditDialog) { mutableStateOf(currentPhone) }
    var tempAddress by remember(currentAddress, showEditDialog) { mutableStateOf(currentAddress) }
    var tempNotification by remember(currentNotification) { mutableStateOf(currentNotification) }

    // Load Profile from Server on Enter
    LaunchedEffect(Unit) {
        isLoading = true
        apiErrorMessage = null
        try {
            val response = RetrofitClient.userApiService.getProfile()
            isLoading = false
            if (response.isSuccessful && response.body()?.success == true) {
                val profile = response.body()?.data
                if (profile != null) {
                    currentName = profile.fullName
                    currentEmail = profile.email
                    currentPhone = profile.phone ?: ""
                    currentAvatarUrl = profile.avatarUrl
                    currentHandle = "@${profile.email.substringBefore("@")}"
                    
                    // Update SessionManager
                    val updatedUserInfo = UserInfo(
                        id = profile.id,
                        email = profile.email,
                        fullName = profile.fullName,
                        role = profile.role,
                        avatarUrl = profile.avatarUrl,
                        phone = profile.phone
                    )
                    sessionManager.updateUserInfo(updatedUserInfo)
                    onProfileUpdate(
                        profile.fullName,
                        currentHandle,
                        profile.email,
                        profile.phone ?: "",
                        currentAddress,
                        currentNotification
                    )
                }
            } else {
                apiErrorMessage = response.body()?.message ?: "Lấy thông tin hồ sơ thất bại!"
            }
        } catch (e: Exception) {
            isLoading = false
            apiErrorMessage = "Lỗi kết nối: ${e.localizedMessage}"
        }
    }

    // Photo picker launcher for Avatar
    val avatarPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                coroutineScope.launch {
                    isLoading = true
                    apiErrorMessage = null
                    try {
                        val file = FileUtils.uriToFile(context, uri)
                        if (file != null) {
                            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                            val uploadResponse = RetrofitClient.uploadApiService.uploadFile(body)
                            
                            if (uploadResponse.isSuccessful && uploadResponse.body()?.success == true) {
                                val newAvatarUrl = uploadResponse.body()?.data
                                // Save to User Profile
                                val updateReq = UpdateProfileRequest(
                                    fullName = currentName,
                                    phone = currentPhone,
                                    avatarUrl = newAvatarUrl
                                )
                                val profileResponse = RetrofitClient.userApiService.updateProfile(updateReq)
                                
                                if (profileResponse.isSuccessful && profileResponse.body()?.success == true) {
                                    val profile = profileResponse.body()?.data
                                    if (profile != null) {
                                        currentAvatarUrl = profile.avatarUrl
                                        
                                        // Update Session Info
                                        val updatedUserInfo = UserInfo(
                                            id = profile.id,
                                            email = profile.email,
                                            fullName = profile.fullName,
                                            role = profile.role,
                                            avatarUrl = profile.avatarUrl,
                                            phone = profile.phone
                                        )
                                        sessionManager.updateUserInfo(updatedUserInfo)
                                        onProfileUpdate(
                                            profile.fullName,
                                            currentHandle,
                                            profile.email,
                                            profile.phone ?: "",
                                            currentAddress,
                                            currentNotification
                                        )
                                        Toast.makeText(context, "Cập nhật ảnh đại diện thành công!", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    apiErrorMessage = "Lỗi lưu ảnh hồ sơ: ${profileResponse.body()?.message}"
                                }
                            } else {
                                apiErrorMessage = "Upload ảnh thất bại: ${uploadResponse.body()?.message}"
                            }
                        } else {
                            apiErrorMessage = "Không thể xử lý ảnh."
                        }
                    } catch (e: Exception) {
                        apiErrorMessage = "Lỗi upload ảnh: ${e.localizedMessage}"
                    } finally {
                        isLoading = false
                    }
                }
            }
        }
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F5F9))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Pinned Brown Gradient Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF9E6549), Color(0xFF7D4329))
                        )
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back navigation arrow
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))

                    // Circular Avatar using AsyncImage
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .border(1.5.dp, Color.White, CircleShape)
                            .clickable { avatarPickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        val avatarUrlFull = if (!currentAvatarUrl.isNullOrEmpty()) {
                            if (currentAvatarUrl!!.startsWith("http")) currentAvatarUrl
                            else {
                                val rootUrl = RetrofitClient.BASE_URL.substringBefore("/api/")
                                "$rootUrl$currentAvatarUrl"
                            }
                        } else null

                        if (avatarUrlFull != null) {
                            AsyncImage(
                                model = avatarUrlFull,
                                contentDescription = "User Avatar",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        } else {
                            val initials = if (currentName.isNotEmpty()) currentName.take(1).uppercase() else "?"
                            Text(
                                text = initials,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Chào mừng, $currentName",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 19.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = currentEmail,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // VIP Bronze Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(Color.Black)
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "VIP",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = "Bronze",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Error display if any
            if (apiErrorMessage != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEE2E2),
                    border = BorderStroke(1.dp, Color(0xFFFCA5A5))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Error, null, tint = Color(0xFFDC2626), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(apiErrorMessage!!, color = Color(0xFF991B1B), fontSize = 13.sp, modifier = Modifier.weight(1f))
                        IconButton(onClick = { apiErrorMessage = null }, modifier = Modifier.size(18.dp)) {
                            Icon(Icons.Default.Close, null, tint = Color(0xFF991B1B), modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            // 2. Scrollable Cards List
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Card 1: Rewards & Savings
                ProfileCard(title = "Phần thưởng và tiết kiệm") {
                    ProfileRowItem(
                        icon = Icons.Outlined.ConfirmationNumber,
                        title = "Khuyến mãi"
                    )
                    ProfileRowItem(
                        icon = Icons.Outlined.Payments,
                        title = "Thưởng hoàn tiền mặt",
                        rightText = "0 đ"
                    )
                    ProfileRowItem(
                        icon = Icons.Outlined.Paid,
                        title = "Tiền Agoda",
                        rightText = "0 đ"
                    )
                }

                // Card 2: My Account
                ProfileCard(title = "Tài khoản của tôi") {
                    ProfileRowItem(
                        icon = Icons.Default.Person,
                        title = "Hồ sơ",
                        onClick = { showEditDialog = true }
                    )
                    ProfileRowItem(
                        icon = Icons.Outlined.Lock,
                        title = "Đổi mật khẩu",
                        onClick = { showChangePasswordDialog = true }
                    )
                    ProfileRowItem(
                        icon = Icons.Outlined.Comment,
                        title = "Tin nhắn từ cơ sở lưu trú"
                    )
                    ProfileRowItem(
                        icon = Icons.Outlined.FavoriteBorder,
                        title = "Danh sách yêu thích"
                    )
                    ProfileRowItem(
                        icon = Icons.Outlined.CreditCard,
                        title = "Thông tin thẻ đã lưu của tôi"
                    )
                    ProfileRowItem(
                        icon = Icons.Outlined.RateReview,
                        title = "Nhận xét của tôi"
                    )
                }

                // Card 3: Member Benefits
                ProfileCard(title = "Quyền lợi thành viên") {
                    ProfileRowItem(
                        icon = Icons.Default.Stars,
                        title = "AgodaVIP"
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .background(Color(0xFF2C3E50), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "P",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "PointsMAX",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1E293B)
                        )
                    }
                }

                // Card 4: Settings
                ProfileCard(title = "Cài đặt") {
                    ProfileRowItem(
                        icon = Icons.Default.Language,
                        title = "Ngôn ngữ",
                        rightText = "Tiếng Việt 🇻🇳"
                    )
                    ProfileRowItem(
                        icon = Icons.Outlined.LocalOffer,
                        title = "Giá hiển thị",
                        rightText = "Theo mỗi đêm"
                    )
                    
                    val currencyText = buildAnnotatedString {
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append("đ")
                        }
                        append(" | VND")
                    }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CurrencyExchange,
                            contentDescription = null,
                            tint = Color(0xFF2C3E50),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Tiền tệ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = currencyText,
                            color = Color(0xFF64748B),
                            fontSize = 14.sp
                        )
                    }

                    ProfileRowItem(
                        icon = Icons.Default.LocationOn,
                        title = "Khoảng cách",
                        rightText = "km"
                    )
                    ProfileRowItem(
                        icon = Icons.Outlined.Notifications,
                        title = "Thông báo",
                        rightText = if (tempNotification) "Bật" else "Tắt",
                        onClick = {
                            tempNotification = !tempNotification
                            onProfileUpdate(currentName, currentHandle, currentEmail, currentPhone, currentAddress, tempNotification)
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Smartphone,
                            contentDescription = null,
                            tint = Color(0xFF2C3E50),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Lắc điện thoại để báo cáo lỗi",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1E293B),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFF007AFF),
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Switch(
                            checked = shakeToReport,
                            onCheckedChange = { shakeToReport = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF5D4037)
                            )
                        )
                    }
                }

                // Card 5: Host listing advertisement
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = Color(0xFF008234),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Đăng cơ sở lưu trú của mình lên Agoda",
                            color = Color(0xFF008234),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                    }
                }

                // Card 6: Help & Info
                ProfileCard(title = "Trợ giúp và thông tin") {
                    ProfileRowItem(
                        icon = Icons.Default.Info,
                        title = "Về chúng tôi"
                    )
                    ProfileRowItem(
                        icon = Icons.Default.HeadsetMic,
                        title = "Trung tâm Trợ giúp"
                    )
                }

                // Card 7: Account Management
                ProfileCard(title = "Quản lý tài khoản") {
                    ProfileRowItem(
                        icon = Icons.Default.Delete,
                        title = "Xóa Tài Khoản",
                        onClick = { showDeleteConfirmDialog = true }
                    )
                    ProfileRowItem(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        title = "Thoát",
                        onClick = {
                            coroutineScope.launch {
                                isLoading = true
                                try {
                                    RetrofitClient.authApiService.logout()
                                } catch (e: Exception) {
                                    // Proceed to clear local session even on failure
                                } finally {
                                    isLoading = false
                                    onLogout()
                                }
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Circular Loading Overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        }
    }

    // ── Dialogs ─────────────────────────────────────────────────────────────
    
    // Edit Profile Modal Dialog
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(
                    "Chỉnh sửa hồ sơ",
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Tên hiển thị") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = tempHandle,
                        onValueChange = { tempHandle = it },
                        label = { Text("Username (@...)") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        singleLine = true,
                        enabled = false // Autogenerated email prefix
                    )
                    OutlinedTextField(
                        value = tempEmail,
                        onValueChange = { tempEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        singleLine = true,
                        enabled = false // Email usually cannot be changed in standard update
                    )
                    OutlinedTextField(
                        value = tempPhone,
                        onValueChange = { tempPhone = it },
                        label = { Text("Số điện thoại") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = tempAddress,
                        onValueChange = { tempAddress = it },
                        label = { Text("Địa chỉ") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            isLoading = true
                            apiErrorMessage = null
                            try {
                                val updateReq = UpdateProfileRequest(
                                    fullName = tempName,
                                    phone = tempPhone,
                                    avatarUrl = currentAvatarUrl
                                )
                                val response = RetrofitClient.userApiService.updateProfile(updateReq)
                                isLoading = false
                                if (response.isSuccessful && response.body()?.success == true) {
                                    val profile = response.body()?.data
                                    if (profile != null) {
                                        currentName = profile.fullName
                                        currentEmail = profile.email
                                        currentPhone = profile.phone ?: ""
                                        currentAvatarUrl = profile.avatarUrl
                                        currentHandle = "@${profile.email.substringBefore("@")}"
                                        
                                        // Update SessionManager
                                        val updatedUserInfo = UserInfo(
                                            id = profile.id,
                                            email = profile.email,
                                            fullName = profile.fullName,
                                            role = profile.role,
                                            avatarUrl = profile.avatarUrl,
                                            phone = profile.phone
                                        )
                                        sessionManager.updateUserInfo(updatedUserInfo)
                                        
                                        onProfileUpdate(
                                            profile.fullName,
                                            currentHandle,
                                            profile.email,
                                            profile.phone ?: "",
                                            tempAddress,
                                            tempNotification
                                        )
                                        showEditDialog = false
                                        Toast.makeText(context, "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    apiErrorMessage = "Cập nhật thất bại: " + (response.body()?.message ?: "Lỗi hệ thống")
                                }
                            } catch (e: Exception) {
                                isLoading = false
                                apiErrorMessage = "Lỗi kết nối: ${e.localizedMessage}"
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Lưu", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Hủy", color = SecondaryTextColor)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }

    // Change Password Dialog
    if (showChangePasswordDialog) {
        var oldPassword by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var confirmNewPassword by remember { mutableStateOf("") }
        var changePasswordError by remember { mutableStateOf<String?>(null) }
        var changePasswordSuccess by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            title = {
                Text(
                    "Đổi mật khẩu",
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (changePasswordError != null) {
                        Text(
                            text = changePasswordError!!,
                            color = Color.Red,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    if (changePasswordSuccess) {
                        Text(
                            text = "Đổi mật khẩu thành công!",
                            color = Color(0xFF388E3C),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = { Text("Mật khẩu cũ") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        singleLine = true,
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
                    )
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Mật khẩu mới") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        singleLine = true,
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
                    )
                    OutlinedTextField(
                        value = confirmNewPassword,
                        onValueChange = { confirmNewPassword = it },
                        label = { Text("Xác nhận mật khẩu mới") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        singleLine = true,
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                            changePasswordError = "Vui lòng nhập đầy đủ thông tin!"
                            return@Button
                        }
                        if (newPassword != confirmNewPassword) {
                            changePasswordError = "Mật khẩu xác nhận không khớp!"
                            return@Button
                        }
                        if (newPassword.length < 6) {
                            changePasswordError = "Mật khẩu mới phải có ít nhất 6 ký tự!"
                            return@Button
                        }
                        coroutineScope.launch {
                            isLoading = true
                            changePasswordError = null
                            try {
                                val response = RetrofitClient.authApiService.changePassword(
                                    ChangePasswordRequest(oldPassword, newPassword)
                                )
                                isLoading = false
                                if (response.isSuccessful && response.body()?.success == true) {
                                    changePasswordSuccess = true
                                    oldPassword = ""
                                    newPassword = ""
                                    confirmNewPassword = ""
                                    kotlinx.coroutines.delay(1500)
                                    showChangePasswordDialog = false
                                } else {
                                    changePasswordError = response.body()?.message ?: "Đổi mật khẩu thất bại!"
                                }
                            } catch (e: Exception) {
                                isLoading = false
                                changePasswordError = "Lỗi kết nối: ${e.localizedMessage}"
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Đổi mật khẩu", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePasswordDialog = false }) {
                    Text("Hủy", color = SecondaryTextColor)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }

    // Delete Account Confirmation Dialog
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = {
                Text(
                    "Xóa tài khoản",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    "Bạn có chắc chắn muốn xóa tài khoản này không? Hành động này không thể hoàn tác và tất cả các thông tin đặt chỗ sẽ bị mất.",
                    color = DarkTextColor,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        coroutineScope.launch {
                            isLoading = true
                            try {
                                RetrofitClient.authApiService.logout()
                            } catch (e: Exception) {}
                            isLoading = false
                            onLogout()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Xóa tài khoản", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Hủy", color = SecondaryTextColor)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}

// Reusable card container matching standard iOS / Android card menus
@Composable
fun ProfileCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Color(0xFF5A6E85),
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            content()
        }
    }
}

// Reusable profile item row with text and standard arrow indicator
@Composable
fun ProfileRowItem(
    icon: ImageVector,
    title: String,
    iconColor: Color = Color(0xFF2C3E50),
    rightText: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFF1E293B)
        )
        Spacer(modifier = Modifier.weight(1f))
        if (rightText != null) {
            Text(
                text = rightText,
                color = Color(0xFF64748B),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

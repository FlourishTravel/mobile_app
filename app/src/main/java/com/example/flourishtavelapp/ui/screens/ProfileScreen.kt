package com.example.flourishtavelapp.ui.screens

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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*

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
    // Control dialogs
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var shakeToReport by remember { mutableStateOf(false) }

    // Sync input states using key bindings
    var tempName by remember(userName) { mutableStateOf(userName) }
    var tempHandle by remember(userHandle) { mutableStateOf(userHandle) }
    var tempEmail by remember(userEmail) { mutableStateOf(userEmail) }
    var tempPhone by remember(userPhone) { mutableStateOf(userPhone) }
    var tempAddress by remember(userAddress) { mutableStateOf(userAddress) }
    var tempNotification by remember(notificationEnabled) { mutableStateOf(notificationEnabled) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F5F9)) // Premium Light grey-blue background
    ) {
        // 1. Pinned Brown Gradient Header (Bronze Accent)
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
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Chào mừng, $userName",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 19.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = userEmail,
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

        // 2. Scrollable Cards List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Card 1: Phần thưởng và tiết kiệm (Rewards & Savings)
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

            // Card 2: Tài khoản của tôi (My Account)
            ProfileCard(title = "Tài khoản của tôi") {
                ProfileRowItem(
                    icon = Icons.Default.Person,
                    title = "Hồ sơ",
                    onClick = { showEditDialog = true }
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

            // Card 3: Quyền lợi thành viên (Member Benefits)
            ProfileCard(title = "Quyền lợi thành viên") {
                ProfileRowItem(
                    icon = Icons.Default.Stars,
                    title = "AgodaVIP"
                )
                
                // PointsMAX item with custom letter 'P' icon matching the style
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

            // Card 4: Cài đặt (Settings)
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
                
                // Underlined currency 'đ' text
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
                        onProfileUpdate(tempName, tempHandle, tempEmail, tempPhone, tempAddress, tempNotification)
                    }
                )

                // Lắc điện thoại để báo cáo lỗi
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

            // Card 5: Đăng cơ sở lưu trú của mình lên Agoda
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

            // Card 6: Trợ giúp và thông tin (Help & Info)
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

            // Card 7: Quản lý tài khoản (Account Management)
            ProfileCard(title = "Quản lý tài khoản") {
                ProfileRowItem(
                    icon = Icons.Default.Delete,
                    title = "Xóa Tài Khoản",
                    onClick = { showDeleteConfirmDialog = true }
                )
                ProfileRowItem(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = "Thoát",
                    onClick = onLogout
                )
            }

            Spacer(modifier = Modifier.height(100.dp)) // generous spacer for navigation bar
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
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = tempEmail,
                        onValueChange = { tempEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        singleLine = true
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
                        onProfileUpdate(tempName, tempHandle, tempEmail, tempPhone, tempAddress, tempNotification)
                        showEditDialog = false
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
                        onLogout() // log out after delete mockup
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

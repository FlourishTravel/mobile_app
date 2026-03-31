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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    var isEditingMain by remember { mutableStateOf(false) }
    var isEditingDetails by remember { mutableStateOf(false) }
    
    // Local states for editing
    var tempName by remember { mutableStateOf(userName) }
    var tempHandle by remember { mutableStateOf(userHandle) }
    var tempEmail by remember { mutableStateOf(userEmail) }
    var tempPhone by remember { mutableStateOf(userPhone) }
    var tempAddress by remember { mutableStateOf(userAddress) }
    var tempNotification by remember { mutableStateOf(notificationEnabled) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkTextColor)
            }
            Text(
                text = "Hồ sơ",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkTextColor
            )
            IconButton(onClick = { /* Settings */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = DarkTextColor)
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image Section
            Box(contentAlignment = Alignment.BottomCenter) {
                Box(contentAlignment = Alignment.Center) {
                    Surface(
                        modifier = Modifier.size(140.dp).padding(8.dp),
                        shape = CircleShape,
                        border = BorderStroke(2.dp, Color.White),
                        color = Color.Black
                    ) {
                        // Placeholder for Fox Avatar
                        Icon(
                            Icons.Default.Face,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                    // Camera Icon Overlay for changing image
                    Surface(
                        modifier = Modifier.size(40.dp).offset(x = 45.dp, y = 35.dp).clickable { /* Action to change image */ },
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 4.dp
                    ) {
                        Icon(Icons.Default.CameraAlt, null, tint = PrimaryGreen, modifier = Modifier.padding(8.dp))
                    }
                }
                
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PrimaryGreen,
                    modifier = Modifier.offset(y = 10.dp)
                ) {
                    Text(
                        "PRO TRAVELER",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // User Name & Handle Section
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    if (isEditingMain) {
                        OutlinedTextField(
                            value = tempName,
                            onValueChange = { tempName = it },
                            label = { Text("Tên hiển thị") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = tempHandle,
                            onValueChange = { tempHandle = it },
                            label = { Text("Username (@...)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                onProfileUpdate(tempName, tempHandle, tempEmail, tempPhone, tempAddress, tempNotification)
                                isEditingMain = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                        ) {
                            Text("Lưu thay đổi")
                        }
                    } else {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextColor
                        )
                        Text(
                            text = userHandle,
                            color = SecondaryTextColor,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { isEditingMain = true }) {
                            Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp), tint = PrimaryGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Chỉnh sửa tên & username", fontSize = 12.sp, color = PrimaryGreen)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Personal Info Section
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Thông tin cá nhân", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                        IconButton(onClick = { isEditingDetails = !isEditingDetails }) {
                            Icon(if (isEditingDetails) Icons.Default.Close else Icons.Default.Edit, null, tint = SecondaryTextColor)
                        }
                    }
                    
                    if (isEditingDetails) {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(value = tempEmail, onValueChange = { tempEmail = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = tempPhone, onValueChange = { tempPhone = it }, label = { Text("Số điện thoại") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = tempAddress, onValueChange = { tempAddress = it }, label = { Text("Địa chỉ") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                onProfileUpdate(tempName, tempHandle, tempEmail, tempPhone, tempAddress, tempNotification)
                                isEditingDetails = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                        ) {
                            Text("Lưu thông tin")
                        }
                    } else {
                        Spacer(modifier = Modifier.height(16.dp))
                        InfoRow(Icons.Default.Email, "Email", userEmail)
                        InfoRow(Icons.Default.Phone, "SĐT", userPhone)
                        InfoRow(Icons.Default.LocationOn, "Địa chỉ", userAddress)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notifications Section
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Cài đặt thông báo", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("Cảnh báo, tin tức, cập nhật", color = SecondaryTextColor, fontSize = 12.sp)
                        }
                    }
                    Switch(
                        checked = tempNotification,
                        onCheckedChange = { 
                            tempNotification = it
                            onProfileUpdate(tempName, tempHandle, tempEmail, tempPhone, tempAddress, it)
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = PrimaryGreen)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            // Logout Button
            Button(
                onClick = onLogout,
                modifier = Modifier.padding(bottom = 60.dp).height(50.dp).padding(horizontal = 40.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.1f), contentColor = Color.Red)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Đăng xuất tài khoản", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = SecondaryTextColor, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 10.sp, color = SecondaryTextColor, fontWeight = FontWeight.Bold)
            Text(value, style = MaterialTheme.typography.bodyMedium, color = DarkTextColor)
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DarkTextColor)
        Text(label, fontSize = 10.sp, color = SecondaryTextColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun HistoryItem(title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Surface(modifier = Modifier.size(50.dp), shape = CircleShape, color = Color.LightGray) {
            // Placeholder for image
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(subtitle, color = SecondaryTextColor, fontSize = 12.sp)
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
    }
}

@Composable
fun SavedLocationItem(label: String, modifier: Modifier) {
    Column(modifier = modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth().height(100.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color.LightGray
        ) {
            // Placeholder for image
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}

@Composable
fun ProfileMenuOption(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = Color(0xFFF5F5F5)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = SecondaryTextColor, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(subtitle, color = SecondaryTextColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
    }
}

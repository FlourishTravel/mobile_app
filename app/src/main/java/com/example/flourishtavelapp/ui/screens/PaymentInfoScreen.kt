package com.example.flourishtravelapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.ui.theme.*
import com.example.flourishtravelapp.data.session.SessionManager

@Composable
fun PaymentInfoScreen(
    adultCount: Int,
    childCount: Int,
    name: String,
    email: String,
    phone: String,
    idCard: String,
    gender: String,
    note: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onIdCardChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
    promoDiscount: Long = 0L // added
) {
    // Hardware back press behavior
    BackHandler {
        onBack()
    }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // Auto prefill info from SessionManager
    LaunchedEffect(Unit) {
        val userInfo = sessionManager.getUserInfo()
        if (userInfo != null) {
            if (name.isBlank() || name == "Nguyễn Văn A") {
                onNameChange(userInfo.fullName)
            }
            if (email.isBlank() || email == "example@mail.com") {
                onEmailChange(userInfo.email)
            }
            if (phone.isBlank() || phone == "090 123 4567") {
                onPhoneChange(userInfo.phone ?: "")
            }
        }
    }

    val adultPrice = 1000000
    val childPrice = 450000
    val totalAmount = (adultCount.toLong() * adultPrice) + (childCount.toLong() * childPrice)
    val displayTotal = if (totalAmount - promoDiscount > 0) totalAmount - promoDiscount else 0L

    Box(modifier = modifier.fillMaxSize().background(NatureGreenBackground)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkTextColor)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Contact Info",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor
                )
                Spacer(modifier = Modifier.weight(1.2f))
            }

            // Step Indicator
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                StepItem(number = "1", label = "Bước 1: Thông tin", isActive = true)
                Box(modifier = Modifier.width(30.dp).height(1.dp).background(Color.LightGray).padding(horizontal = 8.dp))
                StepItem(number = "2", label = "Bước 2: Thanh toán", isActive = false)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // Tour Preview Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Box {
                            Image(
                                painter = painterResource(id = com.example.flourishtravelapp.R.drawable.maya_bg),
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(24.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Surface(
                                modifier = Modifier.padding(12.dp).align(Alignment.TopStart),
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White.copy(alpha = 0.9f)
                            ) {
                                Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(12.dp))
                                    Text(" 4.9", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, tint = PrimaryGreen, modifier = Modifier.size(14.dp))
                            Text(" THAILAND", color = PrimaryGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("BANGKOK - PATTAYA", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = DarkTextColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarMonth, null, tint = SecondaryTextColor, modifier = Modifier.size(14.dp))
                            Text(" 15/10 - 20/10/2023", color = SecondaryTextColor, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(Icons.Default.People, null, tint = SecondaryTextColor, modifier = Modifier.size(14.dp))
                            Text(" ${adultCount + childCount} người", color = SecondaryTextColor, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Contact Info Form
                Text("Thông Tin Liên Hệ", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkTextColor)
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        PaymentInputField(label = "HỌ VÀ TÊN", value = name, onValueChange = onNameChange, placeholder = "Nguyễn Văn A")
                        Spacer(modifier = Modifier.height(16.dp))
                        PaymentInputField(label = "EMAIL", value = email, onValueChange = onEmailChange, placeholder = "example@mail.com")
                        Spacer(modifier = Modifier.height(16.dp))
                        PaymentInputField(label = "SỐ ĐIỆN THOẠI", value = phone, onValueChange = onPhoneChange, placeholder = "090 123 4567")
                        Spacer(modifier = Modifier.height(16.dp))
                        PaymentInputField(label = "CĂN CƯỚC CÔNG DÂN", value = idCard, onValueChange = onIdCardChange, placeholder = "12 số CMND/CCCD")
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("GIỚI TÍNH", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SecondaryTextColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            GenderButton(label = "Nam", icon = Icons.Default.Male, isSelected = gender == "Nam", onClick = { onGenderChange("Nam") }, modifier = Modifier.weight(1f))
                            GenderButton(label = "Nữ", icon = Icons.Default.Female, isSelected = gender == "Nữ", onClick = { onGenderChange("Nữ") }, modifier = Modifier.weight(1f))
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        PaymentInputField(label = "GHI CHÚ", value = note, onValueChange = onNoteChange, placeholder = "Yêu cầu đặc biệt cho chuyến đi...", singleLine = false)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Total Amount Summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F4F2).copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Adults (%,dđ x $adultCount)".format(adultPrice), color = SecondaryTextColor, fontSize = 12.sp)
                            Text("%,dđ".format(adultCount * adultPrice.toLong()), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        if (childCount > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Children (%,dđ x $childCount)".format(childPrice), color = SecondaryTextColor, fontSize = 12.sp)
                                Text("%,dđ".format(childCount * childPrice.toLong()), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                        if (promoDiscount > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Mã giảm giá", color = Color(0xFF00BFA5), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("-%,dđ".format(promoDiscount), color = Color(0xFF00BFA5), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.3f))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Tổng cộng", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("%,dđ".format(displayTotal), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = PrimaryGreen)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(120.dp))
            }
        }

        // Fixed Bottom Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Tiếp tục ->", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun StepItem(number: String, label: String, isActive: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(28.dp),
            shape = CircleShape,
            color = if (isActive) PrimaryGreen else Color(0xFFE0E7E2)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(number, color = if (isActive) Color.White else SecondaryTextColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isActive) DarkTextColor else SecondaryTextColor)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentInputField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String, singleLine: Boolean = true) {
    Column {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SecondaryTextColor)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, fontSize = 14.sp, color = Color.Gray) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = LightGreenBackground,
                unfocusedContainerColor = LightGreenBackground,
                focusedBorderColor = PrimaryGreen,
                unfocusedBorderColor = Color.Transparent
            ),
            singleLine = singleLine
        )
    }
}

@Composable
private fun GenderButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    Surface(
        modifier = modifier.height(48.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) PrimaryGreen else LightGreenBackground
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = if (isSelected) Color.White else SecondaryTextColor, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, color = if (isSelected) Color.White else SecondaryTextColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}


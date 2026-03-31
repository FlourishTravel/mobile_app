package com.example.flourishtavelapp.ui.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*

@Composable
fun BankTransferScreen(
    adultCount: Int,
    childCount: Int,
    onBack: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val adultPrice = 1000000
    val childPrice = 450000
    val totalAmount = (adultCount.toLong() * adultPrice) + (childCount.toLong() * childPrice)

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
                    "Thanh toán",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor
                )
                Spacer(modifier = Modifier.weight(1f))
                Text("Bước 2/2", color = SecondaryTextColor, fontSize = 12.sp)
            }

            // Step Indicator - Synchronized with Step 1 design
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                StepItem(number = "1", label = "Bước 1: Thông tin", isActive = true, isCompleted = true)
                Box(modifier = Modifier.width(30.dp).height(1.dp).background(PrimaryGreen))
                StepItem(number = "2", label = "Bước 2: Thanh toán", isActive = true)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main Payment Content
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("TỔNG SỐ TIỀN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SecondaryTextColor)
                    Text("%,dđ".format(totalAmount), fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = DarkTextColor)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // QR Code
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFF0D1B3E))
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.QrCode2, null, tint = Color.White, modifier = Modifier.size(120.dp))
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.QrCodeScanner, null, tint = SecondaryTextColor, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Quét mã để thanh toán nhanh", color = SecondaryTextColor, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Bank Account Details
                    BankDetailRow(label = "NGÂN HÀNG", value = "Vietcombank", icon = Icons.Default.AccountBalance)
                    Spacer(modifier = Modifier.height(16.dp))
                    BankDetailRow(label = "SỐ TÀI KHOẢN", value = "1234567890", icon = Icons.Default.ContentCopy)
                    Spacer(modifier = Modifier.height(16.dp))
                    BankDetailRow(label = "CHỦ TÀI KHOẢN", value = "FLOURISH TRAVEL", icon = Icons.Default.Person)

                    Spacer(modifier = Modifier.height(32.dp))

                    // Transfer Content (Memo)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        color = LightGreenBackground.copy(alpha = 0.7f)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("NỘI DUNG CHUYỂN KHOẢN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SecondaryTextColor)
                                Text("FLM-BOOKING-ID", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = PrimaryGreen)
                            }
                            Button(
                                onClick = { /* Copy */ },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text("SAO CHÉP", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Safety Badge
            Surface(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE0F2F1)
            ) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VerifiedUser, null, tint = Color(0xFF00BFA5), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Giao dịch an toàn & bảo mật", color = Color(0xFF00BFA5), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Vui lòng chuyển đúng số tiền và nội dung để hệ thống\ntự động xác nhận nhanh nhất.",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 11.sp,
                color = SecondaryTextColor,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(120.dp))
        }

        // Final Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Button(
                onClick = onComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Tôi đã chuyển khoản", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun StepItem(number: String, label: String, isActive: Boolean = false, isCompleted: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(28.dp),
            shape = CircleShape,
            color = if (isActive || isCompleted) PrimaryGreen else Color(0xFFE0E7E2)
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isCompleted) {
                    Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                } else {
                    Text(number, color = if (isActive) Color.White else SecondaryTextColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isActive || isCompleted) DarkTextColor else SecondaryTextColor)
    }
}

@Composable
fun BankDetailRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SecondaryTextColor)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
        }
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = Color(0xFFF1F4F2)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = DarkTextColor, modifier = Modifier.size(18.dp))
            }
        }
    }
}

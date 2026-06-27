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
import androidx.compose.runtime.Composable
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
import com.example.flourishtravelapp.ui.theme.*

@Composable
fun BookingSuccessScreen(
    adultCount: Int,
    childCount: Int,
    name: String,
    email: String,
    idCard: String,
    gender: String,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier,
    bookingId: String = "",
    orderId: String = "",
    promoDiscount: Long = 0L // added
) {
    // Intercept back button to return to home since booking is completed
    BackHandler {
        onHomeClick()
    }

    val adultPrice = 1000000
    val childPrice = 450000
    val totalAmount = (adultCount.toLong() * adultPrice) + (childCount.toLong() * childPrice)
    val finalTotal = if (totalAmount - promoDiscount > 0) totalAmount - promoDiscount else 0L

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onHomeClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkTextColor)
                    }
                    Text(
                        "Xác nhận đặt tour",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextColor
                    )
                }
                Text(
                    "Trang chủ",
                    modifier = Modifier.clickable { onHomeClick() },
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Success Icon
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = PrimaryGreen
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(48.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Thanh toán thành công!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkTextColor
                )
                
                Text(
                    "Chúc mừng bạn đã sở hữu tấm vé tới thiên đường du lịch.",
                    modifier = Modifier.padding(horizontal = 40.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = SecondaryTextColor,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Tour Info Card (Maya Phi Phi)
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = com.example.flourishtravelapp.R.drawable.maya_bg),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp).clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("THAILAND ADVENTURE", color = PrimaryGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("VỊNH MAYA - PHI PHI", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkTextColor)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarMonth, null, tint = SecondaryTextColor, modifier = Modifier.size(14.dp))
                                Text(" 5 Ngày 4 Đêm", color = SecondaryTextColor, fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(16.dp))
                                Icon(Icons.Default.People, null, tint = SecondaryTextColor, modifier = Modifier.size(14.dp))
                                Text(" ${adultCount + childCount} Người", color = SecondaryTextColor, fontSize = 12.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // QR Code Ticket Card
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = NatureGreenBackground)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // QR Graphic Placeholder
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.White)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.QrCode2, null, tint = DarkTextColor, modifier = Modifier.fillMaxSize())
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Mã vé điện tử của bạn", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
                        
                        if (bookingId.isNotEmpty()) {
                            Text("Mã đặt chỗ: $bookingId", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = PrimaryGreen, modifier = Modifier.padding(top = 4.dp))
                        }
                        if (orderId.isNotEmpty()) {
                            Text("Mã đơn hàng: $orderId", fontSize = 12.sp, color = SecondaryTextColor)
                        }

                        Text(
                            "Lưu mã QR này để check-in tại điểm hẹn một cách nhanh chóng.",
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            color = SecondaryTextColor,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Contact Info Card
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Thông tin liên hệ", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkTextColor)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        SuccessInfoRow(label = "Họ và tên", value = name)
                        SuccessInfoRow(label = "Giới tính", value = gender)
                        SuccessInfoRow(label = "Email", value = email)
                        SuccessInfoRow(label = "Căn cước công dân", value = idCard)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Total Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("TỔNG THANH TOÁN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SecondaryTextColor)
                            Text("%,d VND".format(finalTotal), fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = PrimaryGreen)
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = LightGreenBackground
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF00BFA5), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ĐÃ THANH TOÁN", color = Color(0xFF00BFA5), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(120.dp))
            }
        }

        // Final Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Button(
                onClick = onHomeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Về Trang chủ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun SuccessInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = SecondaryTextColor, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Bold, color = DarkTextColor, fontSize = 14.sp)
    }
}


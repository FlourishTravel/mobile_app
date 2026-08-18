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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.UserBookingDetailDto
import com.example.flourishtravelapp.data.util.resolveMediaUrl
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
    promoDiscount: Long = 0L
) {
    BackHandler {
        onHomeClick()
    }

    var detail by remember { mutableStateOf<UserBookingDetailDto?>(null) }
    var isLoading by remember { mutableStateOf(bookingId.isNotBlank()) }

    LaunchedEffect(bookingId) {
        if (bookingId.isBlank()) {
            isLoading = false
            return@LaunchedEffect
        }
        try {
            val response = RetrofitClient.bookingApiService.getBookingDetail(bookingId)
            if (response.isSuccessful) {
                detail = response.body()?.data
            }
        } catch (_: Exception) {
        } finally {
            isLoading = false
        }
    }

    val status = detail?.bookingStatus?.lowercase().orEmpty()
    val paid = status == "paid" || status == "confirmed" || status == "completed"
    val guestCount = detail?.guestCount ?: (adultCount + childCount)
    val amount = detail?.totalAmount?.toLong()
    val title = detail?.tourTitle ?: "Đơn đặt tour"
    val dates = listOfNotNull(detail?.sessionStartDate, detail?.sessionEndDate)
        .distinct()
        .joinToString(" → ")
        .ifBlank { null }
    val nights = detail?.tourDurationNights
    val days = detail?.tourDurationDays

    Box(modifier = modifier.fillMaxSize().background(NatureGreenBackground)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
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

                if (isLoading) {
                    CircularProgressIndicator(color = PrimaryGreen)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = if (paid) PrimaryGreen else Color(0xFFF59E0B)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            if (paid) Icons.Default.Check else Icons.Default.Schedule,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    if (paid) "Thanh toán thành công!" else "Đã tạo đơn đặt tour",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkTextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Text(
                    if (paid)
                        "Vé đã ghi nhận. Mang mã đặt chỗ khi check-in."
                    else
                        "Nếu vừa thoát PayOS, hoàn tất trong 15 phút — hết hạn đơn tự hủy và trả chỗ.",
                    modifier = Modifier.padding(horizontal = 40.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = SecondaryTextColor,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = resolveMediaUrl(detail?.tourThumbnailUrl),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp).clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = com.example.flourishtravelapp.R.drawable.maya_bg),
                            error = painterResource(id = com.example.flourishtravelapp.R.drawable.maya_bg)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                detail?.categoryName?.uppercase() ?: "FLOURISH TRAVEL",
                                color = PrimaryGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarMonth, null, tint = SecondaryTextColor, modifier = Modifier.size(14.dp))
                                Text(
                                    " " + (dates ?: listOfNotNull(days?.let { "$it ngày" }, nights?.let { "$it đêm" }).joinToString(" ").ifBlank { "—" }),
                                    color = SecondaryTextColor,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Icon(Icons.Default.People, null, tint = SecondaryTextColor, modifier = Modifier.size(14.dp))
                                Text(" $guestCount khách", color = SecondaryTextColor, fontSize = 12.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = NatureGreenBackground)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                        Text("Mã đặt chỗ", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
                        val displayCode = detail?.bookingCode?.takeIf { it.isNotBlank() }
                            ?: bookingId.replace("-", "").take(8).uppercase().let { if (it.length == 8) "FT-$it" else bookingId }
                        if (displayCode.isNotEmpty()) {
                            Text(
                                displayCode,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = PrimaryGreen,
                                modifier = Modifier.padding(top = 4.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        val showOrder = detail?.paymentOrderId ?: orderId
                        if (!showOrder.isNullOrBlank() && showOrder != displayCode) {
                            Text("Mã thanh toán: $showOrder", fontSize = 12.sp, color = SecondaryTextColor)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                        SuccessInfoRow(label = "Email", value = detail?.customerEmail ?: email)
                        if (idCard.isNotBlank()) SuccessInfoRow(label = "Căn cước", value = idCard)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                            Text(
                                if (amount != null) "%,d VND".format(amount) else "—",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                color = PrimaryGreen
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (paid) LightGreenBackground else Color(0xFFFFF7ED)
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    if (paid) Icons.Default.CheckCircle else Icons.Default.Schedule,
                                    null,
                                    tint = if (paid) Color(0xFF00BFA5) else Color(0xFFF59E0B),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    if (paid) "ĐÃ THANH TOÁN" else "CHỜ THANH TOÁN",
                                    color = if (paid) Color(0xFF00BFA5) else Color(0xFFF59E0B),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(120.dp))
            }
        }

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

package com.example.flourishtravelapp.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.ui.theme.*
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.CreateBookingRequest
import com.example.flourishtravelapp.data.model.GuestItem
import kotlinx.coroutines.launch

@Composable
fun BankTransferScreen(
    adultCount: Int,
    childCount: Int,
    onBack: () -> Unit,
    onComplete: (String, String) -> Unit, // returns bookingId, orderId
    sessionId: String,
    promoCode: String?,
    promoDiscount: Long,
    bookingName: String,
    bookingEmail: String,
    bookingPhone: String,
    bookingIdCard: String,
    bookingGender: String,
    bookingNote: String,
    paymentMethod: String = "Bank Transfer",
    modifier: Modifier = Modifier
) {
    // Hardware back press behavior
    BackHandler {
        onBack()
    }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()

    val adultPrice = 1000000
    val childPrice = 450000
    val totalAmount = (adultCount.toLong() * adultPrice) + (childCount.toLong() * childPrice)
    val finalTotal = if (totalAmount - promoDiscount > 0) totalAmount - promoDiscount else 0L

    var isProcessing by remember { mutableStateOf(false) }
    var apiError by remember { mutableStateOf<String?>(null) }

    val isOnlinePayment = paymentMethod == "Online Payment"

    suspend fun openPaymentUrl(bookingId: String, paymentUrl: String?) {
        var url = paymentUrl
        if (url.isNullOrBlank()) {
            val momoResponse = RetrofitClient.bookingApiService.getMomoPaymentUrl(bookingId)
            if (momoResponse.isSuccessful && momoResponse.body()?.success == true) {
                url = momoResponse.body()?.data?.paymentUrl
            }
        }
        if (!url.isNullOrBlank()) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

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
                    if (isOnlinePayment) "Thanh toán MoMo" else "Thanh toán",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor
                )
                Spacer(modifier = Modifier.weight(1f))
                Text("Bước 2/2", color = SecondaryTextColor, fontSize = 12.sp)
            }

            // Step Indicator
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
                    // API error banner
                    if (apiError != null) {
                        Surface(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFEE2E2),
                            border = BorderStroke(1.dp, Color(0xFFFCA5A5))
                        ) {
                            Text(
                                text = apiError!!,
                                color = Color(0xFF991B1B),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(12.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Text("TỔNG SỐ TIỀN THỰC TẾ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SecondaryTextColor)
                    Text("%,dđ".format(finalTotal), fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = DarkTextColor)
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    if (isOnlinePayment) {
                        Icon(Icons.Default.AccountBalanceWallet, null, tint = PrimaryGreen, modifier = Modifier.size(80.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Vui lòng hoàn tất thanh toán trong ứng dụng MoMo sau khi nhấn nút bên dưới.",
                            color = SecondaryTextColor,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    } else {
                    
                    // QR Code Graphic
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
                    
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("SỐ TÀI KHOẢN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SecondaryTextColor)
                            Text("1234567890", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
                        }
                        Surface(
                            onClick = {
                                clipboardManager.setText(AnnotatedString("1234567890"))
                                Toast.makeText(context, "Đã sao chép số tài khoản!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = Color(0xFFF1F4F2)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.ContentCopy, null, tint = DarkTextColor, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    BankDetailRow(label = "CHỦ TÀI KHOẢN", value = "FLOURISH TRAVEL", icon = Icons.Default.Person)

                    Spacer(modifier = Modifier.height(32.dp))

                    // Transfer Content (Memo)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        color = LightGreenBackground.copy(alpha = 0.7f)
                    ) {
                        val transferMemo = "FT-${bookingPhone.takeLast(4)}"
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("NỘI DUNG CHUYỂN KHOẢN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SecondaryTextColor)
                                Text(transferMemo, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = PrimaryGreen)
                            }
                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(transferMemo))
                                    Toast.makeText(context, "Đã sao chép nội dung chuyển khoản!", Toast.LENGTH_SHORT).show()
                                },
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

        // Final Booking Action Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        isProcessing = true
                        apiError = null
                        
                        // Construct lists of guests
                        val guestNamesList = mutableListOf<String>()
                        val guestsList = mutableListOf<GuestItem>()
                        
                        guestNamesList.add(bookingName)
                        guestsList.add(GuestItem(fullName = bookingName, idNumber = bookingIdCard, dateOfBirth = "1995-01-01"))
                        
                        val totalGuestsCount = adultCount + childCount
                        for (i in 2..totalGuestsCount) {
                            val name = "Khách đi cùng $i"
                            guestNamesList.add(name)
                            guestsList.add(GuestItem(fullName = name, idNumber = "000000000000", dateOfBirth = "2000-01-01"))
                        }

                        val request = CreateBookingRequest(
                            sessionId = sessionId,
                            guestCount = totalGuestsCount,
                            specialRequests = bookingNote.ifBlank { null },
                            promotionCode = promoCode,
                            contactPhone = bookingPhone,
                            pickupAddress = "TP. Hồ Chí Minh, Việt Nam",
                            guestNames = guestNamesList,
                            guests = guestsList,
                            emergencyContactName = bookingName,
                            emergencyContactPhone = bookingPhone,
                            paymentMethod = if (paymentMethod == "Online Payment") "ewallet" else "bank"
                        )

                        try {
                            val response = RetrofitClient.bookingApiService.createBooking(request)
                            if (response.isSuccessful && response.body()?.success == true) {
                                val bookingData = response.body()?.data
                                if (bookingData != null) {
                                    if (isOnlinePayment) {
                                        openPaymentUrl(bookingData.bookingId, bookingData.paymentUrl)
                                    }
                                    onComplete(bookingData.bookingId, bookingData.orderId)
                                } else {
                                    apiError = "Lỗi phản hồi: Dữ liệu đặt tour trống!"
                                }
                            } else {
                                apiError = "Đặt vé thất bại: " + (response.body()?.message ?: "Lỗi máy chủ")
                            }
                        } catch (e: Exception) {
                            apiError = "Lỗi kết nối: ${e.localizedMessage}"
                        } finally {
                            isProcessing = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                enabled = !isProcessing
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text(
                        if (isOnlinePayment) "Thanh toán qua MoMo" else "Tôi đã chuyển khoản",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StepItem(number: String, label: String, isActive: Boolean = false, isCompleted: Boolean = false) {
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
private fun BankDetailRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
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


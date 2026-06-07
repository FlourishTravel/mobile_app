package com.example.flourishtavelapp.ui.screens

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*
import com.example.flourishtavelapp.data.api.RetrofitClient
import com.example.flourishtavelapp.data.model.ValidateSessionRequest
import com.example.flourishtavelapp.data.model.ValidatePromoRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingReviewScreen(
    initialAdultCount: Int,
    initialChildCount: Int,
    onBack: () -> Unit,
    onProceed: (Int, Int, String, String?, Long) -> Unit, // added sessionId, promoCode, promoDiscount
    initialTourId: String? = null,
    initialSessionId: String? = null,
    modifier: Modifier = Modifier
) {
    // Hardware back press behavior
    BackHandler {
        onBack()
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var adultCount by remember { mutableIntStateOf(initialAdultCount) }
    var childCount by remember { mutableIntStateOf(initialChildCount) }
    var selectedPaymentMethod by remember { mutableStateOf("Bank Transfer") }

    // Dynamic session and tour from server
    var sessionId by remember { mutableStateOf(initialSessionId ?: "d36e9617-bf7c-4a60-ab39-ec1f17bda5a5") } // Default fallback (Huế Tour Session)
    var tourId by remember { mutableStateOf(initialTourId ?: "234c6269-9382-42e4-ab11-ca518e4ddb4b") }     // Default fallback (Huế Tour)
    var tourTitle by remember { mutableStateOf("Vịnh Maya (Phi Phi)") }
    var adultPrice by remember { mutableIntStateOf(1000000) } // Default 1.000.000đ
    var childPrice by remember { mutableIntStateOf(450000) }  // Default 450.000đ

    // Promo states
    var promoCodeInput by remember { mutableStateOf("") }
    var promoDiscountAmount by remember { mutableDoubleStateOf(0.0) }
    var promoError by remember { mutableStateOf<String?>(null) }
    var promoSuccessMessage by remember { mutableStateOf<String?>(null) }
    var isValidatingPromo by remember { mutableStateOf(false) }

    // Validation & loading states
    var isProcessing by remember { mutableStateOf(false) }
    var apiError by remember { mutableStateOf<String?>(null) }

    // Fetch active session / tour details on open
    LaunchedEffect(tourId) {
        try {
            if (initialTourId == null) {
                val responseTours = RetrofitClient.bookingApiService.getTours()
                if (responseTours.isSuccessful) {
                    val tours = responseTours.body()?.data?.content
                    val activeTour = tours?.firstOrNull { it.earliestSession != null }
                    if (activeTour != null) {
                        sessionId = activeTour.earliestSession!!.id
                        tourId = activeTour.id
                        tourTitle = activeTour.title
                    }
                }
            }
            
            // Now fetch details for the current tourId (either user-provided or fetched) to get correct prices and title
            val response = RetrofitClient.bookingApiService.getTourDetail(tourId)
            if (response.isSuccessful) {
                val detail = response.body()?.data
                if (detail != null) {
                    tourTitle = detail.title
                    adultPrice = detail.basePrice.toInt()
                    childPrice = (detail.basePrice * 0.5).toInt()
                    if (initialSessionId == null) {
                        val session = detail.sessions?.firstOrNull()
                        if (session != null) {
                            sessionId = session.id
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val totalAmount = (adultCount.toLong() * adultPrice) + (childCount.toLong() * childPrice)
    val discountAmount = promoDiscountAmount.toLong()
    val finalAmount = if (totalAmount - discountAmount > 0) totalAmount - discountAmount else 0L

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
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkTextColor)
                }
                Text(
                    "Review & Pay",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor
                )
                IconButton(onClick = { /* Info */ }) {
                    Icon(Icons.Default.Info, contentDescription = "Info", tint = PrimaryGreen)
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // Error Bar if validation fails
                if (apiError != null) {
                    Surface(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFEE2E2),
                        border = BorderStroke(1.dp, Color(0xFFFCA5A5))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Error, null, tint = Color(0xFFDC2626), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(apiError!!, color = Color(0xFF991B1B), fontSize = 13.sp, modifier = Modifier.weight(1f))
                        }
                    }
                }

                // Selected Tour Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = com.example.flourishtavelapp.R.drawable.maya_bg),
                            contentDescription = null,
                            modifier = Modifier.size(100.dp).clip(RoundedCornerShape(24.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("SELECTED TOUR", color = PrimaryGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(tourTitle, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkTextColor)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                                Text(" 4.9", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text(" (1.2k reviews)", color = SecondaryTextColor, fontSize = 12.sp)
                            }
                            Text(
                                text = "%,dđ / person".format(adultPrice),
                                color = PrimaryGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Guest Count Adjustment (New Composable item to support guest changes)
                Text("Guests Count", fontWeight = FontWeight.Bold, color = DarkTextColor)
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("Người lớn", fontWeight = FontWeight.Bold, color = DarkTextColor, fontSize = 15.sp)
                                Text("Từ 12 tuổi trở lên", color = SecondaryTextColor, fontSize = 11.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { if (adultCount > 1) adultCount-- }) {
                                    Icon(Icons.Default.RemoveCircleOutline, null, tint = PrimaryGreen)
                                }
                                Text("$adultCount", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                IconButton(onClick = { adultCount++ }) {
                                    Icon(Icons.Default.AddCircleOutline, null, tint = PrimaryGreen)
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F4F2))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("Trẻ em", fontWeight = FontWeight.Bold, color = DarkTextColor, fontSize = 15.sp)
                                Text("Từ 2 - 11 tuổi", color = SecondaryTextColor, fontSize = 11.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { if (childCount > 0) childCount-- }) {
                                    Icon(Icons.Default.RemoveCircleOutline, null, tint = PrimaryGreen)
                                }
                                Text("$childCount", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                IconButton(onClick = { childCount++ }) {
                                    Icon(Icons.Default.AddCircleOutline, null, tint = PrimaryGreen)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Promo Code Section
                Text("Promo Code", fontWeight = FontWeight.Bold, color = DarkTextColor)
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = promoCodeInput,
                                onValueChange = { promoCodeInput = it },
                                placeholder = { Text("Mã giảm giá", fontSize = 14.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF8FAFC),
                                    unfocusedContainerColor = Color(0xFFF8FAFC),
                                    focusedBorderColor = PrimaryGreen,
                                    unfocusedBorderColor = Color(0xFFCBD5E1)
                                ),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                onClick = {
                                    if (promoCodeInput.isBlank()) return@Button
                                    coroutineScope.launch {
                                        isValidatingPromo = true
                                        promoError = null
                                        promoSuccessMessage = null
                                        try {
                                            val response = RetrofitClient.bookingApiService.validatePromo(
                                                ValidatePromoRequest(
                                                    code = promoCodeInput.trim(),
                                                    sessionId = sessionId,
                                                    guestCount = adultCount + childCount
                                                )
                                            )
                                            if (response.isSuccessful && response.body()?.success == true) {
                                                val result = response.body()?.data
                                                if (result?.valid == true) {
                                                    promoDiscountAmount = result.discountAmount ?: 0.0
                                                    promoSuccessMessage = result.message ?: "Áp dụng thành công!"
                                                    Toast.makeText(context, "Mã giảm giá hợp lệ!", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    promoError = result?.message ?: "Mã giảm giá không hợp lệ!"
                                                    promoDiscountAmount = 0.0
                                                }
                                            } else {
                                                promoError = response.body()?.message ?: "Không thể áp dụng mã!"
                                                promoDiscountAmount = 0.0
                                            }
                                        } catch (e: Exception) {
                                            promoError = "Lỗi kết nối: ${e.localizedMessage}"
                                            promoDiscountAmount = 0.0
                                        } finally {
                                            isValidatingPromo = false
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                                enabled = !isValidatingPromo && promoCodeInput.isNotBlank(),
                                modifier = Modifier.height(56.dp)
                            ) {
                                if (isValidatingPromo) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                } else {
                                    Text("Áp dụng", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        if (promoError != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(promoError!!, color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                        if (promoSuccessMessage != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(promoSuccessMessage!!, color = Color(0xFF2E7D32), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Payment Method Section
                Text("Payment Method", fontWeight = FontWeight.Bold, color = DarkTextColor)
                Spacer(modifier = Modifier.height(12.dp))
                PaymentMethodItem(title = "Bank Transfer", subtitle = "Chuyển khoản ngân hàng", icon = Icons.Default.AccountBalance, isSelected = selectedPaymentMethod == "Bank Transfer", onClick = { selectedPaymentMethod = "Bank Transfer" })
                Spacer(modifier = Modifier.height(12.dp))
                PaymentMethodItem(title = "Online Payment", subtitle = "Momo, ZaloPay, etc.", icon = Icons.Default.AccountBalanceWallet, isSelected = selectedPaymentMethod == "Online Payment", onClick = { selectedPaymentMethod = "Online Payment" })

                Spacer(modifier = Modifier.height(24.dp))

                // Payment Details
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F4F2).copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("PAYMENT DETAILS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5D4037), modifier = Modifier.align(Alignment.CenterHorizontally))
                        Spacer(modifier = Modifier.height(20.dp))
                        PaymentDetailRow("Adults (x$adultCount)", "%,dđ".format(adultCount * adultPrice))
                        PaymentDetailRow("Children (x$childCount)", "%,dđ".format(childCount * childPrice))
                        if (discountAmount > 0) {
                            PaymentDetailRow("Promo Discount", "-%,dđ".format(discountAmount))
                        }
                        PaymentDetailRow("Processing Fee", "Free", isFree = true)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp), thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Total Amount", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkTextColor)
                            Text("%,dđ".format(finalAmount), fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = DarkTextColor)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(120.dp))
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(20.dp)) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        isProcessing = true
                        apiError = null
                        try {
                            val response = RetrofitClient.bookingApiService.validateSession(
                                ValidateSessionRequest(
                                    sessionId = sessionId,
                                    guestCount = adultCount + childCount,
                                    tourId = tourId
                                )
                            )
                            if (response.isSuccessful && response.body()?.success == true) {
                                val result = response.body()?.data
                                if (result?.valid == true) {
                                    onProceed(
                                        adultCount,
                                        childCount,
                                        sessionId,
                                        if (promoDiscountAmount > 0) promoCodeInput.trim() else null,
                                        discountAmount
                                    )
                                } else {
                                    apiError = result?.message ?: "Phiên đặt tour đã đầy hoặc không khả dụng!"
                                }
                            } else {
                                apiError = response.body()?.message ?: "Lỗi xác thực phiên với server!"
                            }
                        } catch (e: Exception) {
                            apiError = "Lỗi kết nối: ${e.localizedMessage}"
                        } finally {
                            isProcessing = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                enabled = !isProcessing
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text("Tiến hành thanh toán", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = if (isSelected) BorderStroke(2.dp, PrimaryGreen) else null,
        shadowElevation = if (isSelected) 0.dp else 2.dp
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF1F4F2)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = DarkTextColor, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
                Text(subtitle, fontSize = 12.sp, color = SecondaryTextColor)
            }
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = PrimaryGreen)
            )
        }
    }
}

@Composable
private fun PaymentDetailRow(label: String, value: String, isFree: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = SecondaryTextColor, fontSize = 14.sp)
        Text(
            text = value,
            color = if (isFree) Color(0xFF00BFA5) else DarkTextColor,
            fontWeight = if (isFree) FontWeight.Bold else FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}


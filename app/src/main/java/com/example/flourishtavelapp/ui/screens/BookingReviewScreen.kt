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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingReviewScreen(
    initialAdultCount: Int,
    initialChildCount: Int,
    onBack: () -> Unit,
    onProceed: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var adultCount by remember { mutableIntStateOf(initialAdultCount) }
    var childCount by remember { mutableIntStateOf(initialChildCount) }
    var selectedPaymentMethod by remember { mutableStateOf("Bank Transfer") }

    val adultPrice = 1000000 // 1.000.000đ
    val childPrice = 450000  // 450.000đ
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
                            Text("Vịnh Maya (Phi Phi)", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkTextColor)
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
                        PaymentDetailRow("Processing Fee", "Free", isFree = true)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp), thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Total Amount", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkTextColor)
                            Text("%,dđ".format(totalAmount), fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = DarkTextColor)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(120.dp))
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(20.dp)) {
            Button(
                onClick = { onProceed(adultCount, childCount) },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Tiến hành thanh toán", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun PaymentMethodItem(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth().clickable { onClick() }, shape = RoundedCornerShape(24.dp), color = Color.White, border = if (isSelected) BorderStroke(2.dp, PrimaryGreen) else null, shadowElevation = if (isSelected) 0.dp else 2.dp) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(48.dp), shape = RoundedCornerShape(12.dp), color = Color(0xFFF1F4F2)) { Box(contentAlignment = Alignment.Center) { Icon(icon, null, tint = DarkTextColor, modifier = Modifier.size(24.dp)) } }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
                Text(subtitle, fontSize = 12.sp, color = SecondaryTextColor)
            }
            RadioButton(selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = PrimaryGreen))
        }
    }
}



@Composable
fun PaymentDetailRow(label: String, value: String, isFree: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = SecondaryTextColor, fontSize = 14.sp)
        Text(text = value, color = if (isFree) Color(0xFF00BFA5) else DarkTextColor, fontWeight = if (isFree) FontWeight.Bold else FontWeight.Medium, fontSize = 14.sp)
    }
}

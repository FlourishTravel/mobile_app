package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyTourScreen(onBack: () -> Unit) {
    var quantity by remember { mutableIntStateOf(2) }
    var selectedPayment by remember { mutableStateOf(0) }
    
    var fullName by remember { mutableStateOf("Nguyễn Văn A") }
    var email by remember { mutableStateOf("example@flourish.com") }
    var phoneNumber by remember { mutableStateOf("090 1234 567") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Mua Tour Code", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryTeal)
                        Text("Flourish", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryTeal, modifier = Modifier.padding(end = 16.dp))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryTeal)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("TỔNG CỘNG", style = MaterialTheme.typography.labelSmall, color = SecondaryTextColor)
                            Text("${String.format("%,d", quantity * 500000)}đ", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PrimaryTeal)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("HẾT HẠN SAU", style = MaterialTheme.typography.labelSmall, color = SecondaryTextColor)
                            Text("14:59", fontWeight = FontWeight.Bold, color = Color.Red)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { /* Handle payment */ },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Thanh toán ngay", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, null, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Banner
            Card(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(BannerBlue, PrimaryTeal)))
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFCCBC).copy(alpha = 0.8f)
                        ) {
                            Text("SPECIAL OFFER", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Khám phá hành trình mới cùng Flourish", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quantity Selector
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Số lượng Tour Code", fontWeight = FontWeight.Bold)
                        Text("Chọn số lượng code bạn cần mua", color = SecondaryTextColor, fontSize = 12.sp)
                    }
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = Color(0xFFF1F3F4),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 4.dp)) {
                            IconButton(onClick = { if (quantity > 1) quantity-- }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Remove, null, modifier = Modifier.size(16.dp))
                            }
                            Text(String.format("%02d", quantity), modifier = Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.Bold)
                            IconButton(onClick = { quantity++ }, modifier = Modifier.size(32.dp), colors = IconButtonDefaults.iconButtonColors(containerColor = PrimaryTeal, contentColor = Color.White)) {
                                Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Information Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, null, tint = PrimaryTeal, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Thông tin người mua", fontWeight = FontWeight.Bold, color = PrimaryTeal)
            }

            Spacer(modifier = Modifier.height(12.dp))
            BuyTourTextField(label = "HỌ VÀ TÊN", value = fullName, onValueChange = { fullName = it })
            Spacer(modifier = Modifier.height(12.dp))
            BuyTourTextField(label = "EMAIL LIÊN HỆ", value = email, onValueChange = { email = it })
            Spacer(modifier = Modifier.height(12.dp))
            BuyTourTextField(label = "SỐ ĐIỆN THOẠI", value = phoneNumber, onValueChange = { phoneNumber = it })

            Spacer(modifier = Modifier.height(24.dp))

            // Price Summary
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, PrimaryTeal.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Số lượng tour", color = SecondaryTextColor)
                        Text(String.format("%02d", quantity), fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Đơn giá", color = SecondaryTextColor)
                        Text("500.000đ", fontWeight = FontWeight.Bold)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Tổng thanh toán", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("${String.format("%,d", quantity * 500000)}đ", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryTeal)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Payment Methods
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Payments, null, tint = PrimaryTeal, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Phương thức thanh toán", fontWeight = FontWeight.Bold, color = PrimaryTeal)
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            PaymentMethodItem(0, Icons.Default.AccountBalance, "Chuyển khoản Ngân hàng", "Vietcombank", selectedPayment == 0) { selectedPayment = 0 }
            Spacer(modifier = Modifier.height(8.dp))
            PaymentMethodItem(1, Icons.Default.CreditCard, "Thẻ tín dụng / Ghi nợ", "Visa, Mastercard, JCB", selectedPayment == 1) { selectedPayment = 1 }
            Spacer(modifier = Modifier.height(8.dp))
            PaymentMethodItem(2, Icons.Default.AccountBalanceWallet, "Ví điện tử", "MoMo, ZaloPay, VNPay", selectedPayment == 2) { selectedPayment = 2 }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun BuyTourTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = SecondaryTextColor)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE0E0E0).copy(alpha = 0.5f),
                unfocusedContainerColor = Color(0xFFE0E0E0).copy(alpha = 0.5f),
                disabledContainerColor = Color(0xFFE0E0E0).copy(alpha = 0.5f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true
        )
    }
}

@Composable
fun PaymentMethodItem(index: Int, icon: ImageVector, title: String, subtitle: String, isSelected: Boolean, onSelect: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onSelect() },
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = if (isSelected) BorderStroke(1.dp, PrimaryTeal) else BorderStroke(1.dp, Color.Transparent)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF1F3F4)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = PrimaryTeal, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(subtitle, color = SecondaryTextColor, fontSize = 12.sp)
            }
            RadioButton(selected = isSelected, onClick = onSelect, colors = RadioButtonDefaults.colors(selectedColor = PrimaryTeal))
        }
    }
}

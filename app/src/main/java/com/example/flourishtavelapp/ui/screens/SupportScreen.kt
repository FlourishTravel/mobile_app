package com.example.flourishtravelapp.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.ContactRequestCreate
import com.example.flourishtravelapp.data.model.SiteContentDto
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun SupportScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    userName: String = "",
    userEmail: String = "",
    userPhone: String = ""
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var articles by remember { mutableStateOf<List<SiteContentDto>>(emptyList()) }
    var guideName by remember { mutableStateOf<String?>(null) }
    var guidePhone by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf(userName) }
    var email by remember { mutableStateOf(userEmail) }
    var phone by remember { mutableStateOf(userPhone) }
    var message by remember { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val help = RetrofitClient.contentApiService.listContent("help")
            if (help.isSuccessful) {
                articles = help.body()?.data.orEmpty()
            }
            if (articles.isEmpty()) {
                val all = RetrofitClient.contentApiService.listContent(null)
                if (all.isSuccessful) articles = all.body()?.data.orEmpty().take(8)
            }
        } catch (_: Exception) {
        }
        try {
            val bookings = RetrofitClient.bookingApiService.getMyBookings()
            val active = bookings.body()?.data.orEmpty().firstOrNull {
                val s = it.bookingStatus.lowercase()
                s == "paid" || s == "confirmed"
            }
            if (active != null) {
                val detail = RetrofitClient.bookingApiService.getBookingDetail(active.bookingId)
                val data = detail.body()?.data
                guideName = data?.guideName
                guidePhone = data?.contactPhone
            }
        } catch (_: Exception) {
        }
        isLoading = false
    }

    fun dial(number: String) {
        val cleaned = number.filter { it.isDigit() || it == '+' }
        if (cleaned.isBlank()) return
        context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleaned")))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkTextColor)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Hỗ trợ",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp)
            )
        }
        Text(
            text = "Gửi yêu cầu tới Flourish hoặc xem hướng dẫn từ hệ thống",
            color = SecondaryTextColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 48.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = PrimaryGreen)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Cần hỗ trợ khẩn?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Gửi form bên dưới — CSKH nhận trên admin. Nếu đang trong tour, gọi HDV của đoàn.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        if (!guideName.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Đoàn hiện tại", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            SupportContactCard(
                title = "HDV • $guideName",
                description = "Liên hệ hướng dẫn viên của chuyến đã thanh toán gần nhất.",
                tag = "Guide",
                tagIcon = Icons.Outlined.PersonSearch,
                phoneNumber = guidePhone.orEmpty().ifBlank { "—" },
                onCall = { if (!guidePhone.isNullOrBlank()) dial(guidePhone!!) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Gửi yêu cầu hỗ trợ", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Họ tên") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Số điện thoại") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = message, onValueChange = { message = it }, label = { Text("Nội dung") }, modifier = Modifier.fillMaxWidth().height(120.dp))
                Button(
                    onClick = {
                        if (name.isBlank() || email.isBlank() || message.isBlank()) {
                            Toast.makeText(context, "Nhập tên, email và nội dung", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        scope.launch {
                            sending = true
                            try {
                                val res = RetrofitClient.contentApiService.createContactRequest(
                                    ContactRequestCreate(
                                        name = name.trim(),
                                        email = email.trim(),
                                        phone = phone.trim().ifBlank { null },
                                        message = message.trim()
                                    )
                                )
                                if (res.isSuccessful) {
                                    Toast.makeText(context, res.body()?.message ?: "Đã gửi yêu cầu", Toast.LENGTH_SHORT).show()
                                    message = ""
                                } else {
                                    Toast.makeText(context, "Không gửi được yêu cầu", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, e.localizedMessage ?: "Lỗi kết nối", Toast.LENGTH_SHORT).show()
                            } finally {
                                sending = false
                            }
                        }
                    },
                    enabled = !sending,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text(if (sending) "Đang gửi..." else "Gửi tới Flourish", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (articles.isNotEmpty()) {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Hướng dẫn", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            articles.forEach { article ->
                ChecklistItem(
                    Icons.Outlined.Description,
                    article.title.orEmpty().ifBlank { "Bài viết" },
                    article.summary?.ifBlank { null } ?: article.body.orEmpty().take(180)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        TextButton(onClick = onLogout) {
            Text("Đăng xuất", color = PrimaryGreen)
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun SupportContactCard(
    title: String,
    description: String,
    tag: String,
    tagIcon: ImageVector,
    phoneNumber: String,
    address: String? = null,
    onCall: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, color = SecondaryTextColor, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(12.dp))
            Surface(shape = RoundedCornerShape(8.dp), color = LightGreenBackground) {
                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(tagIcon, null, tint = PrimaryGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(tag, color = PrimaryGreen, style = MaterialTheme.typography.labelSmall)
                }
            }
            if (address != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(address, color = PrimaryGreen, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onCall,
                enabled = phoneNumber != "—",
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkTextColor)
            ) {
                Text("Gọi $phoneNumber", color = Color.White)
            }
        }
    }
}

@Composable
fun ChecklistItem(icon: ImageVector, title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(32.dp).background(LightGreenBackground, CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = PrimaryGreen, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold)
                Text(description, color = SecondaryTextColor, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

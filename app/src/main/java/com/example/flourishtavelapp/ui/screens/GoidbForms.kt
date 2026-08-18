package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.*
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    isLoggedIn: Boolean = true, navId: String = "",
    onBack: () -> Unit, onRequiresLogin: () -> Unit = { }, onSuccess: (String) -> Unit = { }
) {
        var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var newPwd by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    Scaffold(topBar = { TopAppBar(title = { Text("Dat lai mat khau", fontWeight = FontWeight.Bold, color = DarkTextColor) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground)) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
    OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Ma xac nhan") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
    OutlinedTextField(value = newPwd, onValueChange = { newPwd = it }, label = { Text("Mat khau moi") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
                Spacer(Modifier.height(8.dp))
                Button(onClick = { scope.launch { isLoading = true; msg = null
                    try {
val body = mapOf<String, Any?>("email" to email, "code" to code, "newPassword" to newPwd)
                                val resp = RetrofitClient.goidbApiService.post_auth_reset_password(body = body)
                                msg = if (resp.isSuccessful) "Dat lai mat khau thanh cong" else "Loi: ${resp.code()}"
                        onSuccess(msg ?: "Thanh cong")
                    } catch (e: Exception) { msg = "Loi: ${e.message}"; e.printStackTrace() } finally { isLoading = false }
                } }, enabled = !isLoading, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005b41)), modifier = Modifier.fillMaxWidth()) { Text("Gui yeu cau", color = Color.White) }
                if (isLoading) CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.padding(top = 12.dp))
                msg?.let { Text(it, color = if (it.startsWith("Loi")) Color(0xFFB91C1C) else Color(0xFF047857), modifier = Modifier.padding(top = 8.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefundRequestScreen(
    isLoggedIn: Boolean = true, navId: String = "",
    onBack: () -> Unit, onRequiresLogin: () -> Unit = { }, onSuccess: (String) -> Unit = { }
) {
        var reason by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    Scaffold(topBar = { TopAppBar(title = { Text("Yeu cau hoan tien", fontWeight = FontWeight.Bold, color = DarkTextColor) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground)) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
    OutlinedTextField(value = reason, onValueChange = { reason = it }, label = { Text("Ly do hoan tien") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
                Spacer(Modifier.height(8.dp))
                Button(onClick = { scope.launch { isLoading = true; msg = null
                    try {
val body = mapOf<String, Any?>("reason" to reason)
                                val resp = RetrofitClient.goidbApiService.post_bookings_id_request_refund(id = navId, body = body)
                                msg = if (resp.isSuccessful) "Da gui yeu cau hoan tien" else "Loi: ${resp.code()}"
                        onSuccess(msg ?: "Thanh cong")
                    } catch (e: Exception) { msg = "Loi: ${e.message}"; e.printStackTrace() } finally { isLoading = false }
                } }, enabled = !isLoading, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005b41)), modifier = Modifier.fillMaxWidth()) { Text("Gui yeu cau", color = Color.White) }
                if (isLoading) CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.padding(top = 12.dp))
                msg?.let { Text(it, color = if (it.startsWith("Loi")) Color(0xFFB91C1C) else Color(0xFF047857), modifier = Modifier.padding(top = 8.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaitlistScreen(
    isLoggedIn: Boolean = true, navId: String = "",
    onBack: () -> Unit, onRequiresLogin: () -> Unit = { }, onSuccess: (String) -> Unit = { }
) {
        var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    Scaffold(topBar = { TopAppBar(title = { Text("Dang ky cho", fontWeight = FontWeight.Bold, color = DarkTextColor) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground)) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Ho ten") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
    OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Ghi chu") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
                Spacer(Modifier.height(8.dp))
                Button(onClick = { scope.launch { isLoading = true; msg = null
                    try {
val body = mapOf<String, Any?>("email" to email, "name" to name, "note" to note)
                                val resp = RetrofitClient.goidbApiService.post_waitlist(body = body)
                                msg = if (resp.isSuccessful) "Da dang ky vao danh sach cho" else "Loi: ${resp.code()}"
                        onSuccess(msg ?: "Thanh cong")
                    } catch (e: Exception) { msg = "Loi: ${e.message}"; e.printStackTrace() } finally { isLoading = false }
                } }, enabled = !isLoading, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005b41)), modifier = Modifier.fillMaxWidth()) { Text("Dang ky", color = Color.White) }
                if (isLoading) CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.padding(top = 12.dp))
                msg?.let { Text(it, color = if (it.startsWith("Loi")) Color(0xFFB91C1C) else Color(0xFF047857), modifier = Modifier.padding(top = 8.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    isLoggedIn: Boolean = true, navId: String = "",
    onBack: () -> Unit, onRequiresLogin: () -> Unit = { }, onSuccess: (String) -> Unit = { }
) {
        var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    Scaffold(topBar = { TopAppBar(title = { Text("Lien he / Nhan tin", fontWeight = FontWeight.Bold, color = DarkTextColor) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground)) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Ho ten") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
    OutlinedTextField(value = message, onValueChange = { message = it }, label = { Text("Noi dung") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
                Spacer(Modifier.height(8.dp))
                Button(onClick = { scope.launch { isLoading = true; msg = null
                    try {
val body = mapOf<String, Any?>("email" to email, "name" to name, "message" to message)
                                val resp = RetrofitClient.goidbApiService.post_contact_requests(body = body)
                                msg = if (resp.isSuccessful) "Da gui lien he" else "Loi: ${resp.code()}"
                        onSuccess(msg ?: "Thanh cong")
                    } catch (e: Exception) { msg = "Loi: ${e.message}"; e.printStackTrace() } finally { isLoading = false }
                } }, enabled = !isLoading, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005b41)), modifier = Modifier.fillMaxWidth()) { Text("Gui", color = Color.White) }
                if (isLoading) CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.padding(top = 12.dp))
                msg?.let { Text(it, color = if (it.startsWith("Loi")) Color(0xFFB91C1C) else Color(0xFF047857), modifier = Modifier.padding(top = 8.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsletterScreen(
    isLoggedIn: Boolean = true, navId: String = "",
    onBack: () -> Unit, onRequiresLogin: () -> Unit = { }, onSuccess: (String) -> Unit = { }
) {
        var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    Scaffold(topBar = { TopAppBar(title = { Text("Dang ky nhan ban tin", fontWeight = FontWeight.Bold, color = DarkTextColor) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground)) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
                Spacer(Modifier.height(8.dp))
                Button(onClick = { scope.launch { isLoading = true; msg = null
                    try {
val body = mapOf<String, Any?>("email" to email)
                                val resp = RetrofitClient.goidbApiService.post_contact_requests_newsletter(body = body)
                                msg = if (resp.isSuccessful) "Da dang ky nhan ban tin" else "Loi: ${resp.code()}"
                        onSuccess(msg ?: "Thanh cong")
                    } catch (e: Exception) { msg = "Loi: ${e.message}"; e.printStackTrace() } finally { isLoading = false }
                } }, enabled = !isLoading, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005b41)), modifier = Modifier.fillMaxWidth()) { Text("Dang ky", color = Color.White) }
                if (isLoading) CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.padding(top = 12.dp))
                msg?.let { Text(it, color = if (it.startsWith("Loi")) Color(0xFFB91C1C) else Color(0xFF047857), modifier = Modifier.padding(top = 8.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloraLocationScreen(
    isLoggedIn: Boolean = true, navId: String = "",
    onBack: () -> Unit, onRequiresLogin: () -> Unit = { }, onSuccess: (String) -> Unit = { }
) {
        var lat by remember { mutableStateOf("") }
    var lng by remember { mutableStateOf("") }
    var addr by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    Scaffold(topBar = { TopAppBar(title = { Text("Cap nhat vi tri Flora", fontWeight = FontWeight.Bold, color = DarkTextColor) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground)) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
    OutlinedTextField(value = lat, onValueChange = { lat = it }, label = { Text("Vi do (lat)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
    OutlinedTextField(value = lng, onValueChange = { lng = it }, label = { Text("Kinh do (lng)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
    OutlinedTextField(value = addr, onValueChange = { addr = it }, label = { Text("Dia chi") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
                Spacer(Modifier.height(8.dp))
                Button(onClick = { scope.launch { isLoading = true; msg = null
                    try {
val body = mapOf<String, Any?>("latitude" to (lat.toDoubleOrNull() as Any?), "longitude" to (lng.toDoubleOrNull() as Any?), "address" to addr)
                                val resp = RetrofitClient.goidbApiService.post_flora_bookings_bookingId_location(bookingId = navId, body = body)
                                msg = if (resp.isSuccessful) "Da cap nhat vi tri" else "Loi: ${resp.code()}"
                        onSuccess(msg ?: "Thanh cong")
                    } catch (e: Exception) { msg = "Loi: ${e.message}"; e.printStackTrace() } finally { isLoading = false }
                } }, enabled = !isLoading, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005b41)), modifier = Modifier.fillMaxWidth()) { Text("Cap nhat", color = Color.White) }
                if (isLoading) CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.padding(top = 12.dp))
                msg?.let { Text(it, color = if (it.startsWith("Loi")) Color(0xFFB91C1C) else Color(0xFF047857), modifier = Modifier.padding(top = 8.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationMatchScreen(
    isLoggedIn: Boolean = true, navId: String = "",
    onBack: () -> Unit, onRequiresLogin: () -> Unit = { }, onSuccess: (String) -> Unit = { }
) {
        var prefs by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    Scaffold(topBar = { TopAppBar(title = { Text("Goi y diem den", fontWeight = FontWeight.Bold, color = DarkTextColor) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground)) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
    OutlinedTextField(value = prefs, onValueChange = { prefs = it }, label = { Text("So thich (cach nhau dau phay)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
    OutlinedTextField(value = budget, onValueChange = { budget = it }, label = { Text("Ngan sach") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
                Spacer(Modifier.height(8.dp))
                Button(onClick = { scope.launch { isLoading = true; msg = null
                    try {
val plist = prefs.split(",").map { it.trim() }
                                val body = mapOf<String, Any?>("preferences" to plist, "budget" to budget)
                                val resp = RetrofitClient.goidbApiService.post_destinations_flora_match(body = body)
                                msg = if (resp.isSuccessful) "Da tim thay goi y phu hop" else "Loi: ${resp.code()}"
                        onSuccess(msg ?: "Thanh cong")
                    } catch (e: Exception) { msg = "Loi: ${e.message}"; e.printStackTrace() } finally { isLoading = false }
                } }, enabled = !isLoading, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005b41)), modifier = Modifier.fillMaxWidth()) { Text("Tim goi y", color = Color.White) }
                if (isLoading) CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.padding(top = 12.dp))
                msg?.let { Text(it, color = if (it.startsWith("Loi")) Color(0xFFB91C1C) else Color(0xFF047857), modifier = Modifier.padding(top = 8.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloraRecommendScreen(
    isLoggedIn: Boolean = true, navId: String = "",
    onBack: () -> Unit, onRequiresLogin: () -> Unit = { }, onSuccess: (String) -> Unit = { }
) {
        var tourId by remember { mutableStateOf("") }
    var prefs by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    Scaffold(topBar = { TopAppBar(title = { Text("Flora goi y tour", fontWeight = FontWeight.Bold, color = DarkTextColor) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground)) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
    OutlinedTextField(value = tourId, onValueChange = { tourId = it }, label = { Text("Tour ID") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
    OutlinedTextField(value = prefs, onValueChange = { prefs = it }, label = { Text("So thich (cach nhau dau phay)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
                Spacer(Modifier.height(8.dp))
                Button(onClick = { scope.launch { isLoading = true; msg = null
                    try {
val plist = prefs.split(",").map { it.trim() }
                                val body = mapOf<String, Any?>("tourId" to tourId, "preferences" to plist)
                                val resp = RetrofitClient.goidbApiService.post_catalog_flora_recommend(body = body)
                                msg = if (resp.isSuccessful) "Da nhan goi y tu Flora" else "Loi: ${resp.code()}"
                        onSuccess(msg ?: "Thanh cong")
                    } catch (e: Exception) { msg = "Loi: ${e.message}"; e.printStackTrace() } finally { isLoading = false }
                } }, enabled = !isLoading, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005b41)), modifier = Modifier.fillMaxWidth()) { Text("Goi y", color = Color.White) }
                if (isLoading) CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.padding(top = 12.dp))
                msg?.let { Text(it, color = if (it.startsWith("Loi")) Color(0xFFB91C1C) else Color(0xFF047857), modifier = Modifier.padding(top = 8.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourAvailabilityScreen(
    isLoggedIn: Boolean = true, navId: String = "",
    onBack: () -> Unit, onRequiresLogin: () -> Unit = { }, onSuccess: (String) -> Unit = { }
) {
        var destination by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var pax by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    Scaffold(topBar = { TopAppBar(title = { Text("Kiem tra lich trong", fontWeight = FontWeight.Bold, color = DarkTextColor) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground)) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
    OutlinedTextField(value = destination, onValueChange = { destination = it }, label = { Text("Diem den (slug)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
    OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Ngay (yyyy-mm-dd)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
    OutlinedTextField(value = pax, onValueChange = { pax = it }, label = { Text("So khach") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
    Spacer(Modifier.height(10.dp))
                Spacer(Modifier.height(8.dp))
                Button(onClick = { scope.launch { isLoading = true; msg = null
                    try {
val resp = RetrofitClient.goidbApiService.get_tours_availability_check(destinationSlug = destination.ifBlank { null }, date = date.ifBlank { null }, pax = pax.toIntOrNull())
                                msg = if (resp.isSuccessful) "Co lich trong: ${{resp.body()?.data}}" else "Loi: ${resp.code()}"
                        onSuccess(msg ?: "Thanh cong")
                    } catch (e: Exception) { msg = "Loi: ${e.message}"; e.printStackTrace() } finally { isLoading = false }
                } }, enabled = !isLoading, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005b41)), modifier = Modifier.fillMaxWidth()) { Text("Kiem tra", color = Color.White) }
                if (isLoading) CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.padding(top = 12.dp))
                msg?.let { Text(it, color = if (it.startsWith("Loi")) Color(0xFFB91C1C) else Color(0xFF047857), modifier = Modifier.padding(top = 8.dp)) }
            }
        }
    }
}

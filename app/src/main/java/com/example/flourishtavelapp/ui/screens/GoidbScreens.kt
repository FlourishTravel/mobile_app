package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
fun CategoriesScreen(
    isLoggedIn: Boolean = true,
    onBack: () -> Unit,
    onRequiresLogin: () -> Unit = {}
) {
    var data by remember { mutableStateOf<Any?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val resp = RetrofitClient.goidbApiService.get_categories_archived()
            if (resp.isSuccessful) data = resp.body()?.data else errorMsg = "Loi ${resp.code()}"
        } catch (e: Exception) { errorMsg = e.message; e.printStackTrace() }
        finally { isLoading = false }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Danh muc tour", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground))
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryGreen) }
        } else if (errorMsg != null) {
            Box(Modifier.fillMaxSize().padding(padding).padding(24.dp), contentAlignment = Alignment.Center) { Text(errorMsg ?: "Loi", color = Color(0xFFB91C1C)) }
        } else if (data != null) {
            LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Text(data.toString(), Modifier.padding(14.dp), fontSize = 13.sp, color = Color(0xFF334155))
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("Khong co du lieu", color = DarkTextColor) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    isLoggedIn: Boolean = true,
    onBack: () -> Unit,
    onRequiresLogin: () -> Unit = {}
) {
    var data by remember { mutableStateOf<Any?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val resp = RetrofitClient.goidbApiService.get_catalog()
            if (resp.isSuccessful) data = resp.body()?.data else errorMsg = "Loi ${resp.code()}"
        } catch (e: Exception) { errorMsg = e.message; e.printStackTrace() }
        finally { isLoading = false }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Danh muc tong hop", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground))
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryGreen) }
        } else if (errorMsg != null) {
            Box(Modifier.fillMaxSize().padding(padding).padding(24.dp), contentAlignment = Alignment.Center) { Text(errorMsg ?: "Loi", color = Color(0xFFB91C1C)) }
        } else if (data != null) {
            LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Text(data.toString(), Modifier.padding(14.dp), fontSize = 13.sp, color = Color(0xFF334155))
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("Khong co du lieu", color = DarkTextColor) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(
    isLoggedIn: Boolean = true,
    onBack: () -> Unit,
    onRequiresLogin: () -> Unit = {}
) {
    var data by remember { mutableStateOf<Any?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val resp = RetrofitClient.goidbApiService.get_content()
            if (resp.isSuccessful) data = resp.body()?.data else errorMsg = "Loi ${resp.code()}"
        } catch (e: Exception) { errorMsg = e.message; e.printStackTrace() }
        finally { isLoading = false }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Noi dung trang", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground))
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryGreen) }
        } else if (errorMsg != null) {
            Box(Modifier.fillMaxSize().padding(padding).padding(24.dp), contentAlignment = Alignment.Center) { Text(errorMsg ?: "Loi", color = Color(0xFFB91C1C)) }
        } else if (data != null) {
            LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Text(data.toString(), Modifier.padding(14.dp), fontSize = 13.sp, color = Color(0xFF334155))
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("Khong co du lieu", color = DarkTextColor) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuidesScreen(
    isLoggedIn: Boolean = true,
    onBack: () -> Unit,
    onRequiresLogin: () -> Unit = {}
) {
    var data by remember { mutableStateOf<Any?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val resp = RetrofitClient.goidbApiService.get_guides()
            if (resp.isSuccessful) data = resp.body()?.data else errorMsg = "Loi ${resp.code()}"
        } catch (e: Exception) { errorMsg = e.message; e.printStackTrace() }
        finally { isLoading = false }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Huong dan vien", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground))
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryGreen) }
        } else if (errorMsg != null) {
            Box(Modifier.fillMaxSize().padding(padding).padding(24.dp), contentAlignment = Alignment.Center) { Text(errorMsg ?: "Loi", color = Color(0xFFB91C1C)) }
        } else if (data != null) {
            LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Text(data.toString(), Modifier.padding(14.dp), fontSize = 13.sp, color = Color(0xFF334155))
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("Khong co du lieu", color = DarkTextColor) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelPreferencesScreen(
    isLoggedIn: Boolean = true,
    onBack: () -> Unit,
    onRequiresLogin: () -> Unit = {}
) {
    var data by remember { mutableStateOf<Any?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val resp = RetrofitClient.goidbApiService.get_users_me_travel_preferences()
            if (resp.isSuccessful) data = resp.body()?.data else errorMsg = "Loi ${resp.code()}"
        } catch (e: Exception) { errorMsg = e.message; e.printStackTrace() }
        finally { isLoading = false }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("So thich du lich", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground))
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryGreen) }
        } else if (errorMsg != null) {
            Box(Modifier.fillMaxSize().padding(padding).padding(24.dp), contentAlignment = Alignment.Center) { Text(errorMsg ?: "Loi", color = Color(0xFFB91C1C)) }
        } else if (data != null) {
            LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Text(data.toString(), Modifier.padding(14.dp), fontSize = 13.sp, color = Color(0xFF334155))
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("Khong co du lieu", color = DarkTextColor) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthScreen(
    isLoggedIn: Boolean = true,
    onBack: () -> Unit,
    onRequiresLogin: () -> Unit = {}
) {
    var data by remember { mutableStateOf<Any?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val resp = RetrofitClient.goidbApiService.get_health()
            if (resp.isSuccessful) data = resp.body()?.data else errorMsg = "Loi ${resp.code()}"
        } catch (e: Exception) { errorMsg = e.message; e.printStackTrace() }
        finally { isLoading = false }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Trang thai he thong", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground))
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryGreen) }
        } else if (errorMsg != null) {
            Box(Modifier.fillMaxSize().padding(padding).padding(24.dp), contentAlignment = Alignment.Center) { Text(errorMsg ?: "Loi", color = Color(0xFFB91C1C)) }
        } else if (data != null) {
            LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Text(data.toString(), Modifier.padding(14.dp), fontSize = 13.sp, color = Color(0xFF334155))
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("Khong co du lieu", color = DarkTextColor) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotConfigScreen(
    isLoggedIn: Boolean = true,
    onBack: () -> Unit,
    onRequiresLogin: () -> Unit = {}
) {
    var data by remember { mutableStateOf<Any?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val resp = RetrofitClient.goidbApiService.get_chatbot_config()
            if (resp.isSuccessful) data = resp.body()?.data else errorMsg = "Loi ${resp.code()}"
        } catch (e: Exception) { errorMsg = e.message; e.printStackTrace() }
        finally { isLoading = false }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Cau hinh chatbot", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground))
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryGreen) }
        } else if (errorMsg != null) {
            Box(Modifier.fillMaxSize().padding(padding).padding(24.dp), contentAlignment = Alignment.Center) { Text(errorMsg ?: "Loi", color = Color(0xFFB91C1C)) }
        } else if (data != null) {
            LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Text(data.toString(), Modifier.padding(14.dp), fontSize = 13.sp, color = Color(0xFF334155))
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("Khong co du lieu", color = DarkTextColor) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideMembersScreen(
    isLoggedIn: Boolean = true,
    navSessionId: String,
    onBack: () -> Unit,
    onRequiresLogin: () -> Unit = {}
) {
    var data by remember { mutableStateOf<Any?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val resp = RetrofitClient.goidbApiService.get_guide_sessions_sessionId_members(sessionId = navSessionId)
            if (resp.isSuccessful) data = resp.body()?.data else errorMsg = "Loi ${resp.code()}"
        } catch (e: Exception) { errorMsg = e.message; e.printStackTrace() }
        finally { isLoading = false }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Thanh vien tour", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground))
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryGreen) }
        } else if (errorMsg != null) {
            Box(Modifier.fillMaxSize().padding(padding).padding(24.dp), contentAlignment = Alignment.Center) { Text(errorMsg ?: "Loi", color = Color(0xFFB91C1C)) }
        } else if (data != null) {
            LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Text(data.toString(), Modifier.padding(14.dp), fontSize = 13.sp, color = Color(0xFF334155))
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("Khong co du lieu", color = DarkTextColor) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourDetailBySlugScreen(
    isLoggedIn: Boolean = true,
    navSlug: String,
    onBack: () -> Unit,
    onRequiresLogin: () -> Unit = {}
) {
    var data by remember { mutableStateOf<Any?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val resp = RetrofitClient.goidbApiService.get_tours_by_slug_slug(slug = navSlug)
            if (resp.isSuccessful) data = resp.body()?.data else errorMsg = "Loi ${resp.code()}"
        } catch (e: Exception) { errorMsg = e.message; e.printStackTrace() }
        finally { isLoading = false }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Chi tiet tour (slug)", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground))
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryGreen) }
        } else if (errorMsg != null) {
            Box(Modifier.fillMaxSize().padding(padding).padding(24.dp), contentAlignment = Alignment.Center) { Text(errorMsg ?: "Loi", color = Color(0xFFB91C1C)) }
        } else if (data != null) {
            LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Text(data.toString(), Modifier.padding(14.dp), fontSize = 13.sp, color = Color(0xFF334155))
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("Khong co du lieu", color = DarkTextColor) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimilarToursScreen(
    isLoggedIn: Boolean = true,
    navId: String,
    onBack: () -> Unit,
    onRequiresLogin: () -> Unit = {}
) {
    var data by remember { mutableStateOf<Any?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val resp = RetrofitClient.goidbApiService.get_tours_id_similar(id = navId)
            if (resp.isSuccessful) data = resp.body()?.data else errorMsg = "Loi ${resp.code()}"
        } catch (e: Exception) { errorMsg = e.message; e.printStackTrace() }
        finally { isLoading = false }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tour tuong tu", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground))
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryGreen) }
        } else if (errorMsg != null) {
            Box(Modifier.fillMaxSize().padding(padding).padding(24.dp), contentAlignment = Alignment.Center) { Text(errorMsg ?: "Loi", color = Color(0xFFB91C1C)) }
        } else if (data != null) {
            LazyColumn(modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Text(data.toString(), Modifier.padding(14.dp), fontSize = 13.sp, color = Color(0xFF334155))
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("Khong co du lieu", color = DarkTextColor) }
        }
    }
}

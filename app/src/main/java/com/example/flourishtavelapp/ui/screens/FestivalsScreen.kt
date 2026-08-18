package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.ThaiFestival
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FestivalsScreen(
    isLoggedIn: Boolean,
    onBack: () -> Unit,
    onFestivalClick: (String) -> Unit,
    onRequiresLogin: () -> Unit
) {
    var festivals by remember { mutableStateOf<List<ThaiFestival>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val response = RetrofitClient.destinationApiService.getFestivals()
            if (response.isSuccessful) festivals = response.body()?.data ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lễ hội Thái Lan", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground)
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(festivals) { f ->
                    Card(
                        Modifier.fillMaxWidth().clickable {
                            if (isLoggedIn) onFestivalClick(f.slug) else onRequiresLogin()
                        },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column {
                            AsyncImage(model = f.imageUrl, contentDescription = null, modifier = Modifier.fillMaxWidth().height(160.dp), contentScale = ContentScale.Crop)
                            Column(Modifier.padding(14.dp)) {
                                Text(f.name, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = DarkTextColor)
                                f.monthLabel?.let { Text(it, fontSize = 13.sp, color = Color(0xFF8B5CF6), fontWeight = FontWeight.Medium) }
                                f.description?.let {
                                    Spacer(Modifier.height(4.dp))
                                    Text(it, fontSize = 13.sp, color = Color(0xFF64748B), maxLines = 2, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.Promotion
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromotionsScreen(
    isLoggedIn: Boolean,
    onBack: () -> Unit,
    onRequiresLogin: () -> Unit
) {
    var promotions by remember { mutableStateOf<List<Promotion>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val response = RetrofitClient.promotionApiService.getActivePromotions()
            if (response.isSuccessful) promotions = response.body()?.data ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Uu dai va ma giam gia", fontWeight = FontWeight.Bold, color = DarkTextColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = DarkTextColor) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NatureGreenBackground)
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryGreen) }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(NatureGreenBackground).padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(promotions) { p -> PromotionCard(p) }
            }
        }
    }
}

@Composable
private fun PromotionCard(p: Promotion) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocalOffer, null, tint = Color(0xFFD97706), modifier = Modifier.size(36.dp))
            Spacer(Modifier.width(14.dp))
            Column(Modifier.fillMaxWidth()) {
                Text(p.name ?: p.code, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
                val desc = when (p.discountType) {
                    "PERCENT" -> "Giam ${p.discountValue?.toInt()}%"
                    "FIXED" -> "Giam ${p.discountValue?.toLong()} d"
                    else -> p.discountType ?: ""
                }
                Text(desc, fontSize = 13.sp, color = Color(0xFFB45309), fontWeight = FontWeight.Bold)
                Text("Ma: ${p.code}", fontSize = 12.sp, color = Color(0xFF64748B))
                p.validTo?.let {
                    val formatted = formatValidTo(it)
                    if (formatted != null) Text("Het han: $formatted", fontSize = 12.sp, color = Color(0xFF64748B))
                }
            }
        }
    }
}

private fun formatValidTo(iso: String): String? {
    return try {
        Instant.parse(iso).atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } catch (_: Exception) {
        null
    }
}

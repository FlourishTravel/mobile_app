package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*
import com.example.flourishtavelapp.ui.components.TourCard
import com.example.flourishtavelapp.ui.components.SmallCuratedCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomepageScreen(
    userName: String, 
    modifier: Modifier = Modifier, 
    onBack: () -> Unit,
    onTourClick: () -> Unit,
    onAssistantClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DarkTextColor
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Flourish",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor
                )
            }
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onProfileClick() },
                shape = CircleShape,
                color = Color.DarkGray
            ) {
                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.padding(8.dp))
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            // Welcome Section
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Chào $userName, cùng khám phá!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = DarkTextColor
                )
            )
            Text(
                text = "Hành trình tiếp theo của bạn bắt đầu từ đây.",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryTextColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Search Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                color = Color.White,
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, null, tint = DarkTextColor.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Bạn muốn đi đâu?", color = SecondaryTextColor, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI Assistant Card
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onAssistantClick() },
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    Image(
                        painter = painterResource(id = com.example.flourishtavelapp.R.drawable.ai_bg),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Added a dark gradient overlay instead of red for better readability without red tint
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.Black.copy(alpha = 0.1f), Color.Black.copy(alpha = 0.4f))
                                )
                            )
                    )
                    Row(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Bắt đầu Trợ lý Chuyến đi",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = "Hãy để AI lập kế hoạch cho chuyến đi mơ ước của bạn",
                                color = Color.White.copy(alpha = 0.9f),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Surface(
                            modifier = Modifier.size(44.dp),
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.3f)
                        ) {
                            Icon(Icons.Default.SmartToy, null, tint = Color.White, modifier = Modifier.padding(10.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Featured Tours Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Tour nổi bật",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor
                )
                Text(
                    "Xem tất cả",
                    color = Color(0xFF00796B),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onTourClick() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Horizontal Tour List
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    Box(modifier = Modifier.clickable { onTourClick() }) {
                        TourCard(
                            title = "Xứ sở chùa vàng",
                            location = "Bangkook, Thái Lan",
                            price = "$500",
                            imageRes = com.example.flourishtavelapp.R.drawable.bangkook_bg
                        )
                    }
                }
                item {
                    Box(modifier = Modifier.clickable { onTourClick() }) {
                        TourCard(
                            title = "Zen Forest",
                            location = "Ubud, Bali",
                            price = "$890",
                            imageRes = com.example.flourishtavelapp.R.drawable.venezia_bg
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Curated Section
            Text(
                "Dành riêng cho bạn",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkTextColor
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Large Curated Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable { onTourClick() },
                shape = RoundedCornerShape(24.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = com.example.flourishtavelapp.R.drawable.travel_bg),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Text("Chỗ ở bền vững", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        Text("Sang trọng và thân thiện với môi trường", color = Color.White.copy(alpha = 0.9f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Small Curated Cards
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SmallCuratedCard(
                    modifier = Modifier.weight(1f).clickable { onTourClick() },
                    icon = Icons.Default.Favorite,
                    title = "Yêu thích nhất",
                    iconColor = Color(0xFF4CAF50),
                    iconBg = Color(0xFFC8E6C9)
                )
                SmallCuratedCard(
                    modifier = Modifier.weight(1f).clickable { onTourClick() },
                    icon = Icons.Default.Diamond,
                    title = "Điểm đến tiềm năng",
                    iconColor = Color(0xFF03A9F4),
                    iconBg = Color(0xFFB3E5FC)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

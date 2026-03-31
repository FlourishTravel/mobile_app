package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier, 
    onBack: () -> Unit,
    onProfileClick: () -> Unit,
    onTourDetailClick: (String) -> Unit = {}
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
                    "Khám phá",
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
                color = PrimaryGreen
            ) {
                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.padding(8.dp))
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            // AI-Picked Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                    Image(
                        painter = painterResource(id = com.example.flourishtavelapp.R.drawable.thailan),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Black.copy(alpha = 0.2f), Color.Black.copy(alpha = 0.6f))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(24.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "AI CHỌN CHO TÂM TRẠNG CỦA BẠN",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Khám Phá Thái Lan Chill",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 32.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Dựa trên sở thích khám phá văn hóa của bạn. 92% Phù hợp.",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                        ) {
                            Text("Khám phá ngay", color = PrimaryGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search and Filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, null, tint = SecondaryTextColor)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Bạn muốn đến đâu ở Thái Lan?", color = SecondaryTextColor, fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFE0E7E2)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Tune, null, tint = DarkTextColor)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Categories
            val categories = listOf("Tất cả", "Bangkok", "Chiang Mai", "Phuket", "Đảo")
            var selectedCategory by remember { mutableStateOf("Tất cả") }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        modifier = Modifier.clickable { selectedCategory = category },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) PrimaryGreen else Color.White
                    ) {
                        Text(
                            text = category,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                            color = if (isSelected) Color.White else DarkTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Trending Zones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Bản đồ du lịch Thái Lan", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkTextColor)
                Text("Xem bản đồ", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth().height(180.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = com.example.flourishtavelapp.R.drawable.bandodulichthailan_bg),
                        contentDescription = "Bản đồ ",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Surface(
                        modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(Color.Red, CircleShape))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Bản đồ ", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Hot Now
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Whatshot, null, tint = Color.Red, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Đang hot", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkTextColor)
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    HotNowCard(
                        title = "Chợ đêm Jodd Fairs",
                        location = "Rama IX, Bangkok • 5.1k người tham gia",
                        tag = "SẦM UẤT TỐI NAY",
                        imageResId = com.example.flourishtavelapp.R.drawable.joddfairs_bg
                    )
                }
                item {
                    HotNowCard(
                        title = "Chùa Wat Pho",
                        location = " Ngoài chùa Phật Ngọc, chùa Phật Nằm cũng là một ngôi chùa nổi tiếng khác.",
                        tag = "CÒN VÀI VÉ",
                        imageResId = com.example.flourishtavelapp.R.drawable.phat_bg
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Curated For You
            Text("Địa điểm nổi tiếng Thái Lan", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkTextColor)
            Spacer(modifier = Modifier.height(16.dp))
            CuratedExploreCard(
                title = "Chùa Wat Arun",
                price = "$15",
                location = "Bangkok, Thái Lan",
                description = "Chiêm ngưỡng vẻ đẹp lộng lẫy của 'Chùa Bình Minh' bên bờ sông Chao Phraya với kiến trúc khảm sành sứ độc đáo.",
                tags = listOf("Văn hóa", "Kiến trúc", "Lịch sử"),
                imageResId = com.example.flourishtavelapp.R.drawable.awat_bg,
                onCardClick = { onTourDetailClick("wat_arun") }
            )
            Spacer(modifier = Modifier.height(24.dp))
            CuratedExploreCard(
                title = "Vịnh Maya, Phi Phi",
                price = "$45",
                location = "Krabi/Phuket",
                description = "Khám phá bãi biển thiên đường nổi tiếng thế giới với làn nước xanh ngọc bích và những vách núi đá vôi hùng vĩ.",
                tags = listOf("Biển đảo", "Thiên nhiên", "Lặn biển"),
                imageResId = com.example.flourishtavelapp.R.drawable.maya_bg,
                onCardClick = { onTourDetailClick("maya_phi_phi") }
            )
            Spacer(modifier = Modifier.height(24.dp))
            CuratedExploreCard(
                title = "Phố cổ Chiang Mai",
                price = "$25",
                location = "Chiang Mai, Thái Lan",
                description = "Tận hưởng không khí yên bình, những ngôi chùa cổ kính và trải nghiệm lớp học nấu ăn truyền thống Thái Lan.",
                tags = listOf("Ẩm thực", "Bình yên", "Thủ công"),
                imageResId = com.example.flourishtavelapp.R.drawable.chiangmai_bg
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun HotNowCard(title: String, location: String, tag: String, imageResId: Int? = null) {
    Card(
        modifier = Modifier.width(280.dp).height(110.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxSize(), 
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.DarkGray)
            ) {
                if (imageResId != null) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = tag, 
                    color = Color.Red, 
                    fontSize = 9.sp, 
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = title, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 15.sp, 
                    color = DarkTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = location, 
                    color = SecondaryTextColor, 
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

@Composable
fun CuratedExploreCard(
    title: String, 
    price: String, 
    location: String, 
    description: String, 
    tags: List<String>,
    imageResId: Int? = null,
    onCardClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onCardClick() },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                if (imageResId != null) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE0E0E0)))
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(shape = RoundedCornerShape(8.dp), color = PrimaryGreen) {
                        Text("KHÁCH CHỌN NHIỀU", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(modifier = Modifier.size(36.dp), shape = CircleShape, color = Color.White) {
                            Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.FavoriteBorder, null, modifier = Modifier.size(18.dp)) }
                        }
                        Surface(modifier = Modifier.size(36.dp), shape = CircleShape, color = Color.White) {
                            Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Share, null, modifier = Modifier.size(18.dp)) }
                        }
                    }
                }
            }
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DarkTextColor)
                    Text(price, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PrimaryGreen)
                }
                Text(location, color = SecondaryTextColor, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(description, color = SecondaryTextColor, fontSize = 14.sp, lineHeight = 20.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    tags.forEach { tag ->
                        Surface(shape = RoundedCornerShape(8.dp), color = LightGreenBackground) {
                            Text(tag, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

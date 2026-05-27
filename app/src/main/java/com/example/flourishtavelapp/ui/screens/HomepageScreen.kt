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
import androidx.compose.material.icons.automirrored.outlined.AirplaneTicket
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.R
import com.example.flourishtavelapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomepageScreen(
    userName: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onTourClick: () -> Unit,
    onAssistantClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCategoryClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Top Bar: "Khám phá" + Search + Avatar ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Khám phá",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = DarkTextColor
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = DarkTextColor
                        )
                    }
                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { onProfileClick() },
                        shape = CircleShape,
                        color = PrimaryGreen
                    ) {
                        Icon(
                            Icons.Default.Person,
                            null,
                            tint = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // ── Quick Action Icons ──
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF5F5F5)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        QuickActionItem(icon = Icons.AutoMirrored.Outlined.AirplaneTicket, label = "Visa", onClick = { onCategoryClick("Visa") })
                        QuickActionItem(icon = Icons.Outlined.DirectionsBus, label = "Transport", onClick = { onCategoryClick("Transport") })
                        QuickActionItem(icon = Icons.Outlined.CurrencyExchange, label = "Currency", onClick = { onCategoryClick("Currency") })
                        QuickActionItem(icon = Icons.Outlined.Cloud, label = "Weather", onClick = { onCategoryClick("Weather") })
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Chuyến đi nổi bật ──
            Text(
                "Chuyến đi nổi bật",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DarkTextColor,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                item {
                    FeaturedTourCard(
                        title = "Kỳ quan Bangkok",
                        subtitle = "Grand Palace & Wat Arun",
                        price = "2.490.000đ",
                        rating = "4.9",
                        badge = "Bán chạy nhất",
                        badgeColor = PrimaryGreen,
                        imageRes = R.drawable.bangkook_bg,
                        onClick = { onCategoryClick("Kỳ quan Bangkok") }
                    )
                }
                item {
                    FeaturedTourCard(
                        title = "Đảo Phi Phi",
                        subtitle = "Lặn biển & hoàng hôn",
                        price = "3.150.000đ",
                        rating = null,
                        badge = "Tour biển",
                        badgeColor = Color(0xFF2196F3),
                        imageRes = R.drawable.phiphi_bg,
                        onClick = { onCategoryClick("Đảo Phi Phi") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Khám phá theo vùng ──
            Text(
                "Khám phá theo vùng",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DarkTextColor,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Region grid: 2 columns
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RegionCard(
                        name = "Chiang Mai",
                        imageRes = R.drawable.chiangmai_bg,
                        modifier = Modifier
                            .weight(1f)
                            .height(140.dp),
                        onClick = { onCategoryClick("Chiang Mai") }
                    )
                    RegionCard(
                        name = "Phuket",
                        imageRes = R.drawable.phuket_bg,
                        modifier = Modifier
                            .weight(1f)
                            .height(140.dp),
                        onClick = { onCategoryClick("Phuket") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RegionCard(
                        name = "Bangkok",
                        imageRes = R.drawable.bangkook_bg,
                        modifier = Modifier
                            .weight(1f)
                            .height(140.dp),
                        onClick = { onCategoryClick("Bangkok") }
                    )
                    RegionCard(
                        name = "Pattaya",
                        imageRes = R.drawable.venezia_bg,
                        modifier = Modifier
                            .weight(1f)
                            .height(140.dp),
                        onClick = { onCategoryClick("Pattaya") }
                    )
                }
            }

            // Bottom spacing for FAB and nav bar
            Spacer(modifier = Modifier.height(80.dp))
        }

        // ── Floating AI Assistant Bubble ──
        FloatingActionButton(
            onClick = onAssistantClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 20.dp)
                .size(60.dp),
            shape = CircleShape,
            containerColor = Color.Transparent,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1A3C34),
                                Color(0xFF2D5A4E)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = "Trợ lý AI",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

// ── Quick Action Item ──
@Composable
private fun QuickActionItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier.size(44.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = label,
                    tint = Color(0xFF555555),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = SecondaryTextColor,
            fontWeight = FontWeight.Medium
        )
    }
}

// ── Featured Tour Card ──
@Composable
private fun FeaturedTourCard(
    title: String,
    subtitle: String,
    price: String,
    rating: String?,
    badge: String,
    badgeColor: Color,
    imageRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Badge
                Surface(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(8.dp),
                    color = badgeColor
                ) {
                    Text(
                        text = badge,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DarkTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    subtitle,
                    color = SecondaryTextColor,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        price,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    if (rating != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                rating,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = DarkTextColor
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Region Card ──
@Composable
private fun RegionCard(
    name: String,
    imageRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Gradient overlay at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.55f)
                            )
                        )
                    )
            )
            Text(
                text = name,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

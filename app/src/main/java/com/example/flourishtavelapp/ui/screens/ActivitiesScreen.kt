package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.AirplaneTicket
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

data class ActivityCategory(
    val id: String,
    val label: String,
    val icon: ImageVector
)

data class TravelActivity(
    val title: String,
    val location: String,
    val rating: String,
    val reviewCount: String,
    val bookedCount: String,
    val price: String,
    val originalPrice: String?,
    val promoText: String?,
    val badge: String?,
    val imageRes: Int,
    val categoryId: String,
    val keyword: String // Keyword to match homepage click
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesScreen(
    initialCategoryLabel: String,
    onBack: () -> Unit,
    onActivityClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Basic Category List
    val defaultCategories = remember {
        mutableStateListOf(
            ActivityCategory("discount", "HẠ GIÁ!", Icons.Outlined.LocalActivity),
            ActivityCategory("attraction", "Điểm tham quan", Icons.Outlined.Landscape),
            ActivityCategory("park", "Công viên giải trí", Icons.Outlined.Festival),
            ActivityCategory("sim", "Sim và Wifi", Icons.Outlined.Wifi),
            ActivityCategory("visa", "Visa", Icons.AutoMirrored.Outlined.AirplaneTicket),
            ActivityCategory("transport", "Transport", Icons.Outlined.DirectionsBus),
            ActivityCategory("currency", "Currency", Icons.Outlined.CurrencyExchange),
            ActivityCategory("weather", "Weather", Icons.Outlined.Cloud)
        )
    }

    // Rearrange category list so that the clicked one is at the first position
    val categories = remember(initialCategoryLabel) {
        val targetIndex = defaultCategories.indexOfFirst { it.label.equals(initialCategoryLabel, ignoreCase = true) }
        if (targetIndex != -1) {
            val newList = ArrayList(defaultCategories)
            val selectedItem = newList.removeAt(targetIndex)
            newList.add(0, selectedItem)
            newList
        } else {
            val newList = ArrayList(defaultCategories)
            if (initialCategoryLabel.isNotEmpty() && !defaultCategories.any { it.label.equals(initialCategoryLabel, ignoreCase = true) }) {
                newList.add(0, ActivityCategory("region", initialCategoryLabel, Icons.Outlined.LocationOn))
            }
            newList
        }
    }

    var selectedCategory by remember { mutableStateOf(categories.firstOrNull()?.label ?: "HẠ GIÁ!") }
    var searchQuery by remember { mutableStateOf(if (initialCategoryLabel.isNotEmpty()) initialCategoryLabel else "Singapore") }

    val rawActivities = remember {
        listOf(
            TravelActivity(
                title = "Vé vào cổng Universal Studios Singapore | Resorts World Sentosa",
                location = "Singapore",
                rating = "4.6",
                reviewCount = "5.546",
                bookedCount = "52k+",
                price = "206.163 đ",
                originalPrice = "320.000 đ",
                promoText = "Dùng FORYOU để tiết kiệm 10.308,00 đ",
                badge = "Được bán 84 lần trong 24 tiếng qua",
                imageRes = R.drawable.travel_bg,
                categoryId = "park",
                keyword = "Singapore"
            ),
            TravelActivity(
                title = "Vé tham quan Wat Arun (Chùa Bình Minh) & Trải nghiệm Cổ phục Thái Lan",
                location = "Bangkok, Thái Lan",
                rating = "4.9",
                reviewCount = "12.430",
                bookedCount = "100k+",
                price = "150.000 đ",
                originalPrice = "220.000 đ",
                promoText = "Dùng THAILANDCHILL để giảm 20.000 đ",
                badge = "Bán chạy nhất hôm nay",
                imageRes = R.drawable.awat_bg,
                categoryId = "attraction",
                keyword = "Bangkok"
            ),
            TravelActivity(
                title = "Vé vào cửa Singapore Oceanarium – Resorts World Sentosa",
                location = "Singapore",
                rating = "4.6",
                reviewCount = "1.393",
                bookedCount = "6.3k+",
                price = "57.726 đ",
                originalPrice = "90.000 đ",
                promoText = "Dùng FORYOU để tiết kiệm 2.886,00 đ",
                badge = "Được bán 10 lần trong 24 tiếng qua",
                imageRes = R.drawable.chaoriver_bg,
                categoryId = "park",
                keyword = "Singapore"
            ),
            TravelActivity(
                title = "Tour Ngày Đi Đảo Phi Phi & Vịnh Maya Bằng Tàu Siêu Tốc Cao Cấp",
                location = "Phuket/Krabi, Thái Lan",
                rating = "4.8",
                reviewCount = "8.450",
                bookedCount = "40k+",
                price = "950.000 đ",
                originalPrice = "1.250.000 đ",
                promoText = "Hủy miễn phí trong 24h",
                badge = "Lựa chọn tuyệt vời",
                imageRes = R.drawable.maya_bg,
                categoryId = "attraction",
                keyword = "Phi Phi"
            ),
            TravelActivity(
                title = "Tour Khám Phá Chiang Mai Cổ Kính & Chùa Vàng Doi Suthep",
                location = "Chiang Mai, Thái Lan",
                rating = "4.9",
                reviewCount = "5.120",
                bookedCount = "25k+",
                price = "650.000 đ",
                originalPrice = "800.000 đ",
                promoText = "Khám phá văn hoá tâm linh đặc sắc",
                badge = "Được đánh giá cao nhất",
                imageRes = R.drawable.chiangmai_bg,
                categoryId = "attraction",
                keyword = "Chiang Mai"
            ),
            TravelActivity(
                title = "Sim 4G Thái Lan Nhận Tại Sân Bay Suvarnabhumi (BKK) / Don Mueang",
                location = "Bangkok, Thái Lan",
                rating = "4.7",
                reviewCount = "32.100",
                bookedCount = "200k+",
                price = "120.000 đ",
                originalPrice = "180.000 đ",
                promoText = "Kết nối tốc độ cao không giới hạn",
                badge = "Tiện lợi & Bắt buộc có",
                imageRes = R.drawable.travel_bg,
                categoryId = "sim",
                keyword = "Sim"
            )
        )
    }

    // Sort list so that the activity matching initialCategoryLabel is at the first position
    val activities = remember(initialCategoryLabel) {
        if (initialCategoryLabel.isEmpty()) {
            rawActivities
        } else {
            val newList = ArrayList(rawActivities)
            val matchedIndex = newList.indexOfFirst {
                it.keyword.contains(initialCategoryLabel, ignoreCase = true) ||
                initialCategoryLabel.contains(it.keyword, ignoreCase = true) ||
                it.title.contains(initialCategoryLabel, ignoreCase = true) ||
                it.location.contains(initialCategoryLabel, ignoreCase = true)
            }
            if (matchedIndex != -1) {
                val matchedItem = newList.removeAt(matchedIndex)
                newList.add(0, matchedItem)
            }
            newList
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ── Top Bar: Back + "Hoạt động" + Favorite ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DarkTextColor
                    )
                }
                Text(
                    text = "Hoạt động",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DarkTextColor
                )
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = DarkTextColor
                    )
                }
            }

            // ── Search & Filter Row ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search bar
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFFF2F4F5)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Search,
                            null,
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = searchQuery,
                            color = DarkTextColor,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                // Filter button
                Surface(
                    modifier = Modifier.clickable { },
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Tune,
                            null,
                            tint = DarkTextColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Bộ lọc",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = DarkTextColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ── Horizontal Categories Row ──
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat.label
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { selectedCategory = cat.label }
                    ) {
                        Surface(
                            modifier = Modifier.size(50.dp),
                            shape = CircleShape,
                            color = if (isSelected) Color(0xFFE8F5E9) else Color(0xFFF5F5F5)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    cat.icon,
                                    null,
                                    tint = if (isSelected) PrimaryGreen else Color(0xFF555555),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = cat.label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) PrimaryGreen else SecondaryTextColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Activity List ──
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                itemsIndexed(activities) { index, act ->
                    val isFeatured = index == 0
                    ActivityListItem(
                        activity = act,
                        isFeatured = isFeatured,
                        onClick = onActivityClick
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityListItem(
    activity: TravelActivity,
    isFeatured: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .then(
                if (isFeatured) {
                    Modifier.border(2.dp, Color(0xFF1E88E5), RoundedCornerShape(12.dp))
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isFeatured) 3.dp else 1.dp)
    ) {
        Column {
            // Blue header bar for the featured element (Lựa chọn tuyệt vời)
            if (isFeatured) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF1E88E5)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.CheckCircle,
                                null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Lựa chọn tuyệt vời",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            "Đặt ngay để đảm bảo suất tham gia",
                            color = Color.White,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Card content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Image
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = activity.imageRes),
                        contentDescription = activity.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Heart icon
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(26.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.85f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.FavoriteBorder,
                                null,
                                tint = Color.Red,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Details
                Column(modifier = Modifier.weight(1f)) {
                    if (activity.badge != null) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFE3F2FD)
                        ) {
                            Text(
                                text = activity.badge,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                color = Color(0xFF1E88E5),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Text(
                        text = activity.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DarkTextColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${activity.rating} (${activity.reviewCount}) • ${activity.bookedCount} đã đặt",
                            color = SecondaryTextColor,
                            fontSize = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.FlashOn,
                            null,
                            tint = Color(0xFF1E88E5),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        if (activity.promoText != null) {
                            Text(
                                text = activity.promoText,
                                color = Color(0xFF388E3C),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Starting from",
                                color = SecondaryTextColor,
                                fontSize = 10.sp
                            )
                            Text(
                                text = activity.price,
                                color = Color(0xFFFF5722),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

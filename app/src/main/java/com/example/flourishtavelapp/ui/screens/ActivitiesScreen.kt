package com.example.flourishtravelapp.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flourishtravelapp.R
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.mapper.categoryLabelToApiParam
import com.example.flourishtravelapp.data.mapper.toTravelActivity
import com.example.flourishtravelapp.ui.theme.*

data class ActivityCategory(
    val id: String,
    val label: String,
    val icon: ImageVector
)

data class TravelActivity(
    val id: String?,
    val slug: String?,
    val title: String,
    val location: String,
    val rating: String,
    val reviewCount: String,
    val bookedCount: String,
    val price: String,
    val originalPrice: String?,
    val promoText: String?,
    val badge: String?,
    val imageUrl: String?,
    val imageRes: Int,
    val categoryId: String,
    val keyword: String,
    val showTimeLabel: String?,
    val routeLabel: String?
)

private val WEB_ACTIVITY_CATEGORIES = listOf(
    ActivityCategory("all", "Tất cả", Icons.Outlined.Apps),
    ActivityCategory("attraction", "Điểm tham quan", Icons.Outlined.Landscape),
    ActivityCategory("show", "Show & vui chơi", Icons.Outlined.TheaterComedy),
    ActivityCategory("transport", "Di chuyển", Icons.Outlined.DirectionsBus)
)

private val NON_DESTINATION_LABELS = setOf(
    "Tất cả",
    "Điểm tham quan",
    "Show & vui chơi",
    "Di chuyển",
    "Visa",
    "Transport",
    "Currency",
    "Weather",
    "HẠ GIÁ!"
)

private fun initialCategoryFromLabel(label: String): String = when {
    label.equals("Transport", ignoreCase = true) -> "Di chuyển"
    label.equals("Điểm tham quan", ignoreCase = true) -> "Điểm tham quan"
    label.contains("Show", ignoreCase = true) || label.contains("vui chơi", ignoreCase = true) -> "Show & vui chơi"
    label.equals("Di chuyển", ignoreCase = true) -> "Di chuyển"
    else -> "Tất cả"
}

private fun initialSearchFromLabel(label: String): String =
    if (label.isNotBlank() && !NON_DESTINATION_LABELS.any { it.equals(label, ignoreCase = true) }) {
        label
    } else {
        ""
    }

private fun destinationApiParam(searchQuery: String): String? {
    val trimmed = searchQuery.trim()
    if (trimmed.isBlank()) return null
    if (NON_DESTINATION_LABELS.any { it.equals(trimmed, ignoreCase = true) }) return null
    return trimmed
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesScreen(
    initialCategoryLabel: String,
    onBack: () -> Unit,
    onActivityClick: (TravelActivity) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBack()
    }

    val categories = remember { WEB_ACTIVITY_CATEGORIES }

    var selectedCategory by remember(initialCategoryLabel) {
        mutableStateOf(initialCategoryFromLabel(initialCategoryLabel))
    }
    var searchQuery by remember(initialCategoryLabel) {
        mutableStateOf(initialSearchFromLabel(initialCategoryLabel))
    }

    var rawActivities by remember { mutableStateOf<List<TravelActivity>>(emptyList()) }
    var isLoadingActivities by remember { mutableStateOf(true) }
    var activitiesError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedCategory, searchQuery) {
        isLoadingActivities = true
        activitiesError = null
        try {
            val response = RetrofitClient.catalogApiService.getTickets(
                category = categoryLabelToApiParam(selectedCategory),
                destination = destinationApiParam(searchQuery)
            )
            if (response.isSuccessful && response.body()?.success == true) {
                rawActivities = response.body()?.data.orEmpty().map { it.toTravelActivity() }
            } else {
                activitiesError = response.body()?.message ?: "Không tải được hoạt động"
                rawActivities = emptyList()
            }
        } catch (e: Exception) {
            activitiesError = e.localizedMessage ?: "Lỗi kết nối"
            rawActivities = emptyList()
        } finally {
            isLoadingActivities = false
        }
    }

    val activities = remember(initialCategoryLabel, rawActivities) {
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle(
                                color = DarkTextColor,
                                fontSize = 14.sp
                            ),
                            singleLine = true,
                            cursorBrush = SolidColor(PrimaryGreen),
                            decorationBox = { inner ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        "Tìm điểm đến…",
                                        color = SecondaryTextColor,
                                        fontSize = 14.sp
                                    )
                                }
                                inner()
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
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

            if (isLoadingActivities) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            } else if (activitiesError != null) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(activitiesError ?: "", color = Color(0xFFC62828))
                }
            } else if (activities.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Không có hoạt động phù hợp.", color = SecondaryTextColor)
                }
            } else {
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
                            onClick = { onActivityClick(act) }
                        )
                    }
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    if (!activity.imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = activity.imageUrl,
                            contentDescription = activity.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = activity.imageRes),
                            error = painterResource(id = activity.imageRes)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = activity.imageRes),
                            contentDescription = activity.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
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
                            Icons.Default.LocationOn,
                            null,
                            tint = SecondaryTextColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = activity.location,
                            color = SecondaryTextColor,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    activity.showTimeLabel?.takeIf { it.isNotBlank() }?.let { timeLabel ->
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(timeLabel, color = SecondaryTextColor, fontSize = 10.sp)
                    }

                    activity.routeLabel?.takeIf { it.isNotBlank() }?.let { route ->
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(route, color = SecondaryTextColor, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

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
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
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

package com.example.flourishtavelapp.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flourishtavelapp.R
import com.example.flourishtavelapp.data.api.RetrofitClient
import com.example.flourishtavelapp.data.model.*
import com.example.flourishtavelapp.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TourDetailScreen(
    tourId: String,
    onBack: () -> Unit,
    onBookNowClick: (String) -> Unit, // passes the selected sessionId
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBack()
    }
    
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    var tourDetail by remember { mutableStateOf<TourDetailDto?>(null) }
    var isFavorited by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    
    var selectedTab by remember { mutableIntStateOf(0) }
    var adultCount by remember { mutableIntStateOf(2) }
    var childCount by remember { mutableIntStateOf(0) }
    var selectedDateIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(tourId) {
        isLoading = true
        // Fetch details
        try {
            val response = RetrofitClient.bookingApiService.getTourDetail(tourId)
            if (response.isSuccessful) {
                tourDetail = response.body()?.data
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Fetch favorites to see if this tour is favorited
        try {
            val favsResponse = RetrofitClient.favoriteApiService.getFavorites()
            if (favsResponse.isSuccessful) {
                val favorites = favsResponse.body()?.data
                isFavorited = favorites?.any { it.id == tourId } == true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    // Pricing calculation
    val adultPrice = tourDetail?.basePrice?.toLong() ?: 2500000L
    val childPrice = (adultPrice * 0.5).toLong() // 50% discount for children
    val discount = 50000L // default promo code discount
    val totalPrice = (adultCount * adultPrice) + (childCount * childPrice) - discount
    val displayPrice = if (totalPrice > 0) totalPrice else 0L

    val tabList = listOf(
        "Các lựa chọn gói",
        "Thông tin chuyến đi",
        "Thông tin thêm",
        "Chính sách hủy",
        "Đánh giá"
    )

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
    } else if (tourDetail == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Không thể tải thông tin tour.", color = SecondaryTextColor)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)) {
                    Text("Quay lại", color = Color.White)
                }
            }
        }
    } else {
        val detail = tourDetail!!
        val headerImageUrl = detail.images?.firstOrNull()?.imageUrl ?: detail.thumbnailUrl

        Box(modifier = modifier.fillMaxSize().background(Color.White)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
            ) {
                // ── Hero / Header Image ──
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        if (!headerImageUrl.isNullOrEmpty()) {
                            AsyncImage(
                                model = headerImageUrl,
                                contentDescription = detail.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.awat_bg),
                                error = painterResource(id = R.drawable.awat_bg)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.awat_bg),
                                contentDescription = detail.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.3f),
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.5f)
                                        )
                                    )
                                )
                        )

                        // Header buttons over image
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Back Button
                            Surface(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable { onBack() },
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.45f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            // Share / Action button
                            Surface(
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.45f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        // Tour Title Overlay at bottom of image
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PrimaryGreen,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Text(
                                    text = detail.badge ?: "Khám phá",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = detail.title,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // ── Sticky Tab Bar ──
                item {
                    ScrollableTabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.White,
                        contentColor = PrimaryGreen,
                        edgePadding = 16.dp,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = PrimaryGreen,
                                height = 3.dp
                            )
                        },
                        divider = {
                            HorizontalDivider(color = Color(0xFFEEEEEE))
                        }
                    ) {
                        tabList.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = {
                                    Text(
                                        text = title,
                                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 14.sp
                                    )
                                },
                                selectedContentColor = PrimaryGreen,
                                unselectedContentColor = SecondaryTextColor
                            )
                        }
                    }
                }

                // ── Tab Content ──
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        when (selectedTab) {
                            0 -> {
                                // ── TAB 0: CÁC LỰA CHỌN GÓI ──
                                Text(
                                    "Các lựa chọn gói",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkTextColor
                                )
                                Spacer(modifier = Modifier.height(14.dp))

                                // Date selectors row (sessions)
                                if (detail.sessions.isNullOrEmpty()) {
                                    Text("Hiện không có lịch khởi hành cho tour này.", color = SecondaryTextColor, fontSize = 14.sp)
                                } else {
                                    Text("Chọn ngày khởi hành:", color = SecondaryTextColor, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        detail.sessions!!.take(3).forEachIndexed { idx, session ->
                                            val (month, day, weekday) = parseDateToParts(session.startDate)
                                            val label = if (idx == 0) "Sớm nhất" else null
                                            DateItem(
                                                month = month, day = day, weekday = weekday, label = label,
                                                isSelected = selectedDateIndex == idx,
                                                modifier = Modifier.weight(1f),
                                                onClick = { selectedDateIndex = idx }
                                            )
                                        }
                                        // Calendar option
                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(76.dp)
                                                .clickable { },
                                            shape = RoundedCornerShape(12.dp),
                                            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                                            color = Color.White
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center,
                                                modifier = Modifier.padding(4.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.CalendarToday,
                                                    null,
                                                    tint = SecondaryTextColor,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    "Thêm ngày",
                                                    fontSize = 10.sp,
                                                    color = SecondaryTextColor,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // Guest counters
                                Text(
                                    "Số lượng hành khách",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = DarkTextColor
                                )
                                Spacer(modifier = Modifier.height(14.dp))

                                GuestCounterRow(
                                    type = "Người lớn",
                                    ageRange = "Từ 12 tuổi trở lên",
                                    priceLabel = String.format("%,dđ / người", adultPrice),
                                    count = adultCount,
                                    onIncrement = { adultCount++ },
                                    onDecrement = { if (adultCount > 1) adultCount-- }
                                )

                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(color = Color(0xFFF0F0F0))
                                Spacer(modifier = Modifier.height(16.dp))

                                GuestCounterRow(
                                    type = "Trẻ em",
                                    ageRange = "Từ 2 đến 11 tuổi",
                                    priceLabel = String.format("%,dđ / người", childPrice),
                                    count = childCount,
                                    onIncrement = { childCount++ },
                                    onDecrement = { if (childCount > 0) childCount-- }
                                )
                            }

                            1 -> {
                                // ── TAB 1: THÔNG TIN CHUYẾN ĐI ──
                                Text(
                                    detail.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = DarkTextColor
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        null,
                                        tint = PrimaryGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        detail.destinationCity ?: "Thái Lan",
                                        color = SecondaryTextColor,
                                        fontSize = 13.sp
                                    )
                                }
                                Text(
                                    String.format("%,.0fđ / người", detail.basePrice),
                                    color = PrimaryGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                Text(
                                    detail.description ?: "Trải nghiệm trọn vẹn vẻ đẹp Thái Lan cùng FlourishTravel.",
                                    color = SecondaryTextColor,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )

                                if (!detail.highlights.isNullOrEmpty()) {
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        "Điểm nổi bật",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = DarkTextColor
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    detail.highlights!!.forEach { highlight ->
                                        HighlightBulletItem(highlight)
                                    }
                                }

                                if (!detail.itineraries.isNullOrEmpty()) {
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        "Lịch trình",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = DarkTextColor
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                    detail.itineraries!!.sortedBy { it.dayNumber }.forEachIndexed { idx, itinerary ->
                                        ItineraryTimelineItem(
                                            day = itinerary.dayNumber,
                                            title = itinerary.title,
                                            desc = itinerary.description ?: itinerary.summary ?: "",
                                            isLast = idx == detail.itineraries!!.size - 1
                                        )
                                    }
                                }

                                if (!detail.includes.isNullOrEmpty()) {
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        "Dịch vụ bao gồm",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = DarkTextColor
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    detail.includes!!.forEach { inc ->
                                        val icon = when {
                                            inc.lowercase().contains("bay") || inc.lowercase().contains("vé máy") -> Icons.Default.Flight
                                            inc.lowercase().contains("khách sạn") || inc.lowercase().contains("ở") -> Icons.Default.Hotel
                                            inc.lowercase().contains("ăn") || inc.lowercase().contains("uống") -> Icons.Default.Restaurant
                                            inc.lowercase().contains("hdv") || inc.lowercase().contains("hướng dẫn") -> Icons.Default.SupportAgent
                                            else -> Icons.Default.Security
                                        }
                                        InclusionItem(icon, inc)
                                    }
                                }
                            }

                            2 -> {
                                // ── TAB 2: THÔNG TIN THÊM ──
                                Text(
                                    "Thông tin cần biết",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = DarkTextColor
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    "• Quý khách vui lòng mang theo trang phục lịch sự khi ghé thăm các ngôi đền chùa tâm linh tại Thái Lan (áo có tay, quần dài hoặc váy dài quá đầu gối).\n\n" +
                                            "• Nên chuẩn bị sẵn kem chống nắng, mũ nón và kính râm cho các hoạt động tham quan ngoài trời cũng như tắm biển tại Phuket.\n\n" +
                                            "• Tiền tệ khuyên dùng: Baht Thái (THB). Bạn nên đổi trước một ít tiền mặt tại Việt Nam hoặc mang theo thẻ tín dụng quốc tế Visa/Mastercard.",
                                    color = SecondaryTextColor,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                            }

                            3 -> {
                                // ── TAB 3: CHÍNH SÁCH HỦY ──
                                Text(
                                    "Chính sách hủy đặt tour",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = DarkTextColor
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    "• Hủy miễn phí tối đa 48 giờ trước khi lịch trình diễn ra: Hoàn tiền 100% số tiền đã đặt.\n\n" +
                                            "• Hủy từ 24 - 48 giờ trước giờ đi: Hoàn tiền 50% tổng chi phí.\n\n" +
                                            "• Hủy dưới 24 giờ hoặc không có mặt (No-show): Không hoàn tiền trong bất kỳ trường hợp nào.",
                                    color = SecondaryTextColor,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                            }

                            4 -> {
                                // ── TAB 4: ĐÁNH GIÁ (REVIEWS) ──
                                Text(
                                    "Đánh giá từ khách hàng",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = DarkTextColor
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                if (detail.reviews.isNullOrEmpty()) {
                                    Text("Chưa có đánh giá nào cho tour này.", color = SecondaryTextColor, fontSize = 14.sp)
                                } else {
                                    detail.reviews!!.forEach { review ->
                                        FAQItem(
                                            q = "${review.authorName} • ★ ${review.rating}",
                                            a = review.comment ?: "Đánh giá tốt."
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ── Pinned Bottom Bar ──
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                color = Color.White,
                shadowElevation = 16.dp
            ) {
                Column {
                    // FLOURISH promo text
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE8F5E9))
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.CardGiftcard,
                                null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Dùng FLOURISH để tiết kiệm 50.000 đ",
                                color = PrimaryGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Tổng cộng",
                                color = SecondaryTextColor,
                                fontSize = 12.sp
                            )
                            Text(
                                text = String.format("%,dđ", displayPrice),
                                color = Color(0xFFFF5722),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Heart (Favorite) button
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        try {
                                            if (isFavorited) {
                                                val res = RetrofitClient.favoriteApiService.removeFavorite(tourId)
                                                if (res.isSuccessful) {
                                                    isFavorited = false
                                                    Toast.makeText(context, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show()
                                                }
                                            } else {
                                                val res = RetrofitClient.favoriteApiService.addFavorite(FavoriteRequest(tourId))
                                                if (res.isSuccessful) {
                                                    isFavorited = true
                                                    Toast.makeText(context, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            Toast.makeText(context, "Lỗi kết nối favorites", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            ) {
                                Surface(
                                    modifier = Modifier.size(48.dp),
                                    shape = CircleShape,
                                    border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                                    color = Color.White
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                                            null,
                                            tint = if (isFavorited) Color.Red else DarkTextColor,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }

                            // Next step button
                            Button(
                                onClick = {
                                    val session = detail.sessions?.getOrNull(selectedDateIndex)
                                    if (session != null) {
                                        onBookNowClick(session.id)
                                    } else {
                                        Toast.makeText(context, "Vui lòng chọn ngày khởi hành hợp lệ", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .width(180.dp)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryGreen
                                )
                            ) {
                                Text(
                                    "Bước tiếp theo",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Date Selector Item ──
@Composable
fun DateItem(
    month: String,
    day: String,
    weekday: String,
    label: String?,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(76.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.5.dp,
            color = if (isSelected) PrimaryGreen else Color(0xFFE0E0E0)
        ),
        color = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                month,
                fontSize = 9.sp,
                color = SecondaryTextColor
            )
            Text(
                day,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) PrimaryGreen else DarkTextColor
            )
            Text(
                weekday,
                fontSize = 10.sp,
                color = SecondaryTextColor
            )
            if (label != null) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (isSelected) PrimaryGreen else Color(0xFFEEEEEE),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        label,
                        fontSize = 8.sp,
                        color = if (isSelected) Color.White else SecondaryTextColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
        }
    }
}

// ── Guest Counter Row ──
@Composable
fun GuestCounterRow(
    type: String,
    ageRange: String,
    priceLabel: String,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                type,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = DarkTextColor
            )
            Text(
                ageRange,
                color = SecondaryTextColor,
                fontSize = 11.sp
            )
            Text(
                priceLabel,
                color = PrimaryGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Decrement
            IconButton(
                onClick = onDecrement,
                modifier = Modifier
                    .size(32.dp)
                    .border(1.dp, Color(0xFFE0E0E0), CircleShape)
            ) {
                Icon(
                    Icons.Default.Remove,
                    null,
                    tint = SecondaryTextColor,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                count.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = DarkTextColor
            )

            // Increment
            IconButton(
                onClick = onIncrement,
                modifier = Modifier
                    .size(32.dp)
                    .border(1.dp, Color(0xFFE0E0E0), CircleShape)
            ) {
                Icon(
                    Icons.Default.Add,
                    null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// ── Highlight Bullet Item ──
@Composable
fun HighlightBulletItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(PrimaryGreen, CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text,
            color = SecondaryTextColor,
            fontSize = 14.sp
        )
    }
}

// ── Itinerary Timeline Item ──
@Composable
fun ItineraryTimelineItem(day: Int, title: String, desc: String, isLast: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Left timeline column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp)
        ) {
            Surface(
                modifier = Modifier.size(24.dp),
                shape = CircleShape,
                color = PrimaryGreen
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        day.toString(),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(LightGreenBackground)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Right details column
        Column(modifier = Modifier.padding(bottom = 20.dp)) {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = DarkTextColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                desc,
                color = SecondaryTextColor,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

// ── Inclusion / Exclusion Item ──
@Composable
fun InclusionItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Icon(
            icon,
            null,
            tint = PrimaryGreen,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text,
            color = SecondaryTextColor,
            fontSize = 14.sp
        )
    }
}

// ── Review or FAQ Item ──
@Composable
fun FAQItem(q: String, a: String) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    q,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DarkTextColor,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    null,
                    tint = SecondaryTextColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    a,
                    color = SecondaryTextColor,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

private fun parseDateToParts(dateStr: String): Triple<String, String, String> {
    try {
        val parser = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val date = parser.parse(dateStr)
        if (date != null) {
            val monthFormatter = java.text.SimpleDateFormat("tháng M", java.util.Locale.getDefault())
            val dayFormatter = java.text.SimpleDateFormat("dd", java.util.Locale.getDefault())
            val weekdayFormatter = java.text.SimpleDateFormat("EEE", java.util.Locale.getDefault())
            val rawWd = weekdayFormatter.format(date).uppercase()
            // Map common weekdays to Vietnamese abbreviations
            val wd = when {
                rawWd.contains("MON") || rawWd.contains("HAI") -> "T2"
                rawWd.contains("TUE") || rawWd.contains("BA") -> "T3"
                rawWd.contains("WED") || rawWd.contains("TƯ") -> "T4"
                rawWd.contains("THU") || rawWd.contains("NĂM") -> "T5"
                rawWd.contains("FRI") || rawWd.contains("SÁU") -> "T6"
                rawWd.contains("SAT") || rawWd.contains("BẢY") -> "T7"
                rawWd.contains("SUN") || rawWd.contains("NHẬT") -> "CN"
                else -> rawWd
            }
            return Triple(monthFormatter.format(date), dayFormatter.format(date), wd)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return Triple("tháng 5", "28", "T5")
}

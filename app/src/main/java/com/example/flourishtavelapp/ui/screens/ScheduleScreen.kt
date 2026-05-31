package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.flourishtavelapp.ui.theme.*

// Mock model for our tours
data class TourItem(
    val id: String,
    val title: String,
    val rating: Double,
    val reviewCount: Int,
    val bookingCountText: String,
    val location: String,
    val originalPrice: String,
    val currentPrice: String,
    val imageRes: Int,
    val tags: List<String>,
    val isSaved: Boolean,
    val isBooked: Boolean,
    val bookingDate: String? = null
)

// Detailed itinerary models
enum class ActivityStatus {
    Completed, Current, Upcoming
}

data class ActivityItem(
    val id: String,
    val title: String,
    val status: ActivityStatus,
    val description: String,
    val imageRes: Int
)

data class DayPlan(
    val dayNumber: Int,
    val dayTitle: String,
    val daySubtitle: String,
    val activities: List<ActivityItem>
)

// --- Mock Itineraries ---
val mockThailandItinerary = listOf(
    DayPlan(
        dayNumber = 1,
        dayTitle = "Ngày 1: Bangkok",
        daySubtitle = "Chào mừng bạn đến với thủ đô rực rỡ.",
        activities = listOf(
            ActivityItem(
                id = "t1_act1",
                title = "Nhận phòng khách sạn 5* tại trung tâm",
                status = ActivityStatus.Completed,
                description = "Trải nghiệm không gian sang trọng và tiện nghi bậc nhất ngay tại trung tâm thủ đô Bangkok, giúp quý khách thư giãn sau chuyến bay dài.",
                imageRes = com.example.flourishtavelapp.R.drawable.travel_bg
            ),
            ActivityItem(
                id = "t1_act2",
                title = "Dạo thuyền trên sông Chao Phraya",
                status = ActivityStatus.Completed,
                description = "Dòng sông huyền thoại chảy qua lòng Bangkok, nơi bạn có thể ngắm nhìn các ngôi chùa cổ kính và nhịp sống sôi động hai bên bờ khi hoàng hôn buông xuống.",
                imageRes = com.example.flourishtavelapp.R.drawable.chaoriver_bg
            ),
            ActivityItem(
                id = "t1_act3",
                title = "Thưởng thức buffet tối phong cách hoàng gia",
                status = ActivityStatus.Current,
                description = "Thưởng thức tinh hoa ẩm thực Thái Lan với hàng trăm món ăn truyền thống và quốc tế được chế biến bởi các đầu bếp hàng đầu trong không gian cung đình sang trọng.",
                imageRes = com.example.flourishtavelapp.R.drawable.joddfairs_bg
            )
        )
    ),
    DayPlan(
        dayNumber = 2,
        dayTitle = "Ngày 2: Di sản",
        daySubtitle = "Khám phá chiều sâu lịch sử chùa tháp.",
        activities = listOf(
            ActivityItem(
                id = "t1_act4",
                title = "Tham quan Chùa Cung Điện Hoàng Gia Wat Phra Kaew",
                status = ActivityStatus.Upcoming,
                description = "Wat Phra Kaew là ngôi chùa nổi tiếng và linh thiêng nhất tại Thái Lan, nơi lưu giữ bức tượng Phật Ngọc quý giá của vương triều.",
                imageRes = com.example.flourishtavelapp.R.drawable.awat_bg
            ),
            ActivityItem(
                id = "t1_act5",
                title = "Khám phá Wat Arun - Chùa Bình Minh",
                status = ActivityStatus.Upcoming,
                description = "Ngôi chùa cổ kính và lộng lẫy nằm bên bờ sông Chao Phraya, biểu tượng tâm linh và kiến trúc độc đáo bậc nhất của thủ đô Bangkok.",
                imageRes = com.example.flourishtavelapp.R.drawable.awat_bg
            )
        )
    ),
    DayPlan(
        dayNumber = 3,
        dayTitle = "Ngày 3: Chiang Mai",
        daySubtitle = "Vùng đất bình yên ẩm mình giữa rừng núi.",
        activities = listOf(
            ActivityItem(
                id = "t1_act6",
                title = "Ghé thăm Chùa Wat Phra That Doi Suthep",
                status = ActivityStatus.Upcoming,
                description = "Ngôi chùa thiêng liêng nằm trên đỉnh núi Doi Suthep, từ đây bạn có thể phóng tầm mắt ngắm toàn cảnh thung lũng Chiang Mai mộng mơ.",
                imageRes = com.example.flourishtavelapp.R.drawable.chiangmai_bg
            ),
            ActivityItem(
                id = "t1_act7",
                title = "Trải nghiệm khu làng voi thân thiện nhân đạo",
                status = ActivityStatus.Upcoming,
                description = "Tìm hiểu đời sống loài voi tại trung tâm bảo tồn, cùng tắm voi và làm thức ăn cho voi một cách nhân văn, bền vững.",
                imageRes = com.example.flourishtavelapp.R.drawable.travel_bg
            )
        )
    ),
    DayPlan(
        dayNumber = 4,
        dayTitle = "Ngày 4: Thiên nhiên",
        daySubtitle = "Hòa mình cùng biển cả xanh ngắt nhiệt đới.",
        activities = listOf(
            ActivityItem(
                id = "t1_act8",
                title = "Tắm biển và lặn ngắm san hô tại đảo Phi Phi",
                status = ActivityStatus.Upcoming,
                description = "Hòa mình vào làn nước trong vắt nhìn thấy đáy, khám phá các rặng san hô đa sắc màu cùng hàng ngàn loài sinh vật biển kỳ thú.",
                imageRes = com.example.flourishtavelapp.R.drawable.phiphi_bg
            )
        )
    )
)

val mockPhiPhiItinerary = listOf(
    DayPlan(
        dayNumber = 1,
        dayTitle = "Ngày 1: Khởi hành & Di chuyển đến đảo",
        daySubtitle = "Bắt đầu hành trình đến thiên đường biển cả.",
        activities = listOf(
            ActivityItem(
                id = "t2_act1",
                title = "Đón khách tại khách sạn ở Phuket ra bến tàu",
                status = ActivityStatus.Completed,
                description = "Xe du lịch chất lượng cao đón quý khách tại khách sạn trong khu vực Patong/Karon ra bến cảng khởi hành.",
                imageRes = com.example.flourishtavelapp.R.drawable.phuket_bg
            ),
            ActivityItem(
                id = "t2_act2",
                title = "Lên tàu cao tốc Speedboat di chuyển ra quần đảo Phi Phi",
                status = ActivityStatus.Completed,
                description = "Trải nghiệm tàu cao tốc cực nhanh lướt sóng qua vịnh biển xanh ngắt, đón những luồng gió biển sảng khoái.",
                imageRes = com.example.flourishtavelapp.R.drawable.phiphi_bg
            )
        )
    ),
    DayPlan(
        dayNumber = 2,
        dayTitle = "Ngày 2: Lặn ngắm san hô & Trải nghiệm vịnh Maya",
        daySubtitle = "Khám phá thế giới đại dương rực rỡ sắc màu.",
        activities = listOf(
            ActivityItem(
                id = "t2_act3",
                title = "Lặn biển snorkeling tại vịnh Loh Samah",
                status = ActivityStatus.Current,
                description = "Đắm mình vào làn nước ấm áp, trang bị kính lặn và ống thở để ngắm nhìn trực tiếp các rặng san hô tự nhiên cùng đàn cá hề bơi lượn.",
                imageRes = com.example.flourishtavelapp.R.drawable.phiphi_bg
            ),
            ActivityItem(
                id = "t2_act4",
                title = "Thăm vịnh Maya - Thiên đường ẩn giấu",
                status = ActivityStatus.Upcoming,
                description = "Bãi biển nổi tiếng thế giới với những vách núi đá vôi dựng đứng bao bọc bãi cát trắng mịn màng như bột phấn.",
                imageRes = com.example.flourishtavelapp.R.drawable.maya_bg
            ),
            ActivityItem(
                id = "t2_act5",
                title = "Ăn trưa buffet hải sản trên đảo Phi Phi Don",
                status = ActivityStatus.Upcoming,
                description = "Thưởng thức tiệc buffet thịnh soạn tại nhà hàng sát biển với các món hải sản nướng tươi sống, đồ uống mát lạnh.",
                imageRes = com.example.flourishtavelapp.R.drawable.joddfairs_bg
            )
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier, 
    onBack: () -> Unit,
    onProfileClick: () -> Unit
) {
    // Current state for which booked tour is selected to view details
    var selectedBookedTourForDetail by remember { mutableStateOf<TourItem?>(null) }

    if (selectedBookedTourForDetail != null) {
        // Detailed Day-by-Day Accordion Itinerary View matching TourDetailScreen layout
        DetailedItineraryScreen(
            tour = selectedBookedTourForDetail!!,
            onBack = { selectedBookedTourForDetail = null }
        )
    } else {
        // Main list screen (original redesign code)
        TripsMainListScreen(
            modifier = modifier,
            onBack = onBack,
            onProfileClick = onProfileClick,
            onViewItinerary = { tour -> selectedBookedTourForDetail = tour }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsMainListScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onProfileClick: () -> Unit,
    onViewItinerary: (TourItem) -> Unit
) {
    // Local list state to allow dynamic saving/unsaving interactions
    var tourListState by remember {
        mutableStateOf(
            listOf(
                TourItem(
                    id = "1",
                    title = "Vé Suối Nước Nóng Minera Bình Châu",
                    rating = 4.5,
                    reviewCount = 2,
                    bookingCountText = "80+ người đã đặt",
                    location = "Hồ Chí Minh",
                    originalPrice = "120.063,00 đ",
                    currentPrice = "106.496,00 đ",
                    imageRes = com.example.flourishtavelapp.R.drawable.travel_bg,
                    tags = listOf("Không hoàn tiền", "Xác nhận ngay lập tức"),
                    isSaved = true,
                    isBooked = false
                ),
                TourItem(
                    id = "2",
                    title = "Tour Ngắm Hoàng Hôn Trên Sông Chao Phraya",
                    rating = 4.8,
                    reviewCount = 12,
                    bookingCountText = "500+ người đã đặt",
                    location = "Bangkok",
                    originalPrice = "450.000,00 đ",
                    currentPrice = "399.000,00 đ",
                    imageRes = com.example.flourishtavelapp.R.drawable.chaoriver_bg,
                    tags = listOf("Hủy miễn phí", "Xác nhận ngay lập tức"),
                    isSaved = true,
                    isBooked = false
                ),
                TourItem(
                    id = "3",
                    title = "Vé Vào Cổng Đền Wat Arun Bangkok",
                    rating = 4.7,
                    reviewCount = 45,
                    bookingCountText = "1K+ người đã đặt",
                    location = "Bangkok",
                    originalPrice = "150.000,00 đ",
                    currentPrice = "120.000,00 đ",
                    imageRes = com.example.flourishtavelapp.R.drawable.awat_bg,
                    tags = listOf("Không hoàn tiền", "Vé điện tử"),
                    isSaved = true,
                    isBooked = false
                ),
                TourItem(
                    id = "4",
                    title = "Tour Trọn Gói Khám Phá Thái Lan 5N4Đ",
                    rating = 4.9,
                    reviewCount = 156,
                    bookingCountText = "Đang diễn ra",
                    location = "Bangkok",
                    originalPrice = "5.200.000,00 đ",
                    currentPrice = "4.500.000,00 đ",
                    imageRes = com.example.flourishtavelapp.R.drawable.thailan,
                    tags = listOf("Đã xác nhận", "HDV Tiếng Việt"),
                    isSaved = false,
                    isBooked = true,
                    bookingDate = "Khởi hành: 28/05/2026"
                ),
                TourItem(
                    id = "5",
                    title = "Vé Lặn Biển Ngắm San Hô Tại Đảo Phi Phi",
                    rating = 4.6,
                    reviewCount = 32,
                    bookingCountText = "Đã hoàn thành",
                    location = "Phuket",
                    originalPrice = "1.800.000,00 đ",
                    currentPrice = "1.650.000,00 đ",
                    imageRes = com.example.flourishtavelapp.R.drawable.phiphi_bg,
                    tags = listOf("Đã hoàn thành", "Bao gồm ăn trưa"),
                    isSaved = false,
                    isBooked = true,
                    bookingDate = "Khởi hành: 25/05/2026"
                )
            )
        )
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Tour của bạn, 1: Đã lưu
    var selectedCityFilter by remember { mutableStateOf("Mọi thành phố") }

    // Toggle save status
    val onToggleSave: (String) -> Unit = { id ->
        tourListState = tourListState.map { tour ->
            if (tour.id == id) {
                tour.copy(isSaved = !tour.isSaved)
            } else {
                tour
            }
        }
    }

    // Filtered lists based on active tab
    val toursForTab = if (selectedTabIndex == 0) {
        tourListState.filter { it.isBooked }
    } else {
        tourListState.filter { it.isSaved }
    }

    // Filtered lists based on city chip
    val filteredTours = if (selectedCityFilter == "Mọi thành phố") {
        toursForTab
    } else {
        toursForTab.filter { it.location == selectedCityFilter }
    }

    // Dynamically calculate counts for tabs
    val bookedCount = tourListState.count { it.isBooked }
    val savedCount = tourListState.count { it.isSaved }

    // List of cities based on active tab tours for chips
    val availableCities = remember(toursForTab) {
        listOf("Mọi thành phố") + toursForTab.map { it.location }.distinct()
    }

    // Ensure selected filter is reset if it's no longer available in the tab's cities
    LaunchedEffect(selectedTabIndex) {
        selectedCityFilter = "Mọi thành phố"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)) // Clean off-white background
    ) {
        // --- Top Custom Header matching reference image ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 0.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.size(40.dp)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }

                    // Dynamically centered title based on tab selection
                    Text(
                        text = if (selectedTabIndex == 0) "Tour của bạn" else "Đã lưu",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )

                    // Profile Icon
                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { onProfileClick() },
                        shape = CircleShape,
                        color = Color(0xFFE8F5E9)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                // --- Custom TabRow matching reference photo ---
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.White,
                    contentColor = Color(0xFF1976D2), // Royal blue matching reference
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = Color(0xFF1976D2),
                            height = 3.dp
                        )
                    },
                    divider = {
                        HorizontalDivider(color = Color(0xFFEBEBEB), thickness = 1.dp)
                    }
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = {
                            Text(
                                text = "Tour của bạn ($bookedCount)",
                                fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = if (selectedTabIndex == 0) Color(0xFF1976D2) else Color(0xFF555555)
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = {
                            Text(
                                text = "Đã lưu ($savedCount)",
                                fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = if (selectedTabIndex == 1) Color(0xFF1976D2) else Color(0xFF555555)
                            )
                        }
                    )
                }
            }
        }

        // --- LazyColumn for main content including chips and cards ---
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // --- Filter Chips Section ---
            if (availableCities.size > 1) {
                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(availableCities) { city ->
                            val isSelected = selectedCityFilter == city
                            val countInTab = if (city == "Mọi thành phố") {
                                toursForTab.size
                            } else {
                                toursForTab.count { it.location == city }
                            }
                            
                            val displayText = if (city == "Mọi thành phố") {
                                "Mọi thành phố"
                            } else {
                                "$city ($countInTab)"
                            }

                            Surface(
                                modifier = Modifier.clickable { selectedCityFilter = city },
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(
                                    width = 1.2.dp,
                                    color = if (isSelected) Color(0xFF1976D2) else Color(0xFFCCCCCC)
                                ),
                                color = if (isSelected) Color(0xFFE3F2FD) else Color.White
                            ) {
                                Text(
                                    text = displayText,
                                    color = if (isSelected) Color(0xFF1976D2) else Color(0xFF444444),
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                item { Spacer(modifier = Modifier.height(12.dp)) }
            }

            // --- Tour Cards List ---
            if (filteredTours.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 60.dp, horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (selectedTabIndex == 0) "Chưa có tour nào được đặt" else "Không có tour nào trong mục đã lưu",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(filteredTours, key = { it.id }) { tour ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color(0xFFE5E5E5)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Left rounded image
                                Image(
                                    painter = painterResource(id = tour.imageRes),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(105.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                // Right tour metadata
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(
                                            text = tour.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF1E272C),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f).padding(end = 6.dp)
                                        )

                                        // Circular heart button
                                        Surface(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clickable { onToggleSave(tour.id) },
                                            shape = CircleShape,
                                            border = BorderStroke(1.dp, Color(0xFFF0F0F0)),
                                            color = Color.White
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = if (tour.isSaved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                                    contentDescription = "Save",
                                                    tint = if (tour.isSaved) Color.Red else Color.LightGray,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Star Rating
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = "Rating Star",
                                            tint = Color(0xFFFFB300),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${tour.rating} (${tour.reviewCount}) • ${tour.bookingCountText}",
                                            color = Color(0xFF757575),
                                            fontSize = 11.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    // Location Pin
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.LocationOn,
                                            contentDescription = "Location Pin",
                                            tint = Color(0xFF9E9E9E),
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = tour.location,
                                            color = Color(0xFF757575),
                                            fontSize = 11.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Tags (pastel backgrounds)
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        tour.tags.forEachIndexed { idx, tag ->
                                            val (bgColor, textColor) = if (idx == 0) {
                                                Color(0xFFF3E5F5) to Color(0xFF7B1FA2)
                                            } else {
                                                Color(0xFFE1F5FE) to Color(0xFF0288D1)
                                            }
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = bgColor
                                            ) {
                                                Text(
                                                    text = tag,
                                                    color = textColor,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Prices
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "Khởi điểm là ",
                                                color = Color(0xFF757575),
                                                fontSize = 11.sp
                                            )
                                            Text(
                                                text = tour.originalPrice,
                                                color = Color(0xFF9E9E9E),
                                                fontSize = 11.sp,
                                                textDecoration = TextDecoration.LineThrough
                                            )
                                        }
                                        Text(
                                            text = tour.currentPrice,
                                            color = Color(0xFFD32F2F),
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Bottom wide outlined button - Now interactive for booked tours!
                            val buttonText = if (selectedTabIndex == 0) "Xem vé điện tử & Quản lý" else "Xem mọi phương án"
                            val buttonColor = if (selectedTabIndex == 0) Color(0xFF4CAF50) else Color(0xFF1976D2)
                            OutlinedButton(
                                onClick = {
                                    if (selectedTabIndex == 0) {
                                        onViewItinerary(tour)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp),
                                shape = RoundedCornerShape(22.dp),
                                border = BorderStroke(1.2.dp, buttonColor),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = buttonColor)
                            ) {
                                Text(
                                    text = buttonText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            // --- Footer matching image label ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                val footerText = if (selectedTabIndex == 0) {
                    "Kết thúc danh sách tour đặt chỗ của quý khách"
                } else {
                    "Kết thúc danh sách đã lưu của quý khách"
                }
                Text(
                    text = footerText,
                    color = Color(0xFF9E9E9E),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailedItineraryScreen(
    tour: TourItem,
    onBack: () -> Unit
) {
    // Retrieve correct itinerary based on tour ID
    val dayPlans = when (tour.id) {
        "4" -> mockThailandItinerary
        "5" -> mockPhiPhiItinerary
        else -> mockThailandItinerary // Fallback
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabList = listOf(
        "Hành trình chi tiết",
        "Vé điện tử",
        "Thông tin thêm"
    )

    // Days expanded map state (Day 1 is open by default)
    val expandedDays = remember {
        mutableStateMapOf<Int, Boolean>().apply {
            put(1, true) // Day 1 expanded by default
        }
    }

    // State for location detail popup dialog
    var activeActivityDetail by remember { mutableStateOf<ActivityItem?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp) // Leave space for pinned bottom bar
        ) {
            // ── 1. Hero Header Image matching TourDetailScreen exactly ──
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    Image(
                        painter = painterResource(id = tour.imageRes),
                        contentDescription = tour.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    
                    // Gradient overlay
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

                    // Floating circular header buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.4f)
                        ) {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.4f)
                            ) {
                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.4f)
                            ) {
                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = if (tour.isSaved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        tint = if (tour.isSaved) Color.Red else Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── 2. Sticky Scrollable Tab Row with green primary color indicator ──
            stickyHeader {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
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
                            HorizontalDivider(color = Color(0xFFF0F0F0))
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
            }

            // ── 3. Content Area based on Selected Tab ──
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    // Tour general title and metadata
                    Text(
                        text = tour.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = DarkTextColor
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = PrimaryGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = tour.location,
                            color = SecondaryTextColor,
                            fontSize = 14.sp
                        )
                    }

                    Text(
                        text = "${tour.currentPrice} / người",
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    when (selectedTab) {
                        0 -> {
                            // ── TAB 0: HÀNH TRÌNH CHI TIẾT (Timeline Accordion) ──
                            Text(
                                text = "Tiến độ hành trình chi tiết của bạn",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = DarkTextColor
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // Day Plans Column of Timeline items
                            dayPlans.forEachIndexed { index, dayPlan ->
                                val isExpanded = expandedDays[dayPlan.dayNumber] ?: false
                                val hasCurrentActivity = dayPlan.activities.any { it.status == ActivityStatus.Current }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Min)
                                ) {
                                    // Left vertical connection line & dots
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .width(36.dp)
                                            .fillMaxHeight()
                                    ) {
                                        if (index > 0) {
                                            Box(
                                                modifier = Modifier
                                                    .width(2.dp)
                                                    .height(20.dp)
                                                    .background(Color(0xFFCCD5C7))
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.height(20.dp))
                                        }

                                        val dotColor = if (hasCurrentActivity) PrimaryGreen else Color(0xFFB0BEC5)
                                        val dotSize = if (hasCurrentActivity) 16.dp else 12.dp
                                        Surface(
                                            modifier = Modifier.size(dotSize),
                                            shape = CircleShape,
                                            color = dotColor
                                        ) {}

                                        if (index < dayPlans.size - 1) {
                                            Box(
                                                modifier = Modifier
                                                    .width(2.dp)
                                                    .weight(1f)
                                                    .background(Color(0xFFCCD5C7))
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }

                                    // Right Accordion Day Card
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(bottom = 16.dp)
                                            .clickable {
                                                expandedDays[dayPlan.dayNumber] = !isExpanded
                                            },
                                        shape = RoundedCornerShape(24.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp)
                                        ) {
                                            // Header
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = dayPlan.dayTitle,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 17.sp,
                                                    color = DarkTextColor
                                                )
                                                
                                                Icon(
                                                    imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                                    contentDescription = "Expand Accordion",
                                                    tint = PrimaryGreen,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }

                                            // Expanded Day Details
                                            if (isExpanded) {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = dayPlan.daySubtitle,
                                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                                    color = SecondaryTextColor,
                                                    fontSize = 13.sp
                                                )

                                                Spacer(modifier = Modifier.height(16.dp))

                                                Column(
                                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                                ) {
                                                    dayPlan.activities.forEach { activity ->
                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .clickable { activeActivityDetail = activity }
                                                                .padding(vertical = 4.dp),
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                                        ) {
                                                            when (activity.status) {
                                                                ActivityStatus.Completed -> {
                                                                    Icon(
                                                                        imageVector = Icons.Filled.CheckCircle,
                                                                        contentDescription = "Completed",
                                                                        tint = PrimaryGreen,
                                                                        modifier = Modifier.size(22.dp)
                                                                    )
                                                                }
                                                                ActivityStatus.Current -> {
                                                                    Box(
                                                                        modifier = Modifier.size(22.dp),
                                                                        contentAlignment = Alignment.Center
                                                                    ) {
                                                                        Box(
                                                                            modifier = Modifier
                                                                                .size(22.dp)
                                                                                .border(2.dp, PrimaryGreen, CircleShape)
                                                                        )
                                                                        Box(
                                                                            modifier = Modifier
                                                                                .size(10.dp)
                                                                                .background(PrimaryGreen, CircleShape)
                                                                        )
                                                                    }
                                                                }
                                                                ActivityStatus.Upcoming -> {
                                                                    Box(
                                                                        modifier = Modifier
                                                                            .size(22.dp)
                                                                            .border(1.5.dp, PrimaryGreen.copy(alpha = 0.5f), CircleShape)
                                                                    )
                                                                }
                                                            }

                                                            Text(
                                                                text = activity.title,
                                                                fontWeight = FontWeight.Medium,
                                                                fontSize = 15.sp,
                                                                color = DarkTextColor,
                                                                modifier = Modifier.weight(1f)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        1 -> {
                            // ── TAB 1: VÉ ĐIỆN TỬ (E-Ticket) ──
                            Text(
                                text = "Thông tin Vé Điện Tử đặt chỗ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = DarkTextColor
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "MÃ ĐẶT CHỖ",
                                            fontWeight = FontWeight.Bold,
                                            color = SecondaryTextColor,
                                            fontSize = 12.sp
                                        )
                                        Surface(
                                            color = LightGreenBackground,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "ĐÃ THANH TOÁN",
                                                color = PrimaryGreen,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "FL-${100000 + tour.id.hashCode() % 900000}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp,
                                        color = DarkTextColor
                                    )

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = Color(0xFFF2F2F2))

                                    // Customer Details
                                    TicketDetailRow("Khách hàng", "Bảo")
                                    TicketDetailRow("Khởi hành", tour.bookingDate ?: "28/05/2026")
                                    TicketDetailRow("Thời gian", "5 ngày 4 đêm")
                                    TicketDetailRow("Số lượng", "2 Người lớn, 0 Trẻ em")

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = Color(0xFFF2F2F2))

                                    // Simulating QR Code Scanner Graphic
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Surface(
                                            modifier = Modifier.size(160.dp),
                                            shape = RoundedCornerShape(16.dp),
                                            color = Color(0xFFF5F5F5),
                                            border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.QrCode,
                                                    contentDescription = "QR Code Scanner Icon",
                                                    tint = DarkTextColor,
                                                    modifier = Modifier.size(90.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Đưa mã này cho HDV tại điểm xuất phát",
                                            color = SecondaryTextColor,
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                        2 -> {
                            // ── TAB 2: THÔNG TIN THÊM (Preparation guidelines) ──
                            Text(
                                text = "Thông tin cẩm nang chuẩn bị chuyến đi",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = DarkTextColor
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = "• Trang phục viếng đền đài: Quý khách vui lòng mặc quần dài qua gối và áo có tay lịch sự khi ghé thăm cung điện hoặc đền chùa linh thiêng.",
                                        color = SecondaryTextColor,
                                        fontSize = 14.sp,
                                        lineHeight = 22.sp
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "• Tiền tệ khuyên dùng: Vui lòng chuẩn bị sẵn tiền Baht Thái (THB) để chi tiêu cá nhân tại chợ đêm hoặc mua sắm vặt. Có thể mang theo thẻ tín dụng quốc tế.",
                                        color = SecondaryTextColor,
                                        fontSize = 14.sp,
                                        lineHeight = 22.sp
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "• SIM Card và Liên lạc: HDV sẽ phát SIM du lịch Thái Lan ngay tại sân bay đón đoàn để quý khách duy trì liên lạc 4G xuyên suốt hành trình.",
                                        color = SecondaryTextColor,
                                        fontSize = 14.sp,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── 4. Pinned Bottom Bar ──
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
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Dùng FLOURISH để tiết kiệm 50.000 đ",
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
                            text = "Tổng",
                            color = SecondaryTextColor,
                            fontSize = 12.sp
                        )
                        Text(
                            text = tour.currentPrice,
                            color = Color(0xFFFF5722),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Heart button
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                            color = Color.White
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (tour.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (tour.isSaved) Color.Red else DarkTextColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Action button (QR Code E-ticket check-in)
                        Button(
                            onClick = { selectedTab = 1 }, // Navigates to QR Code Ticket Tab
                            modifier = Modifier
                                .width(180.dp)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryGreen
                            )
                        ) {
                            Text(
                                text = "Xem Vé QR",
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

    // Modal popup dialog if an activity detail is active
    if (activeActivityDetail != null) {
        LocationDetailDialog(
            activity = activeActivityDetail!!,
            onDismiss = { activeActivityDetail = null }
        )
    }
}

@Composable
fun TicketDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = SecondaryTextColor, fontSize = 13.sp)
        Text(text = value, fontWeight = FontWeight.Bold, color = DarkTextColor, fontSize = 13.sp)
    }
}

@Composable
fun LocationDetailDialog(
    activity: ActivityItem,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                // Image with rounded corners and overlapping circular close button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    Image(
                        painter = painterResource(id = activity.imageRes),
                        contentDescription = activity.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
                        contentScale = ContentScale.Crop
                    )

                    // Overlapping circular close button (white '✕' in semi-transparent circle)
                    Surface(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(36.dp)
                            .align(Alignment.TopEnd)
                            .clickable { onDismiss() },
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.4f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "✕",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Location Title - capitalized, extra-bold, centered
                Text(
                    text = activity.title.uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = Color(0xFF1E272C)
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Location Description - comfortable line height, centered, gray
                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF555555),
                        lineHeight = 22.sp,
                        fontSize = 14.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }
        }
    }
}

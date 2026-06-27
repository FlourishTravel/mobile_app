package com.example.flourishtravelapp.ui.screens

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.BookingSummary
import com.example.flourishtravelapp.data.model.Tour
import com.example.flourishtravelapp.data.model.FavoriteRequest
import kotlinx.coroutines.launch
import com.example.flourishtravelapp.data.model.UserBookingDetailDto
import com.example.flourishtravelapp.data.mapper.toDayPlans
import com.example.flourishtravelapp.R

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
import coil.compose.AsyncImage
import com.example.flourishtravelapp.ui.components.FloraJourneyPanel
import com.example.flourishtravelapp.ui.components.FloraPostTourFeedbackPanel
import com.example.flourishtravelapp.ui.components.rememberFloraJourneyState
import com.example.flourishtravelapp.ui.theme.*

// Mock model for our tours
data class TourItem(
    val id: String,
    val catalogTourId: String? = null,
    val title: String,
    val rating: Double,
    val reviewCount: Int,
    val bookingCountText: String,
    val location: String,
    val originalPrice: String,
    val currentPrice: String,
    val imageRes: Int,
    val thumbnailUrl: String? = null,
    val tags: List<String>,
    val isSaved: Boolean,
    val isBooked: Boolean,
    val bookingDate: String? = null,
    val rawBookingStatus: String? = null
)

private fun formatBookingStatus(status: String): String = when (status.lowercase()) {
    "pending" -> "Chờ thanh toán"
    "paid" -> "Đã thanh toán"
    "cancelled" -> "Đã hủy"
    else -> status
}

private fun fallbackImageForTitle(title: String): Int = when {
    title.contains("Phi Phi", ignoreCase = true) -> R.drawable.phiphi_bg
    title.contains("Phuket", ignoreCase = true) -> R.drawable.phuket_bg
    title.contains("Chiang Mai", ignoreCase = true) -> R.drawable.chiangmai_bg
    else -> R.drawable.awat_bg
}

private fun isUuid(value: String): Boolean =
    Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$").matches(value)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier, 
    onBack: () -> Unit,
    onProfileClick: () -> Unit,
    onOpenGroupChat: (String) -> Unit = {}
) {
    var selectedBookedTourForDetail by remember { mutableStateOf<TourItem?>(null) }

    if (selectedBookedTourForDetail != null) {
        DetailedItineraryScreen(
            tour = selectedBookedTourForDetail!!,
            onBack = { selectedBookedTourForDetail = null },
            onOpenGroupChat = onOpenGroupChat
        )
    } else {
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
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var bookingsList by remember { mutableStateOf<List<BookingSummary>>(emptyList()) }
    var favoritesList by remember { mutableStateOf<List<Tour>>(emptyList()) }
    var isLoadingBookings by remember { mutableStateOf(true) }
    var isLoadingFavorites by remember { mutableStateOf(true) }

    fun refreshBookings() {
        isLoadingBookings = true
        coroutineScope.launch {
            try {
                val response = RetrofitClient.bookingApiService.getMyBookings()
                if (response.isSuccessful) {
                    bookingsList = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoadingBookings = false
            }
        }
    }

    fun refreshFavorites() {
        isLoadingFavorites = true
        coroutineScope.launch {
            try {
                val response = RetrofitClient.favoriteApiService.getFavorites()
                if (response.isSuccessful) {
                    favoritesList = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoadingFavorites = false
            }
        }
    }

    LaunchedEffect(Unit) {
        refreshBookings()
        refreshFavorites()
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Tour của bạn, 1: Đã lưu
    var selectedCityFilter by remember { mutableStateOf("Mọi thành phố") }

    val onToggleSave: (String) -> Unit = { id ->
        coroutineScope.launch {
            try {
                val isSaved = favoritesList.any { it.id == id }
                if (isSaved) {
                    val response = RetrofitClient.favoriteApiService.removeFavorite(id)
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Đã xóa khỏi mục đã lưu", Toast.LENGTH_SHORT).show()
                        refreshFavorites()
                    }
                } else {
                    val response = RetrofitClient.favoriteApiService.addFavorite(FavoriteRequest(id))
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Đã lưu tour", Toast.LENGTH_SHORT).show()
                        refreshFavorites()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val bookedTourItems = bookingsList.map { booking ->
        val title = booking.tourTitle ?: "Tour của bạn"
        val amount = booking.totalAmount?.toLong() ?: 0L
        TourItem(
            id = booking.bookingId,
            catalogTourId = booking.tourId,
            title = title,
            rating = 4.8,
            reviewCount = 12,
            bookingCountText = formatBookingStatus(booking.bookingStatus),
            location = booking.categoryName ?: "Thái Lan",
            originalPrice = String.format("%,dđ", amount + 100000),
            currentPrice = String.format("%,dđ", amount),
            imageRes = fallbackImageForTitle(title),
            thumbnailUrl = booking.tourThumbnailUrl,
            tags = listOf(formatBookingStatus(booking.bookingStatus), "Vé điện tử"),
            isSaved = booking.tourId?.let { tourId -> favoritesList.any { it.id == tourId } } == true,
            isBooked = true,
            bookingDate = booking.sessionStartDate?.let { "Khởi hành: $it" }
                ?: booking.bookedAt?.take(10)?.let { "Đặt ngày: $it" },
            rawBookingStatus = booking.bookingStatus
        )
    }

    val savedTourItems = favoritesList.map { tour ->
        TourItem(
            id = tour.id,
            catalogTourId = tour.id,
            title = tour.title,
            rating = tour.rating ?: 4.7,
            reviewCount = 35,
            bookingCountText = "Khám phá",
            location = tour.destinationCity ?: "Thái Lan",
            originalPrice = String.format("%,.0fđ", tour.basePrice + 200000),
            currentPrice = String.format("%,.0fđ", tour.basePrice),
            imageRes = fallbackImageForTitle(tour.title),
            tags = listOf(tour.badge ?: "Đã lưu", "Vé điện tử"),
            isSaved = true,
            isBooked = false
        )
    }

    val toursForTab = if (selectedTabIndex == 0) bookedTourItems else savedTourItems

    val filteredTours = if (selectedCityFilter == "Mọi thành phố") {
        toursForTab
    } else {
        toursForTab.filter { it.location == selectedCityFilter }
    }

    val bookedCount = bookedTourItems.size
    val savedCount = savedTourItems.size

    val availableCities = remember(toursForTab) {
        listOf("Mọi thành phố") + toursForTab.map { it.location }.distinct()
    }

    LaunchedEffect(selectedTabIndex) {
        selectedCityFilter = "Mọi thành phố"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White
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

                    Text(
                        text = if (selectedTabIndex == 0) "Tour của bạn" else "Đã lưu",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )

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

                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.White,
                    contentColor = Color(0xFF1976D2),
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

        if ((selectedTabIndex == 0 && isLoadingBookings) || (selectedTabIndex == 1 && isLoadingFavorites)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF1976D2))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
                                    if (!tour.thumbnailUrl.isNullOrBlank()) {
                                        AsyncImage(
                                            model = tour.thumbnailUrl,
                                            contentDescription = tour.title,
                                            modifier = Modifier
                                                .size(105.dp)
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop,
                                            placeholder = painterResource(id = tour.imageRes),
                                            error = painterResource(id = tour.imageRes)
                                        )
                                    } else {
                                        Image(
                                            painter = painterResource(id = tour.imageRes),
                                            contentDescription = tour.title,
                                            modifier = Modifier
                                                .size(105.dp)
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

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

                                            Surface(
                                                modifier = Modifier
                                                    .size(34.dp)
                                                    .clickable {
                                                        tour.catalogTourId?.let { onToggleSave(it) }
                                                    },
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

                                val buttonText = if (selectedTabIndex == 0) "Xem vé điện tử & Quản lý" else "Xem mọi phương án"
                                val buttonColor = if (selectedTabIndex == 0) Color(0xFF4CAF50) else Color(0xFF1976D2)
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            if (selectedTabIndex == 0) {
                                                onViewItinerary(tour)
                                            }
                                        },
                                        modifier = Modifier
                                            .weight(1f)
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

                                    // Cancel Booking Button for booked tours
                                    if (selectedTabIndex == 0 && tour.rawBookingStatus?.lowercase() != "cancelled") {
                                        Button(
                                            onClick = {
                                                coroutineScope.launch {
                                                    try {
                                                        val response = RetrofitClient.bookingApiService.cancelBooking(tour.id)
                                                        if (response.isSuccessful) {
                                                            Toast.makeText(context, "Hủy đặt tour thành công", Toast.LENGTH_SHORT).show()
                                                            refreshBookings()
                                                        } else {
                                                            Toast.makeText(context, "Không thể hủy đặt tour", Toast.LENGTH_SHORT).show()
                                                        }
                                                    } catch (e: Exception) {
                                                        e.printStackTrace()
                                                        Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .width(110.dp)
                                                .height(44.dp),
                                            shape = RoundedCornerShape(22.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                                        ) {
                                            Text(
                                                text = "Hủy",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailedItineraryScreen(
    tour: TourItem,
    onBack: () -> Unit,
    onOpenGroupChat: (String) -> Unit = {}
) {
    val catalogId = tour.catalogTourId
    var dayPlans by remember(catalogId) { mutableStateOf<List<DayPlan>>(emptyList()) }
    var isLoadingItinerary by remember(catalogId) { mutableStateOf(!catalogId.isNullOrBlank()) }
    var itineraryError by remember(catalogId) { mutableStateOf<String?>(null) }
    var bookingDetail by remember { mutableStateOf<UserBookingDetailDto?>(null) }

    LaunchedEffect(tour.id) {
        if (tour.isBooked && isUuid(tour.id)) {
            try {
                val response = RetrofitClient.bookingApiService.getBookingDetail(tour.id)
                if (response.isSuccessful) {
                    bookingDetail = response.body()?.data
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            bookingDetail = null
        }
    }

    LaunchedEffect(catalogId) {
        if (catalogId.isNullOrBlank()) {
            dayPlans = emptyList()
            itineraryError = null
            isLoadingItinerary = false
            return@LaunchedEffect
        }
        isLoadingItinerary = true
        itineraryError = null
        try {
            val response = RetrofitClient.bookingApiService.getTourDetail(catalogId)
            if (response.isSuccessful) {
                dayPlans = response.body()?.data?.toDayPlans() ?: emptyList()
            } else {
                dayPlans = emptyList()
                itineraryError = "Không thể tải hành trình (${response.code()})"
            }
        } catch (e: Exception) {
            dayPlans = emptyList()
            itineraryError = e.message ?: "Không thể tải hành trình"
        } finally {
            isLoadingItinerary = false
        }
    }

    val floraBookingId = if (tour.isBooked && isUuid(tour.id)) tour.id else null
    val floraState = floraBookingId?.let { rememberFloraJourneyState(it) }

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
                            if (tour.isBooked && isUuid(tour.id)) {
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = PrimaryGreen
                                ) {
                                    Text(
                                        text = "Chat đoàn",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .clickable { onOpenGroupChat(tour.id) }
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
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

            if (floraBookingId != null) {
                item {
                    floraState?.let { FloraJourneyPanel(state = it) }
                }
                item {
                    FloraPostTourFeedbackPanel(bookingId = floraBookingId)
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
                            when {
                                isLoadingItinerary -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = PrimaryGreen)
                                    }
                                }
                                itineraryError != null -> {
                                    Text(
                                        text = itineraryError ?: "",
                                        color = Color(0xFFD32F2F),
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                dayPlans.isEmpty() -> {
                                    Text(
                                        text = "Chưa có hành trình chi tiết cho tour này.",
                                        color = SecondaryTextColor,
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                else -> {
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
                            }
                        }

                        1 -> {
                            // ── TAB 1: VÉ ĐIỆN TỬ (E-Ticket) ──
                            val detail = bookingDetail
                            val orderIdDisplay = detail?.paymentOrderId?.takeIf { it.isNotBlank() }
                                ?: detail?.bookingId
                                ?: "FL-${100000 + tour.id.hashCode() % 900000}"
                            val customerName = detail?.guestNames?.substringBefore(",")?.trim()?.takeIf { it.isNotBlank() }
                                ?: detail?.emergencyContactName
                                ?: "—"
                            val departureDisplay = detail?.sessionStartDate?.take(10)
                                ?: tour.bookingDate?.removePrefix("Khởi hành: ")?.removePrefix("Đặt ngày: ")
                                ?: "—"
                            val durationDisplay = when {
                                detail?.tourDurationDays != null && detail.tourDurationNights != null ->
                                    "${detail.tourDurationDays} ngày ${detail.tourDurationNights} đêm"
                                detail?.tourDurationDays != null ->
                                    "${detail.tourDurationDays} ngày"
                                else -> "—"
                            }
                            val guestCountDisplay = detail?.guestCount?.let { "$it khách" } ?: "—"

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
                                        text = orderIdDisplay,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp,
                                        color = DarkTextColor
                                    )

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = Color(0xFFF2F2F2))

                                    // Customer Details
                                    TicketDetailRow("Khách hàng", customerName)
                                    TicketDetailRow("Khởi hành", departureDisplay)
                                    TicketDetailRow("Thời gian", durationDisplay)
                                    TicketDetailRow("Số lượng", guestCountDisplay)

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

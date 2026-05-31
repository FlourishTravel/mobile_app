package com.example.flourishtavelapp.ui.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.R
import com.example.flourishtavelapp.ui.theme.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TourDetailScreen(
    onBack: () -> Unit,
    onBookNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var adultCount by remember { mutableIntStateOf(2) }
    var childCount by remember { mutableIntStateOf(0) }
    var selectedDateIndex by remember { mutableIntStateOf(1) }

    // Price calculation
    val adultPrice = 2500000L
    val childPrice = 1250000L
    val discount = 50000L
    val totalPrice = (adultCount * adultPrice) + (childCount * childPrice) - discount
    val displayPrice = if (totalPrice > 0) totalPrice else 0L

    val tabList = listOf(
        "Các lựa chọn gói",
        "Thông tin chuyến đi",
        "Thông tin thêm",
        "Chính sách hủy",
        "Câu hỏi thường gặp"
    )

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
                    Image(
                        painter = painterResource(id = R.drawable.awat_bg),
                        contentDescription = "Wat Arun",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
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
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.4f)
                        ) {
                            IconButton(onClick = onBack) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
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
                                        Icons.Default.Share,
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
                                        Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── Sticky Horizontal Tab Bar (Sidebar equivalent in Compose) ──
            stickyHeader {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 4.dp
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

            // ── Content Area based on Selected Tab ──
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
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

                            // Date selectors row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                DateItem(
                                    month = "tháng 5", day = "28", weekday = "T5", label = "Hôm nay",
                                    isSelected = selectedDateIndex == 0,
                                    modifier = Modifier.weight(1f),
                                    onClick = { selectedDateIndex = 0 }
                                )
                                DateItem(
                                    month = "tháng 5", day = "29", weekday = "T6", label = "Ngày mai",
                                    isSelected = selectedDateIndex == 1,
                                    modifier = Modifier.weight(1f),
                                    onClick = { selectedDateIndex = 1 }
                                )
                                DateItem(
                                    month = "tháng 5", day = "30", weekday = "T7", label = null,
                                    isSelected = selectedDateIndex == 2,
                                    modifier = Modifier.weight(1f),
                                    onClick = { selectedDateIndex = 2 }
                                )
                                // More option
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

                            Spacer(modifier = Modifier.height(20.dp))

                            // Package details card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(
                                            "Tour Riêng Khám Phá Địa Đạo\nCủ Chi Từ TP. Hồ Chí Minh\n(Phú Mỹ), Việt Nam",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = DarkTextColor,
                                            lineHeight = 20.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.clickable { }
                                        ) {
                                            Text(
                                                "Chi tiết",
                                                color = PrimaryGreen,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                            Icon(
                                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                                null,
                                                tint = PrimaryGreen,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Info,
                                            null,
                                            tint = SecondaryTextColor,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            "Không sẵn có trên ngày này",
                                            color = SecondaryTextColor,
                                            fontSize = 13.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(
                                        onClick = { },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent
                                        ),
                                        border = BorderStroke(1.dp, PrimaryGreen)
                                    ) {
                                        Text(
                                            "Xem các ngày khác",
                                            color = PrimaryGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // ── Redesigned Guest Selection replaces activities ──
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        "Số lượng khách",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = DarkTextColor
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Guest option: Adults
                                    GuestCounterRow(
                                        type = "Người lớn",
                                        ageRange = "Từ 12 tuổi trở lên",
                                        priceLabel = "2,500,000đ / khách",
                                        count = adultCount,
                                        onIncrement = { adultCount++ },
                                        onDecrement = { if (adultCount > 0) adultCount-- }
                                    )

                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 16.dp),
                                        color = Color(0xFFF2F2F2)
                                    )

                                    // Guest option: Children
                                    GuestCounterRow(
                                        type = "Trẻ em",
                                        ageRange = "Từ 2 - 11 tuổi",
                                        priceLabel = "1,250,000đ / khách",
                                        count = childCount,
                                        onIncrement = { childCount++ },
                                        onDecrement = { if (childCount > 0) childCount-- }
                                    )
                                }
                            }
                        }

                        1 -> {
                            // ── TAB 1: THÔNG TIN SẢN PHẨM ──
                            Text(
                                "Tour Khám Phá Thái Lan 5N4Đ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
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
                                    "Bangkok – Chiang Mai – Phuket",
                                    color = SecondaryTextColor,
                                    fontSize = 13.sp
                                )
                            }
                            Text(
                                "4.500.000đ / người",
                                color = PrimaryGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            Text(
                                "Hành trình 5 ngày 4 đêm khám phá vẻ đẹp huyền bí của các ngôi chùa tại Bangkok, tận hưởng không khí trong lành tại Chiang Mai và đắm mình trong làn nước xanh biếc của Phuket.",
                                color = SecondaryTextColor,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                "Điểm nổi bật",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = DarkTextColor
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            HighlightBulletItem("Cung điện & Chùa Vàng")
                            HighlightBulletItem("Ẩm thực đường phố")
                            HighlightBulletItem("Biển Phuket")
                            HighlightBulletItem("Chợ đêm & Nightlife")
                            HighlightBulletItem("Trải nghiệm voi Chiang Mai")

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                "Lịch trình",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = DarkTextColor
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            ItineraryTimelineItem(
                                day = 1,
                                title = "Đến Bangkok & Dạo thuyền sông Chao Phraya",
                                desc = "Nhận phòng khách sạn, thưởng thức ẩm thực địa phương và ngắm cảnh sông về đêm."
                            )
                            ItineraryTimelineItem(
                                day = 2,
                                title = "Khám phá Chùa Vàng & Cung điện Hoàng gia",
                                desc = "Tham quan Wat Phra Kaew và Wat Arun, sau đó bay đến Chiang Mai."
                            )
                            ItineraryTimelineItem(
                                day = 3,
                                title = "Chiang Mai - Trải nghiệm văn hóa & Thiên nhiên",
                                desc = "Thăm làng voi nhân đạo và lên đỉnh Doi Suthep ngắm toàn cảnh thành phố."
                            )
                            ItineraryTimelineItem(
                                day = 4,
                                title = "Phuket - Đảo Phi Phi & Lặn ngắm san hô",
                                desc = "Trọn vẹn một ngày lênh đênh trên biển xanh và thư giãn tại bãi cát trắng."
                            )
                            ItineraryTimelineItem(
                                day = 5,
                                title = "Tạm biệt Thái Lan",
                                desc = "Mua sắm đặc sản tại chợ địa phương và làm thủ tục bay về Việt Nam.",
                                isLast = true
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                "Dịch vụ bao gồm",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = DarkTextColor
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            InclusionItem(Icons.Default.Flight, "Vé máy bay khứ hồi")
                            InclusionItem(Icons.Default.Hotel, "Khách sạn 4 sao")
                            InclusionItem(Icons.Default.Restaurant, "Bữa ăn theo chương trình")
                            InclusionItem(Icons.Default.SupportAgent, "HDV tiếng Việt")
                            InclusionItem(Icons.Default.Security, "Bảo hiểm du lịch")
                        }

                        2 -> {
                            // ── TAB 2: THÔNG TIN THÊM ──
                            Text(
                                "Thông tin thêm",
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
                                "Chính sách hủy",
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
                            // ── TAB 4: CÂU HỎI THƯỜNG GẶP ──
                            Text(
                                "Câu hỏi thường gặp",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = DarkTextColor
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            FAQItem(
                                q = "Tour có bao gồm xe đưa đón tại sân bay không?",
                                a = "Có, toàn bộ lịch trình tour trọn gói đã bao gồm xe đưa đón 2 chiều tại sân bay bên Thái Lan."
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            FAQItem(
                                q = "Trẻ em dưới 2 tuổi có được miễn phí không?",
                                a = "Trẻ em dưới 2 tuổi sẽ được miễn phí hoàn toàn dịch vụ land tour, chỉ tính phụ thu vé máy bay theo quy định của hãng."
                            )
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
                            "Tổng",
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
                        // Heart button
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                            color = Color.White
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.FavoriteBorder,
                                    null,
                                    tint = DarkTextColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Next step button
                        Button(
                            onClick = onBookNowClick,
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
            // Decrement button
            Surface(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onDecrement() },
                shape = CircleShape,
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                color = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Remove,
                        null,
                        tint = DarkTextColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Text(
                text = count.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = DarkTextColor
            )

            // Increment button
            Surface(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onIncrement() },
                shape = CircleShape,
                color = PrimaryGreen
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Add,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// ── Highlight Bullet ──
@Composable
fun HighlightBulletItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 3.dp)
    ) {
        Icon(
            Icons.Default.CheckCircle,
            null,
            tint = PrimaryGreen,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text,
            color = DarkTextColor,
            fontSize = 14.sp
        )
    }
}

// ── Inclusion Item ──
@Composable
fun InclusionItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            icon,
            null,
            tint = PrimaryGreen,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text,
            color = DarkTextColor,
            fontSize = 14.sp
        )
    }
}

// ── FAQ Item ──
@Composable
fun FAQItem(q: String, a: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F9F9), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Text(
            "Hỏi: $q",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = DarkTextColor
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "Đáp: $a",
            color = SecondaryTextColor,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}

// ── Itinerary Timeline Item ──
@Composable
fun ItineraryTimelineItem(
    day: Int,
    title: String,
    desc: String,
    isLast: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(PrimaryGreen, CircleShape)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(1.5.dp)
                        .height(80.dp)
                        .background(PrimaryGreen.copy(alpha = 0.4f))
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.padding(bottom = 20.dp)) {
            Text(
                "Ngày $day: $title",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DarkTextColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                desc,
                color = SecondaryTextColor,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}

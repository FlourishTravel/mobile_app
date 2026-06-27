package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.mapper.toGuideTour
import com.example.flourishtravelapp.ui.components.GuideBottomNavigation
import com.example.flourishtravelapp.ui.theme.*

/** Chỉ số tab bottom nav HDV (5 tab). */
internal object GuideTabs {
    const val HOME = 0
    const val TOURS = 1
    const val GROUP = 2      // Khách + Giao tiếp
    const val WORK = 3       // Vận hành + Chi phí
    const val PROFILE = 4
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideHomeScreen(
    guide: GuideAccount,
    modifier: Modifier = Modifier,
    selectedTab: Int = GuideTabs.HOME,
    onTabSelected: (Int) -> Unit = {},
    initialGuestSessionId: String? = null,
    initialGroupSubTab: Int = 0,
    initialWorkSubTab: Int = 0,
    onTourClick: (GuideTour) -> Unit,
    onLogout: () -> Unit,
    onGuideUpdated: (GuideAccount) -> Unit = {},
    onOpenGroupChat: (String) -> Unit = {},
    onEditProfile: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onFloraSettings: () -> Unit = {},
    onSupport: () -> Unit = {}
) {
    var displayGuide by remember(guide) { mutableStateOf(guide) }
    val safeTab = selectedTab.coerceIn(GuideTabs.HOME, GuideTabs.PROFILE)
    var groupSubTab by remember { mutableIntStateOf(initialGroupSubTab.coerceIn(0, 1)) }
    var workSubTab by remember { mutableIntStateOf(initialWorkSubTab.coerceIn(0, 1)) }

    LaunchedEffect(initialGroupSubTab) {
        groupSubTab = initialGroupSubTab.coerceIn(0, 1)
    }
    LaunchedEffect(initialWorkSubTab) {
        workSubTab = initialWorkSubTab.coerceIn(0, 1)
    }

    Scaffold(
        modifier = modifier,
        containerColor = NatureGreenBackground,
        topBar = {
            when (safeTab) {
                GuideTabs.TOURS -> GuideTopAppBar(title = "Tour của tôi")
                GuideTabs.GROUP -> {
                    Column {
                        GuideTopAppBar(title = "Đoàn")
                        GuideSecondaryTabRow(
                            tabs = listOf("Khách hàng", "Giao tiếp"),
                            selectedIndex = groupSubTab,
                            onTabSelected = { groupSubTab = it }
                        )
                    }
                }
                GuideTabs.WORK -> {
                    Column {
                        GuideTopAppBar(title = "Vận hành")
                        GuideSecondaryTabRow(
                            tabs = listOf("Lịch vận hành", "Chi phí"),
                            selectedIndex = workSubTab,
                            onTabSelected = { workSubTab = it }
                        )
                    }
                }
                else -> Unit
            }
        },
        bottomBar = {
            GuideBottomNavigation(
                selectedTab = safeTab,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (safeTab) {
                GuideTabs.HOME -> GuideDashboardContent(
                    guide = displayGuide,
                    onTourClick = onTourClick,
                    onOpenGuestsTab = { onTabSelected(GuideTabs.GROUP) },
                    onSessionsLoaded = { count ->
                        displayGuide = displayGuide.copy(totalTours = count)
                        onGuideUpdated(displayGuide)
                    }
                )
                GuideTabs.TOURS -> GuideTourListContent(
                    showPageHeader = false,
                    onTourClick = onTourClick,
                    onSessionsLoaded = { count ->
                        displayGuide = displayGuide.copy(totalTours = count)
                        onGuideUpdated(displayGuide)
                    }
                )
                GuideTabs.GROUP -> {
                    when (groupSubTab) {
                        0 -> GuideGuestManagementScreen(
                            modifier = Modifier.fillMaxSize(),
                            initialSessionId = initialGuestSessionId,
                            onOpenGroupChat = onOpenGroupChat
                        )
                        else -> GuideCommunicationHubScreen(
                            modifier = Modifier.fillMaxSize(),
                            onOpenFullScreenChat = onOpenGroupChat
                        )
                    }
                }
                GuideTabs.WORK -> {
                    when (workSubTab) {
                        0 -> GuideOperationsScreen(
                            modifier = Modifier.fillMaxSize(),
                            onOpenGuestsTab = { onTabSelected(GuideTabs.GROUP) },
                            onTourClick = onTourClick
                        )
                        else -> GuideExpensesScreen(modifier = Modifier.fillMaxSize())
                    }
                }
                GuideTabs.PROFILE -> GuideProfileContent(
                    guide = displayGuide,
                    onEditProfile = onEditProfile,
                    onNotifications = onNotifications,
                    onFloraSettings = onFloraSettings,
                    onSupport = onSupport,
                    onLogout = onLogout
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuideTourListContent(
    modifier: Modifier = Modifier,
    showPageHeader: Boolean = true,
    onTourClick: (GuideTour) -> Unit,
    onSessionsLoaded: (Int) -> Unit = {}
) {
    var tours by remember { mutableStateOf<List<GuideTour>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var statusFilter by remember { mutableStateOf<TourStatus?>(null) }

    val statusFilters = listOf(
        null to "Tất cả",
        TourStatus.UPCOMING to "Sắp tới",
        TourStatus.ONGOING to "Đang diễn ra",
        TourStatus.COMPLETED to "Đã xong"
    )

    LaunchedEffect(Unit) {
        isLoading = true
        loadError = null
        try {
            val response = RetrofitClient.guideApiService.getSessions()
            if (response.isSuccessful && response.body()?.success == true) {
                tours = response.body()?.data.orEmpty().map { it.toGuideTour() }
                onSessionsLoaded(tours.size)
            } else {
                loadError = response.body()?.message ?: "Không tải được danh sách tour"
            }
        } catch (e: Exception) {
            loadError = e.localizedMessage ?: "Lỗi kết nối"
        } finally {
            isLoading = false
        }
    }

    val ongoingTours = tours.filter { it.status == TourStatus.ONGOING }
    val upcomingTours = tours.filter { it.status == TourStatus.UPCOMING }
    val completedTours = tours.filter { it.status == TourStatus.COMPLETED }
    val filteredTours = statusFilter?.let { filter -> tours.filter { it.status == filter } } ?: tours

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(if (showPageHeader) 16.dp else 12.dp))

        if (showPageHeader) {
            GuideScreenTitle(
                title = "Tour của tôi",
                subtitle = "Danh sách chuyến được phân công."
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(end = 4.dp)
        ) {
            items(statusFilters) { (status, label) ->
                FilterChip(
                    selected = statusFilter == status,
                    onClick = { statusFilter = status },
                    label = { Text(label, fontSize = 12.sp) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            GuideLoadingBox()
        } else {
            loadError?.let { GuideErrorText(it); Spacer(modifier = Modifier.height(8.dp)) }

            if (tours.isEmpty() && loadError == null) {
                Text("Chưa có tour được phân công.", color = SecondaryTextColor, fontSize = 14.sp)
            }

            if (statusFilter != null) {
                if (filteredTours.isEmpty()) {
                    Text("Không có tour trong mục này.", color = SecondaryTextColor, fontSize = 14.sp)
                } else {
                    filteredTours.forEach { tour ->
                        GuideTourCard(tour = tour, onClick = { onTourClick(tour) })
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            } else {
                if (ongoingTours.isNotEmpty()) {
                    GuideSectionHeader(title = "Đang diễn ra", count = ongoingTours.size)
                    Spacer(modifier = Modifier.height(12.dp))
                    ongoingTours.forEach { tour ->
                        GuideTourCard(tour = tour, onClick = { onTourClick(tour) })
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                if (upcomingTours.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    GuideSectionHeader(title = "Sắp khởi hành", count = upcomingTours.size)
                    Spacer(modifier = Modifier.height(12.dp))
                    upcomingTours.forEach { tour ->
                        GuideTourCard(tour = tour, onClick = { onTourClick(tour) })
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                if (completedTours.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    GuideSectionHeader(title = "Đã hoàn thành", count = completedTours.size)
                    Spacer(modifier = Modifier.height(12.dp))
                    completedTours.forEach { tour ->
                        GuideTourCard(tour = tour, onClick = { onTourClick(tour) })
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
internal fun GuideProfileContent(
    guide: GuideAccount,
    modifier: Modifier = Modifier,
    onEditProfile: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onFloraSettings: () -> Unit = {},
    onSupport: () -> Unit = {},
    onLogout: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NatureGreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(colors = listOf(Color(0xFF004D40), PrimaryGreen)))
                .statusBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.size(88.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.25f)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(guide.name, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(guide.handle, color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Surface(shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha = 0.2f)) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFFFD54F), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${guide.rating} · ${guide.totalTours} tour", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(20.dp))
            ProfileInfoRow(icon = Icons.Default.Phone, label = "Điện thoại", value = guide.phone)
            Spacer(modifier = Modifier.height(10.dp))
            ProfileInfoRow(icon = Icons.Default.Explore, label = "Chuyên môn", value = guide.specialty)
            Spacer(modifier = Modifier.height(10.dp))
            ProfileInfoRow(icon = Icons.Default.WorkHistory, label = "Tổng tour", value = "${guide.totalTours} tour đã dẫn")
            Spacer(modifier = Modifier.height(20.dp))
            GuideProfileMenuRow(icon = Icons.Default.Edit, label = "Chỉnh sửa hồ sơ", onClick = onEditProfile)
            Spacer(modifier = Modifier.height(10.dp))
            GuideProfileMenuRow(icon = Icons.Default.Notifications, label = "Thông báo", onClick = onNotifications)
            Spacer(modifier = Modifier.height(10.dp))
            GuideProfileMenuRow(icon = Icons.Default.SmartToy, label = "Cài đặt Flora", onClick = onFloraSettings)
            Spacer(modifier = Modifier.height(10.dp))
            GuideProfileMenuRow(icon = Icons.Default.HeadsetMic, label = "Hỗ trợ", onClick = onSupport)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFEBEE),
                    contentColor = Color(0xFFC62828)
                )
            ) {
                Icon(Icons.Default.Logout, null)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Đăng xuất", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun GuideProfileMenuRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = LightGreenBackground) {
                Icon(icon, null, tint = PrimaryGreen, modifier = Modifier.padding(10.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(label, fontSize = 15.sp, color = DarkTextColor, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = SecondaryTextColor)
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = LightGreenBackground) {
                Icon(icon, null, tint = PrimaryGreen, modifier = Modifier.padding(10.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(label, fontSize = 11.sp, color = SecondaryTextColor)
                Text(value, fontSize = 14.sp, color = DarkTextColor, fontWeight = FontWeight.Medium)
            }
        }
    }
}

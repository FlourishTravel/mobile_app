package com.example.flourishtravelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.flourishtravelapp.ui.components.FlourishBottomNavigation
import com.example.flourishtravelapp.ui.screens.*
import com.example.flourishtravelapp.ui.theme.flourishtravelappTheme
import com.example.flourishtravelapp.data.session.SessionManager
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.mapper.toGuideAccount
import com.example.flourishtravelapp.data.model.isTourGuideRole

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            flourishtravelappTheme {
                AppNavigation()
            }
        }
    }
}

enum class NavigationState {
    Login, Register, MainApp, TourDetail, BookingReview, PaymentInfo, BankTransfer, BookingSuccess,
    GuideHome, GuideTourDetail, GuideCustomerList, GuideProfile, Support,
    Activities, ActivityDetail, FloraSettings, GroupChat, Notifications
}

@Composable
fun AppNavigation() {
    var navState by remember { mutableStateOf(NavigationState.MainApp) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var isLoggedIn by remember { mutableStateOf(false) }
    var isGuideLoggedIn by remember { mutableStateOf(false) }
    var selectedCategoryForActivities by remember { mutableStateOf("") }
    var selectedActivitySlug by remember { mutableStateOf<String?>(null) }
    var selectedTourId by remember { mutableStateOf<String?>(null) }
    var selectedSessionId by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    remember {
        RetrofitClient.init(sessionManager)
        true
    }

    var currentGuide by remember { mutableStateOf<GuideAccount?>(null) }
    var selectedGuideTour by remember { mutableStateOf<GuideTour?>(null) }
    var guideHomeTab by remember { mutableIntStateOf(0) }
    var guidePreselectSessionId by remember { mutableStateOf<String?>(null) }
    var guideGroupSubTab by remember { mutableIntStateOf(0) }
    var guideWorkSubTab by remember { mutableIntStateOf(0) }
    var subScreenReturnState by remember { mutableStateOf(NavigationState.MainApp) }
    var subScreenReturnTab by remember { mutableIntStateOf(0) }

    val isAuthenticated = isLoggedIn || isGuideLoggedIn

    var userName by remember { mutableStateOf("Bảo") }
    var userHandle by remember { mutableStateOf("@bao_explorer") }
    var userEmail by remember { mutableStateOf("bao123@flourish.travel") }
    var userPhone by remember { mutableStateOf("+84 901 234 567") }
    var userAddress by remember { mutableStateOf("TP. Hồ Chí Minh, Việt Nam") }
    var notificationEnabled by remember { mutableStateOf(true) }

    var adultCount by remember { mutableIntStateOf(2) }
    var childCount by remember { mutableIntStateOf(1) }
    var bookingName by remember { mutableStateOf("Nguyễn Văn A") }
    var bookingEmail by remember { mutableStateOf("example@mail.com") }
    var bookingPhone by remember { mutableStateOf("090 123 4567") }
    var bookingIdCard by remember { mutableStateOf("012345678901") }
    var bookingGender by remember { mutableStateOf("Nam") }
    var bookingNote by remember { mutableStateOf("") }
    var bookingPaymentMethod by remember { mutableStateOf("Bank Transfer") }
    var chatBookingId by remember { mutableStateOf("") }
    var chatReturnState by remember { mutableStateOf(NavigationState.MainApp) }
    var chatReturnTab by remember { mutableIntStateOf(1) }

    var bookingSessionId by remember { mutableStateOf("") }
    var bookingPromoCode by remember { mutableStateOf<String?>(null) }
    var bookingPromoDiscount by remember { mutableLongStateOf(0L) }
    var createdBookingId by remember { mutableStateOf("") }
    var createdOrderId by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (sessionManager.isLoggedIn()) {
            val user = sessionManager.getUserInfo()
            if (user != null) {
                userName = user.fullName
                userEmail = user.email
                userHandle = "@${user.email.substringBefore("@")}"
                userPhone = user.phone ?: ""
                if (user.role.isTourGuideRole()) {
                    isLoggedIn = false
                    isGuideLoggedIn = true
                    currentGuide = user.toGuideAccount()
                    navState = NavigationState.GuideHome
                } else {
                    isLoggedIn = true
                    navState = NavigationState.MainApp
                    selectedTab = 0
                }
            }
        }
    }

    val onBackToHome = {
        navState = NavigationState.MainApp
        selectedTab = 0
    }
    val onGoToProfile = { selectedTab = 4 }

    val navigateBackFromSubScreen = {
        navState = subScreenReturnState
        when (subScreenReturnState) {
            NavigationState.GuideHome -> guideHomeTab = subScreenReturnTab
            NavigationState.MainApp -> selectedTab = subScreenReturnTab
            else -> Unit
        }
    }

    val guideLogout = {
        sessionManager.clearSession()
        isGuideLoggedIn = false
        isLoggedIn = false
        currentGuide = null
        navState = NavigationState.MainApp
        selectedTab = 0
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (navState == NavigationState.MainApp) {
                FlourishBottomNavigation(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        }
    ) { innerPadding ->
        when (navState) {
            NavigationState.Login -> LoginScreen(
                onLoginSuccess = { user ->
                    isLoggedIn = true
                    userName = user.fullName
                    userEmail = user.email
                    userHandle = "@${user.email.substringBefore("@")}"
                    userPhone = user.phone ?: ""
                    navState = NavigationState.MainApp
                    selectedTab = 0
                },
                onGuideLoginSuccess = { user ->
                    isLoggedIn = false
                    isGuideLoggedIn = true
                    currentGuide = user.toGuideAccount()
                    userName = user.fullName
                    userEmail = user.email
                    userHandle = "@${user.email.substringBefore("@")}"
                    userPhone = user.phone ?: ""
                    navState = NavigationState.GuideHome
                },
                onRegisterClick = { navState = NavigationState.Register },
                onBack = {
                    navState = NavigationState.MainApp
                    selectedTab = 0
                }
            )
            NavigationState.Register -> RegisterScreen(
                onRegisterSuccess = { user ->
                    isLoggedIn = true
                    userName = user.fullName
                    userEmail = user.email
                    userHandle = "@${user.email.substringBefore("@")}"
                    userPhone = user.phone ?: ""
                    navState = NavigationState.MainApp
                    selectedTab = 0
                },
                onLoginClick = { navState = NavigationState.Login },
                onBack = { navState = NavigationState.Login }
            )
            NavigationState.TourDetail -> {
                val tourId = selectedTourId
                if (tourId != null) {
                    TourDetailScreen(
                        tourId = tourId,
                        onBack = { navState = NavigationState.MainApp },
                        onBookNowClick = { sId ->
                            selectedSessionId = sId
                            navState = NavigationState.BookingReview
                        }
                    )
                } else {
                    LaunchedEffect(Unit) { navState = NavigationState.MainApp }
                }
            }
            NavigationState.BookingReview -> BookingReviewScreen(
                initialAdultCount = adultCount,
                initialChildCount = childCount,
                onBack = { navState = NavigationState.TourDetail },
                onProceed = { adults, children, sessionId, promoCode, discount, paymentMethod ->
                    adultCount = adults
                    childCount = children
                    bookingSessionId = sessionId
                    bookingPromoCode = promoCode
                    bookingPromoDiscount = discount
                    bookingPaymentMethod = paymentMethod
                    navState = NavigationState.PaymentInfo
                },
                initialTourId = selectedTourId,
                initialSessionId = selectedSessionId
            )
            NavigationState.PaymentInfo -> PaymentInfoScreen(
                adultCount = adultCount,
                childCount = childCount,
                name = bookingName,
                email = bookingEmail,
                phone = bookingPhone,
                idCard = bookingIdCard,
                gender = bookingGender,
                note = bookingNote,
                onNameChange = { bookingName = it },
                onEmailChange = { bookingEmail = it },
                onPhoneChange = { bookingPhone = it },
                onIdCardChange = { bookingIdCard = it },
                onGenderChange = { bookingGender = it },
                onNoteChange = { bookingNote = it },
                onBack = { navState = NavigationState.BookingReview },
                onContinue = { navState = NavigationState.BankTransfer },
                promoDiscount = bookingPromoDiscount
            )
            NavigationState.BankTransfer -> BankTransferScreen(
                adultCount = adultCount,
                childCount = childCount,
                onBack = { navState = NavigationState.PaymentInfo },
                onComplete = { bookingId, orderId ->
                    createdBookingId = bookingId
                    createdOrderId = orderId
                    navState = NavigationState.BookingSuccess
                },
                sessionId = bookingSessionId,
                promoCode = bookingPromoCode,
                promoDiscount = bookingPromoDiscount,
                bookingName = bookingName,
                bookingEmail = bookingEmail,
                bookingPhone = bookingPhone,
                bookingIdCard = bookingIdCard,
                bookingGender = bookingGender,
                bookingNote = bookingNote,
                paymentMethod = bookingPaymentMethod
            )
            NavigationState.BookingSuccess -> BookingSuccessScreen(
                adultCount = adultCount,
                childCount = childCount,
                name = bookingName,
                email = bookingEmail,
                idCard = bookingIdCard,
                gender = bookingGender,
                onHomeClick = onBackToHome,
                bookingId = createdBookingId,
                orderId = createdOrderId,
                promoDiscount = bookingPromoDiscount
            )
            NavigationState.Activities -> ActivitiesScreen(
                initialCategoryLabel = selectedCategoryForActivities,
                onBack = { navState = NavigationState.MainApp },
                onActivityClick = { activity ->
                    val slug = activity.slug
                    if (!slug.isNullOrBlank()) {
                        selectedActivitySlug = slug
                        navState = NavigationState.ActivityDetail
                    }
                }
            )
            NavigationState.ActivityDetail -> {
                val slug = selectedActivitySlug
                if (slug != null) {
                    ActivityDetailScreen(
                        slug = slug,
                        onBack = { navState = NavigationState.Activities }
                    )
                } else {
                    LaunchedEffect(Unit) { navState = NavigationState.Activities }
                }
            }
            NavigationState.FloraSettings -> {
                if (isAuthenticated) {
                    FloraSettingsScreen(
                        modifier = Modifier.padding(innerPadding),
                        onBack = {
                            if (isGuideLoggedIn && subScreenReturnState == NavigationState.MainApp) {
                                navState = NavigationState.GuideHome
                                guideHomeTab = 4
                            } else {
                                navigateBackFromSubScreen()
                            }
                        }
                    )
                } else {
                    LaunchedEffect(Unit) { navState = NavigationState.Login }
                }
            }
            NavigationState.GuideHome -> {
                val guide = currentGuide
                if (guide != null) {
                    GuideHomeScreen(
                        guide = guide,
                        modifier = Modifier,
                        selectedTab = guideHomeTab.coerceIn(0, 4),
                        onTabSelected = { guideHomeTab = it.coerceIn(0, 4) },
                        initialGuestSessionId = guidePreselectSessionId,
                        initialGroupSubTab = guideGroupSubTab,
                        initialWorkSubTab = guideWorkSubTab,
                        onTourClick = { tour ->
                            selectedGuideTour = tour
                            navState = NavigationState.GuideTourDetail
                        },
                        onGuideUpdated = { currentGuide = it },
                        onOpenGroupChat = { bookingId ->
                            chatBookingId = bookingId
                            chatReturnState = NavigationState.GuideHome
                            navState = NavigationState.GroupChat
                        },
                        onEditProfile = {
                            subScreenReturnState = NavigationState.GuideHome
                            subScreenReturnTab = 4
                            navState = NavigationState.GuideProfile
                        },
                        onNotifications = {
                            subScreenReturnState = NavigationState.GuideHome
                            subScreenReturnTab = 4
                            navState = NavigationState.Notifications
                        },
                        onFloraSettings = {
                            subScreenReturnState = NavigationState.GuideHome
                            subScreenReturnTab = 4
                            navState = NavigationState.FloraSettings
                        },
                        onSupport = {
                            subScreenReturnState = NavigationState.GuideHome
                            subScreenReturnTab = 4
                            navState = NavigationState.Support
                        },
                        onLogout = guideLogout
                    )
                } else {
                    LaunchedEffect(Unit) { navState = NavigationState.Login }
                }
            }
            NavigationState.GuideTourDetail -> {
                val tour = selectedGuideTour
                if (tour != null) {
                    GuideTourDetailScreen(
                        tour = tour,
                        onBack = { navState = NavigationState.GuideHome },
                        onCustomerListClick = {
                            guidePreselectSessionId = tour.sessionId
                            guideGroupSubTab = 0
                            guideHomeTab = 2
                            navState = NavigationState.GuideHome
                        },
                        onOpenGroupChat = { bookingId ->
                            chatBookingId = bookingId
                            chatReturnState = NavigationState.GuideTourDetail
                            navState = NavigationState.GroupChat
                        },
                        onOpenOperations = {
                            guideWorkSubTab = 0
                            guideHomeTab = 3
                            navState = NavigationState.GuideHome
                        },
                        onOpenGuestsTab = {
                            guideGroupSubTab = 0
                            guideHomeTab = 2
                            navState = NavigationState.GuideHome
                        }
                    )
                } else {
                    LaunchedEffect(Unit) { navState = NavigationState.GuideHome }
                }
            }
            NavigationState.GuideCustomerList -> {
                val tour = selectedGuideTour
                if (tour != null) {
                    GuideCustomerListScreen(
                        tour = tour,
                        onBack = { navState = NavigationState.GuideTourDetail }
                    )
                } else {
                    LaunchedEffect(Unit) { navState = NavigationState.GuideHome }
                }
            }
            NavigationState.GuideProfile -> {
                if (isGuideLoggedIn) {
                    ProfileScreen(
                        userName = userName,
                        userHandle = userHandle,
                        userEmail = userEmail,
                        userPhone = userPhone,
                        userAddress = userAddress,
                        notificationEnabled = notificationEnabled,
                        onProfileUpdate = { name, handle, email, phone, address, notify ->
                            userName = name
                            userHandle = handle
                            userEmail = email
                            userPhone = phone
                            userAddress = address
                            notificationEnabled = notify
                        },
                        modifier = Modifier.padding(innerPadding),
                        onBack = {
                            navState = NavigationState.GuideHome
                            guideHomeTab = 4
                        },
                        onLogout = guideLogout,
                        onFloraSettingsClick = {
                            subScreenReturnState = NavigationState.GuideProfile
                            subScreenReturnTab = 4
                            navState = NavigationState.FloraSettings
                        },
                        onNotificationsClick = {
                            subScreenReturnState = NavigationState.GuideProfile
                            subScreenReturnTab = 4
                            navState = NavigationState.Notifications
                        }
                    )
                } else {
                    LaunchedEffect(Unit) { navState = NavigationState.Login }
                }
            }
            NavigationState.Support -> {
                if (isAuthenticated) {
                    SupportScreen(
                        modifier = Modifier.padding(innerPadding),
                        onBack = {
                            if (isGuideLoggedIn && subScreenReturnState == NavigationState.MainApp) {
                                navState = NavigationState.GuideHome
                                guideHomeTab = 4
                            } else {
                                navigateBackFromSubScreen()
                            }
                        },
                        onLogout = {
                            if (isGuideLoggedIn) {
                                guideLogout()
                            } else {
                                sessionManager.clearSession()
                                isLoggedIn = false
                                navState = NavigationState.MainApp
                                selectedTab = 0
                            }
                        }
                    )
                } else {
                    LaunchedEffect(Unit) { navState = NavigationState.Login }
                }
            }
            NavigationState.GroupChat -> GroupCommunicationScreen(
                bookingId = chatBookingId,
                onBack = {
                    navState = chatReturnState
                    if (chatReturnState == NavigationState.MainApp) {
                        selectedTab = chatReturnTab
                    }
                }
            )
            NavigationState.Notifications -> NotificationsScreen(
                onBack = {
                    if (isGuideLoggedIn && subScreenReturnState == NavigationState.MainApp) {
                        navState = NavigationState.GuideHome
                        guideHomeTab = 4
                    } else {
                        navigateBackFromSubScreen()
                    }
                }
            )
            NavigationState.MainApp -> {
                when (selectedTab) {
                    0 -> HomepageScreen(
                        userName = if (isLoggedIn) userName else "Khách",
                        modifier = Modifier.padding(innerPadding),
                        onBack = { },
                        onTourClick = { tourId ->
                            if (!isLoggedIn) {
                                navState = NavigationState.Login
                            } else {
                                selectedTourId = tourId
                                navState = NavigationState.TourDetail
                            }
                        },
                        onAssistantClick = {
                            if (!isLoggedIn) {
                                navState = NavigationState.Login
                            } else {
                                selectedTab = 2
                            }
                        },
                        onProfileClick = onGoToProfile,
                        onCategoryClick = { category ->
                            selectedCategoryForActivities = category
                            navState = NavigationState.Activities
                        }
                    )
                    1 -> {
                        if (isLoggedIn) {
                            ScheduleScreen(
                                modifier = Modifier.padding(innerPadding),
                                onBack = onBackToHome,
                                onProfileClick = onGoToProfile,
                                onOpenGroupChat = { id ->
                                    chatBookingId = id
                                    chatReturnState = NavigationState.MainApp
                                    chatReturnTab = 1
                                    navState = NavigationState.GroupChat
                                }
                            )
                        } else {
                            LaunchedEffect(Unit) { navState = NavigationState.Login }
                        }
                    }
                    2 -> {
                        if (isLoggedIn) {
                            AssistantScreen(
                                modifier = Modifier.padding(innerPadding),
                                onBack = onBackToHome,
                                onProfileClick = onGoToProfile
                            )
                        } else {
                            LaunchedEffect(Unit) { navState = NavigationState.Login }
                        }
                    }
                    3 -> ExploreScreen(
                        modifier = Modifier.padding(innerPadding),
                        onBack = onBackToHome,
                        onProfileClick = onGoToProfile,
                        onTourDetailClick = { tourId ->
                            selectedTourId = tourId
                            navState = NavigationState.TourDetail
                        }
                    )
                    4 -> {
                        if (isLoggedIn) {
                            ProfileScreen(
                                userName = userName,
                                userHandle = userHandle,
                                userEmail = userEmail,
                                userPhone = userPhone,
                                userAddress = userAddress,
                                notificationEnabled = notificationEnabled,
                                onProfileUpdate = { name, handle, email, phone, address, notify ->
                                    userName = name
                                    userHandle = handle
                                    userEmail = email
                                    userPhone = phone
                                    userAddress = address
                                    notificationEnabled = notify
                                },
                                modifier = Modifier.padding(innerPadding),
                                onBack = onBackToHome,
                                onLogout = {
                                    sessionManager.clearSession()
                                    isLoggedIn = false
                                    selectedTab = 0
                                },
                                onFloraSettingsClick = {
                                    subScreenReturnState = NavigationState.MainApp
                                    subScreenReturnTab = 4
                                    navState = NavigationState.FloraSettings
                                },
                                onNotificationsClick = {
                                    subScreenReturnState = NavigationState.MainApp
                                    subScreenReturnTab = 4
                                    navState = NavigationState.Notifications
                                }
                            )
                        } else {
                            LaunchedEffect(Unit) { navState = NavigationState.Login }
                        }
                    }
                }
            }
        }
    }
}

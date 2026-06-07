package com.example.flourishtavelapp

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
import com.example.flourishtavelapp.ui.components.FlourishBottomNavigation
import com.example.flourishtavelapp.ui.screens.*
import com.example.flourishtavelapp.ui.theme.FlourishTavelAppTheme
import com.example.flourishtavelapp.data.session.SessionManager
import com.example.flourishtavelapp.data.api.RetrofitClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlourishTavelAppTheme {
                AppNavigation()
            }
        }
    }
}

enum class NavigationState {
    Login, Register, MainApp, TourDetail, BookingReview, PaymentInfo, BankTransfer, BookingSuccess,
    GuideHome, GuideTourDetail, GuideCustomerList, Activities
}

@Composable
fun AppNavigation() {
    var navState by remember { mutableStateOf(NavigationState.MainApp) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var isLoggedIn by remember { mutableStateOf(false) }
    var isGuideLoggedIn by remember { mutableStateOf(false) }
    var selectedCategoryForActivities by remember { mutableStateOf("") }
    var selectedTourId by remember { mutableStateOf<String?>(null) }
    var selectedSessionId by remember { mutableStateOf<String?>(null) }

    // SharedPreferences Session Manager
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // Initialize Retrofit Client
    remember {
        RetrofitClient.init(sessionManager)
        true
    }

    // Guide State
    val currentGuide = com.example.flourishtavelapp.ui.screens.mockGuideAccounts.first()
    var selectedGuideTour by remember { mutableStateOf<com.example.flourishtavelapp.ui.screens.GuideTour?>(null) }
    
    // User Profile State
    var userName by remember { mutableStateOf("Bảo") }
    var userHandle by remember { mutableStateOf("@bao_explorer") }
    var userEmail by remember { mutableStateOf("bao123@flourish.travel") }
    var userPhone by remember { mutableStateOf("+84 901 234 567") }
    var userAddress by remember { mutableStateOf("TP. Hồ Chí Minh, Việt Nam") }
    var notificationEnabled by remember { mutableStateOf(true) }

    // Booking State
    var adultCount by remember { mutableIntStateOf(2) }
    var childCount by remember { mutableIntStateOf(1) }
    var bookingName by remember { mutableStateOf("Nguyễn Văn A") }
    var bookingEmail by remember { mutableStateOf("example@mail.com") }
    var bookingPhone by remember { mutableStateOf("090 123 4567") }
    var bookingIdCard by remember { mutableStateOf("012345678901") }
    var bookingGender by remember { mutableStateOf("Nam") }
    var bookingNote by remember { mutableStateOf("") }

    // Backend booking response and session details
    var bookingSessionId by remember { mutableStateOf("") }
    var bookingPromoCode by remember { mutableStateOf<String?>(null) }
    var bookingPromoDiscount by remember { mutableLongStateOf(0L) }
    var createdBookingId by remember { mutableStateOf("") }
    var createdOrderId by remember { mutableStateOf("") }

    // Auto-Login: Check if token exists on app launch
    LaunchedEffect(Unit) {
        if (sessionManager.isLoggedIn()) {
            val user = sessionManager.getUserInfo()
            if (user != null) {
                userName = user.fullName
                userEmail = user.email
                userHandle = "@${user.email.substringBefore("@")}"
                userPhone = user.phone ?: ""
                if (user.role.equals("GUIDE", ignoreCase = true)) {
                    isGuideLoggedIn = true
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
                    isGuideLoggedIn = true
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
                onProceed = { adults, children, sessionId, promoCode, discount ->
                    adultCount = adults
                    childCount = children
                    bookingSessionId = sessionId
                    bookingPromoCode = promoCode
                    bookingPromoDiscount = discount
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
                bookingNote = bookingNote
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
                onActivityClick = { navState = NavigationState.TourDetail }
            )
            // ── Guide Screens ──────────────────────────────────────────────
            NavigationState.GuideHome -> GuideHomeScreen(
                guide = currentGuide,
                modifier = Modifier,
                onTourClick = { tour ->
                    selectedGuideTour = tour
                    navState = NavigationState.GuideTourDetail
                },
                onLogout = {
                    sessionManager.clearSession()
                    isGuideLoggedIn = false
                    navState = NavigationState.MainApp
                    selectedTab = 0
                }
            )
            NavigationState.GuideTourDetail -> {
                val tour = selectedGuideTour
                if (tour != null) {
                    GuideTourDetailScreen(
                        tour = tour,
                        onBack = { navState = NavigationState.GuideHome },
                        onCustomerListClick = { navState = NavigationState.GuideCustomerList }
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
                                onProfileClick = onGoToProfile
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

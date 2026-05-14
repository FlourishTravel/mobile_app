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
import com.example.flourishtavelapp.ui.components.FlourishBottomNavigation
import com.example.flourishtavelapp.ui.screens.*
import com.example.flourishtavelapp.ui.theme.FlourishTavelAppTheme

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
    GuideHome, GuideTourDetail, GuideCustomerList
}

@Composable
fun AppNavigation() {
    var navState by remember { mutableStateOf(NavigationState.MainApp) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var isLoggedIn by remember { mutableStateOf(false) }
    var isGuideLoggedIn by remember { mutableStateOf(false) }

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
                onLoginSuccess = {
                    isLoggedIn = true
                    navState = NavigationState.MainApp
                },
                onGuideLoginSuccess = {
                    isGuideLoggedIn = true
                    navState = NavigationState.GuideHome
                },
                onRegisterClick = { navState = NavigationState.Register },
                onBack = { 
                    navState = NavigationState.MainApp
                    selectedTab = 0 
                }
            )
            NavigationState.Register -> RegisterScreen(
                onRegisterSuccess = {
                    isLoggedIn = true
                    navState = NavigationState.MainApp
                },
                onLoginClick = { navState = NavigationState.Login },
                onBack = { navState = NavigationState.Login }
            )
            NavigationState.TourDetail -> TourDetailScreen(
                onBack = { navState = NavigationState.MainApp },
                onBookNowClick = { navState = NavigationState.BookingReview }
            )
            NavigationState.BookingReview -> BookingReviewScreen(
                initialAdultCount = adultCount,
                initialChildCount = childCount,
                onBack = { navState = NavigationState.TourDetail },
                onProceed = { adults, children ->
                    adultCount = adults
                    childCount = children
                    navState = NavigationState.PaymentInfo
                }
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
                onContinue = { navState = NavigationState.BankTransfer }
            )
            NavigationState.BankTransfer -> BankTransferScreen(
                adultCount = adultCount,
                childCount = childCount,
                onBack = { navState = NavigationState.PaymentInfo },
                onComplete = { navState = NavigationState.BookingSuccess }
            )
            NavigationState.BookingSuccess -> BookingSuccessScreen(
                adultCount = adultCount,
                childCount = childCount,
                name = bookingName,
                email = bookingEmail,
                idCard = bookingIdCard,
                gender = bookingGender,
                onHomeClick = onBackToHome
            )
            // ── Guide Screens ──────────────────────────────────────────────
            NavigationState.GuideHome -> GuideHomeScreen(
                guide = currentGuide,
                modifier = Modifier.padding(innerPadding),
                onTourClick = { tour ->
                    selectedGuideTour = tour
                    navState = NavigationState.GuideTourDetail
                },
                onLogout = {
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
                        onTourClick = {
                            if (!isLoggedIn) {
                                navState = NavigationState.Login
                            } else {
                                selectedTab = 1
                            }
                        },
                        onAssistantClick = {
                            if (!isLoggedIn) {
                                navState = NavigationState.Login
                            } else {
                                selectedTab = 2
                            }
                        },
                        onProfileClick = onGoToProfile
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
                        onTourDetailClick = { navState = NavigationState.TourDetail }
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

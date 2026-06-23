package com.example.flourishtavelapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.data.api.RetrofitClient
import com.example.flourishtavelapp.data.model.TravelPreferencesDto
import com.example.flourishtavelapp.data.preferences.FloraPreferencesMapper
import com.example.flourishtavelapp.data.repository.FloraPreferenceRepository
import com.example.flourishtavelapp.data.repository.PreferenceLoadResult
import com.example.flourishtavelapp.data.repository.PreferenceSaveResult
import com.example.flourishtavelapp.data.session.SessionManager
import com.example.flourishtavelapp.location.LocationPermissionHelper
import com.example.flourishtavelapp.ui.theme.NatureGreenBackground
import com.example.flourishtavelapp.ui.theme.PrimaryGreen
import com.example.flourishtavelapp.ui.theme.SecondaryTextColor
import kotlinx.coroutines.launch

private val TRAVEL_STYLE_OPTIONS = listOf(
    "Biển", "Văn hóa", "Ẩm thực", "Phiêu lưu", "Nghỉ dưỡng", "Mua sắm"
)
private val BUDGET_OPTIONS = listOf("low", "medium", "high")
private val PACE_OPTIONS = listOf("chậm", "vừa phải", "nhanh")

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FloraSettingsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val repository = remember { FloraPreferenceRepository(RetrofitClient.floraApiService) }
    val scope = rememberCoroutineScope()

    var screenState by remember { mutableStateOf<FloraSettingsUiState>(FloraSettingsUiState.Loading) }
    var draft by remember { mutableStateOf(FloraPreferencesMapper.defaults()) }
    var saving by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var statusIsError by remember { mutableStateOf(false) }

  // Text fields for comma-separated lists
    var favoriteDestinationsText by remember { mutableStateOf("") }
    var favoriteFoodsText by remember { mutableStateOf("") }
    var foodDislikesText by remember { mutableStateOf("") }
    var foodAllergiesText by remember { mutableStateOf("") }
    var preferredActivitiesText by remember { mutableStateOf("") }
    var avoidedActivitiesText by remember { mutableStateOf("") }

    fun applyDraftToTexts(prefs: TravelPreferencesDto) {
        favoriteDestinationsText = FloraPreferencesMapper.joinCommaList(prefs.favoriteDestinations)
        favoriteFoodsText = FloraPreferencesMapper.joinCommaList(prefs.favoriteFoods)
        foodDislikesText = FloraPreferencesMapper.joinCommaList(prefs.foodDislikes)
        foodAllergiesText = FloraPreferencesMapper.joinCommaList(prefs.foodAllergies)
        preferredActivitiesText = FloraPreferencesMapper.joinCommaList(prefs.preferredActivities)
        avoidedActivitiesText = FloraPreferencesMapper.joinCommaList(prefs.avoidedActivities)
    }

    fun buildDraftFromUi(): TravelPreferencesDto = draft.copy(
        favoriteDestinations = FloraPreferencesMapper.parseCommaList(favoriteDestinationsText),
        favoriteFoods = FloraPreferencesMapper.parseCommaList(favoriteFoodsText),
        foodDislikes = FloraPreferencesMapper.parseCommaList(foodDislikesText),
        foodAllergies = FloraPreferencesMapper.parseCommaList(foodAllergiesText),
        preferredActivities = FloraPreferencesMapper.parseCommaList(preferredActivitiesText),
        avoidedActivities = FloraPreferencesMapper.parseCommaList(avoidedActivitiesText)
    )

    LaunchedEffect(Unit) {
        if (!sessionManager.isLoggedIn()) {
            screenState = FloraSettingsUiState.Unauthorized
            return@LaunchedEffect
        }
        screenState = FloraSettingsUiState.Loading
        when (val result = repository.loadPreferences()) {
            is PreferenceLoadResult.Success -> {
                draft = result.data
                applyDraftToTexts(result.data)
                screenState = FloraSettingsUiState.Loaded
            }
            is PreferenceLoadResult.Unauthorized ->
                screenState = FloraSettingsUiState.Unauthorized
            is PreferenceLoadResult.Error ->
                screenState = FloraSettingsUiState.Error(result.message)
        }
    }

    val deviceLocationGranted = LocationPermissionHelper.hasForegroundLocationPermission(context)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Cài đặt Flora AI") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NatureGreenBackground
                )
            )
        }
    ) { padding ->
        when (val state = screenState) {
            FloraSettingsUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = PrimaryGreen)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Đang tải cài đặt Flora...", color = SecondaryTextColor)
                }
            }
            FloraSettingsUiState.Unauthorized -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Bạn cần đăng nhập để quản lý cài đặt Flora AI.",
                        color = Color(0xFF92400E)
                    )
                }
            }
            is FloraSettingsUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(state.message, color = Color(0xFF92400E))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        scope.launch {
                            screenState = FloraSettingsUiState.Loading
                            when (val result = repository.loadPreferences()) {
                                is PreferenceLoadResult.Success -> {
                                    draft = result.data
                                    applyDraftToTexts(result.data)
                                    screenState = FloraSettingsUiState.Loaded
                                }
                                is PreferenceLoadResult.Unauthorized ->
                                    screenState = FloraSettingsUiState.Unauthorized
                                is PreferenceLoadResult.Error ->
                                    screenState = FloraSettingsUiState.Error(result.message)
                            }
                        }
                    }) { Text("Thử lại") }
                }
            }
            FloraSettingsUiState.Loaded -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Bạn có thể kiểm soát cách Flora sử dụng thông tin để hỗ trợ chuyến đi của mình.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryTextColor
                    )

                    PrivacyInfoCard(deviceLocationGranted = deviceLocationGranted)

                    SettingsSection(title = "Quyền riêng tư Flora") {
                        ConsentSwitchRow(
                            title = "Cá nhân hóa gợi ý",
                            description = "Cho phép Flora dùng sở thích du lịch và lịch sử tour trong hệ thống để đưa ra gợi ý phù hợp hơn.",
                            checked = draft.personalizationConsent == true,
                            onCheckedChange = { draft = draft.copy(personalizationConsent = it) }
                        )
                        ConsentSwitchRow(
                            title = "Chia sẻ vị trí khi bạn yêu cầu",
                            description = "Flora chỉ sử dụng vị trí khi bạn bấm \"Gợi ý gần đây\" để đề xuất địa điểm phù hợp. Ứng dụng không theo dõi vị trí nền.",
                            checked = draft.locationConsent == true,
                            onCheckedChange = { draft = draft.copy(locationConsent = it) }
                        )
                        ConsentSwitchRow(
                            title = "Nhận thông báo từ Flora",
                            description = "Nhận nhắc giờ tập trung, thay đổi lịch trình và thông báo quan trọng trong chuyến đi (trong ứng dụng).",
                            checked = draft.notificationConsent == true,
                            onCheckedChange = { draft = draft.copy(notificationConsent = it) }
                        )
                    }

                    SettingsSection(title = "Sở thích du lịch") {
                        val isEmpty = draft.travelStyles.isNullOrEmpty() &&
                            draft.budgetLevel.isNullOrBlank() &&
                            favoriteDestinationsText.isBlank()
                        if (isEmpty) {
                            Text(
                                "Bạn có thể thêm sở thích để Flora gợi ý tour và địa điểm phù hợp hơn.",
                                style = MaterialTheme.typography.bodySmall,
                                color = SecondaryTextColor
                            )
                        }

                        Text("Phong cách du lịch", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TRAVEL_STYLE_OPTIONS.forEach { style ->
                                val selected = draft.travelStyles?.contains(style) == true
                                FilterChip(
                                    selected = selected,
                                    onClick = {
                                        val current = draft.travelStyles.orEmpty().toMutableList()
                                        if (selected) current.remove(style) else current.add(style)
                                        draft = draft.copy(travelStyles = current)
                                    },
                                    label = { Text(style) }
                                )
                            }
                        }

                        Text("Ngân sách dự kiến", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            BUDGET_OPTIONS.forEach { level ->
                                FilterChip(
                                    selected = draft.budgetLevel == level,
                                    onClick = { draft = draft.copy(budgetLevel = level) },
                                    label = { Text(level) }
                                )
                            }
                        }

                        CommaListField(
                            label = "Điểm đến yêu thích",
                            value = favoriteDestinationsText,
                            onValueChange = { favoriteDestinationsText = it },
                            placeholder = "Đà Lạt, Phú Quốc..."
                        )
                        CommaListField(
                            label = "Món ăn yêu thích",
                            value = favoriteFoodsText,
                            onValueChange = { favoriteFoodsText = it },
                            placeholder = "hải sản, cà phê..."
                        )
                        CommaListField(
                            label = "Món không thích",
                            value = foodDislikesText,
                            onValueChange = { foodDislikesText = it },
                            placeholder = "món cay..."
                        )

                        Text(
                            "Dị ứng thực phẩm",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Text(
                            "Thông tin này chỉ giúp Flora hạn chế gợi ý không phù hợp, không thay thế việc kiểm tra trực tiếp với nhà hàng.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF92400E)
                        )
                        CommaListField(
                            label = "Dị ứng / cần tránh",
                            value = foodAllergiesText,
                            onValueChange = { foodAllergiesText = it },
                            placeholder = "đậu phộng, hải sản..."
                        )

                        CommaListField(
                            label = "Hoạt động yêu thích",
                            value = preferredActivitiesText,
                            onValueChange = { preferredActivitiesText = it },
                            placeholder = "tắm biển, tham quan..."
                        )
                        CommaListField(
                            label = "Hoạt động muốn tránh",
                            value = avoidedActivitiesText,
                            onValueChange = { avoidedActivitiesText = it },
                            placeholder = "leo núi..."
                        )

                        Text("Nhịp đi du lịch", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PACE_OPTIONS.forEach { pace ->
                                FilterChip(
                                    selected = draft.travelPace == pace,
                                    onClick = { draft = draft.copy(travelPace = pace) },
                                    label = { Text(pace) }
                                )
                            }
                        }

                        ConsentSwitchRow(
                            title = "Đi cùng trẻ em",
                            description = null,
                            checked = draft.travelingWithChildren == true,
                            onCheckedChange = { draft = draft.copy(travelingWithChildren = it) }
                        )
                        ConsentSwitchRow(
                            title = "Đi cùng người lớn tuổi",
                            description = null,
                            checked = draft.travelingWithElderly == true,
                            onCheckedChange = { draft = draft.copy(travelingWithElderly = it) }
                        )
                    }

                    statusMessage?.let { msg ->
                        Text(
                            msg,
                            color = if (statusIsError) Color(0xFF92400E) else Color(0xFF065F46),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                saving = true
                                statusMessage = null
                                val toSave = buildDraftFromUi()
                                when (val result = repository.saveDraft(toSave)) {
                                    is PreferenceSaveResult.Success -> {
                                        draft = result.data
                                        applyDraftToTexts(result.data)
                                        statusIsError = false
                                        statusMessage = "Đã lưu cài đặt Flora AI."
                                    }
                                    is PreferenceSaveResult.Unauthorized -> {
                                        statusIsError = true
                                        statusMessage = "Bạn cần đăng nhập để quản lý cài đặt Flora AI."
                                    }
                                    is PreferenceSaveResult.Error -> {
                                        statusIsError = true
                                        statusMessage = result.message
                                    }
                                }
                                saving = false
                            }
                        },
                        enabled = !saving,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (saving) "Đang lưu..." else "Lưu cài đặt")
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

private sealed class FloraSettingsUiState {
    data object Loading : FloraSettingsUiState()
    data object Loaded : FloraSettingsUiState()
    data object Unauthorized : FloraSettingsUiState()
    data class Error(val message: String) : FloraSettingsUiState()
}

@Composable
private fun PrivacyInfoCard(deviceLocationGranted: Boolean) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFECFDF5),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Quyền riêng tư của bạn", fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
            Text(
                "Flora chỉ dùng vị trí khi bạn cho phép và khi bạn chủ động yêu cầu gợi ý gần đây. Ứng dụng không theo dõi vị trí nền.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF047857)
            )
            Text(
                "Quyền vị trí trên thiết bị: ${if (deviceLocationGranted) "Đã cho phép" else "Chưa cho phép"}",
                style = MaterialTheme.typography.bodySmall,
                color = SecondaryTextColor
            )
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            content()
        }
    }
}

@Composable
private fun ConsentSwitchRow(
    title: String,
    description: String?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            description?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = SecondaryTextColor)
            }
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun CommaListField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = false,
        minLines = 1,
        maxLines = 3
    )
}

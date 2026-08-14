package com.example.flourishtravelapp.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flourishtravelapp.data.api.FileUtils
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.mapper.toGuideAccount
import com.example.flourishtravelapp.data.model.GUIDE_LANGUAGE_OPTIONS
import com.example.flourishtravelapp.data.model.GUIDE_SPECIALTY_OPTIONS
import com.example.flourishtravelapp.data.model.UpdateProfileRequest
import com.example.flourishtravelapp.data.model.UserInfo
import com.example.flourishtravelapp.data.model.genderUiToBe
import com.example.flourishtravelapp.data.model.toggleChip
import com.example.flourishtravelapp.data.session.SessionManager
import com.example.flourishtravelapp.ui.theme.DarkTextColor
import com.example.flourishtravelapp.ui.theme.NatureGreenBackground
import com.example.flourishtravelapp.ui.theme.PrimaryGreen
import com.example.flourishtravelapp.ui.theme.SecondaryTextColor
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GuideEditProfileScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onSaved: (GuideAccount) -> Unit = {}
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()

    var loading by remember { mutableStateOf(true) }
    var saving by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Nam") }
    var email by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }
    var coverUrl by remember { mutableStateOf("") }
    var baseLocation by remember { mutableStateOf("") }
    var experienceYears by remember { mutableStateOf("1") }
    var shortBio by remember { mutableStateOf("") }
    var fullBio by remember { mutableStateOf("") }
    var languages by remember { mutableStateOf(listOf("Tiếng Việt")) }
    var specialties by remember { mutableStateOf(emptyList<String>()) }
    var badges by remember { mutableStateOf(emptyList<String>()) }
    var verified by remember { mutableStateOf(false) }
    var publicApproved by remember { mutableStateOf(false) }
    var pendingReview by remember { mutableStateOf(false) }
    var genderMenuOpen by remember { mutableStateOf(false) }
    var pendingAvatarUri by remember { mutableStateOf<Uri?>(null) }
    var pendingCoverUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        loading = true
        errorMsg = null
        try {
            val response = RetrofitClient.userApiService.getProfile()
            val profile = response.body()?.data
            if (response.isSuccessful && response.body()?.success == true && profile != null) {
                val account = profile.toGuideAccount()
                fullName = account.name
                phone = account.phone
                address = account.address
                gender = account.gender
                email = profile.email
                jobTitle = account.jobTitle
                avatarUrl = account.avatarUrl.orEmpty()
                coverUrl = account.coverUrl.orEmpty()
                baseLocation = account.baseLocation
                experienceYears = (account.experienceYears ?: 1).toString()
                shortBio = account.shortBio
                fullBio = account.fullBio
                languages = account.languages.ifEmpty { listOf("Tiếng Việt") }
                specialties = account.specialties
                badges = account.badges
                verified = account.verified
                publicApproved = account.publicApproved
                pendingReview = account.pendingReview
                sessionManager.updateUserInfo(
                    UserInfo(
                        id = profile.id,
                        email = profile.email,
                        fullName = profile.fullName,
                        role = profile.role,
                        avatarUrl = profile.avatarUrl,
                        phone = profile.phone
                    )
                )
            } else {
                errorMsg = response.body()?.message ?: "Không tải được hồ sơ HDV."
            }
        } catch (e: Exception) {
            errorMsg = e.localizedMessage ?: "Lỗi kết nối."
        } finally {
            loading = false
        }
    }

    val avatarPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) pendingAvatarUri = uri
    }
    val coverPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) pendingCoverUri = uri
    }

    val statusNote = when {
        publicApproved && pendingReview -> "Hồ sơ đang hiện trên Đội ngũ HDV. Bản sửa vừa lưu chờ admin xem lại."
        publicApproved -> "Hồ sơ đã được duyệt và đang hiện trên Khám phá → Đội ngũ HDV."
        pendingReview -> "Đã gửi admin. Chưa duyệt nên trang khách chưa hiện hồ sơ này."
        else -> "Điền bio, ngôn ngữ, chuyên môn rồi Lưu. Admin duyệt xong mới hiện trên trang khách."
    }

    suspend fun uploadImage(uri: Uri): String? {
        val file = FileUtils.uriToFile(context, uri) ?: return null
        val body = MultipartBody.Part.createFormData(
            "file",
            file.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )
        val upload = RetrofitClient.uploadApiService.uploadFile(body)
        return if (upload.isSuccessful && upload.body()?.success == true) upload.body()?.data else null
    }

    fun save() {
        if (fullName.isBlank()) {
            errorMsg = "Tên không được để trống."
            return
        }
        scope.launch {
            saving = true
            errorMsg = null
            try {
                var nextAvatar = avatarUrl
                var nextCover = coverUrl
                pendingAvatarUri?.let { uri ->
                    nextAvatar = uploadImage(uri) ?: throw IllegalStateException("Không tải được ảnh đại diện.")
                }
                pendingCoverUri?.let { uri ->
                    nextCover = uploadImage(uri) ?: throw IllegalStateException("Không tải được ảnh bìa.")
                }
                val years = experienceYears.toIntOrNull()?.coerceIn(0, 50) ?: 0
                val response = RetrofitClient.userApiService.updateProfile(
                    UpdateProfileRequest(
                        fullName = fullName.trim(),
                        phone = phone.trim(),
                        avatarUrl = nextAvatar.ifBlank { null },
                        gender = genderUiToBe(gender),
                        address = address.trim(),
                        guideShortBio = shortBio.trim(),
                        guideBio = fullBio.trim(),
                        guideLanguages = languages,
                        guideSpecialties = specialties,
                        guideCoverUrl = nextCover,
                        guideExperienceYears = years,
                        guideBaseLocation = baseLocation.trim()
                    )
                )
                val profile = response.body()?.data
                if (response.isSuccessful && response.body()?.success == true && profile != null) {
                    val account = profile.toGuideAccount()
                    sessionManager.updateUserInfo(
                        UserInfo(
                            id = profile.id,
                            email = profile.email,
                            fullName = profile.fullName,
                            role = profile.role,
                            avatarUrl = profile.avatarUrl,
                            phone = profile.phone
                        )
                    )
                    onSaved(account)
                    Toast.makeText(context, "Đã lưu hồ sơ. Admin duyệt phần công khai trước khi hiện trên trang khách.", Toast.LENGTH_LONG).show()
                    onBack()
                } else {
                    errorMsg = response.body()?.message ?: "Không lưu được hồ sơ."
                }
            } catch (e: Exception) {
                errorMsg = e.localizedMessage ?: "Lỗi kết nối."
            } finally {
                saving = false
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = NatureGreenBackground,
        topBar = {
            TopAppBar(
                title = { Text("Hồ sơ công khai HDV", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { inner ->
        if (loading) {
            Box(Modifier.fillMaxSize().padding(inner), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (publicApproved) Color(0xFFECFDF5) else Color(0xFFFFFBEB)
            ) {
                Text(
                    statusNote,
                    modifier = Modifier.padding(12.dp),
                    color = if (publicApproved) Color(0xFF047857) else Color(0xFF92400E),
                    fontSize = 13.sp
                )
            }
            errorMsg?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = Color(0xFFC62828), fontSize = 13.sp)
            }

            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F5E9))
                        .clickable { avatarPicker.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    val avatarSrc = pendingAvatarUri?.toString() ?: avatarUrl
                    if (avatarSrc.isNotBlank()) {
                        AsyncImage(avatarSrc, contentDescription = "Ảnh đại diện", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        Icon(Icons.Default.Person, null, tint = PrimaryGreen, modifier = Modifier.size(36.dp))
                    }
                }
                Spacer(Modifier.size(14.dp))
                Column {
                    Text(fullName.ifBlank { "Hướng dẫn viên" }, fontWeight = FontWeight.Bold, color = DarkTextColor)
                    Text("$jobTitle · $email", fontSize = 12.sp, color = SecondaryTextColor)
                    if (verified) Text("Đã xác minh", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                    if (badges.isNotEmpty()) Text(badges.joinToString(" · "), fontSize = 12.sp, color = SecondaryTextColor)
                    Text("Đổi ảnh đại diện", fontSize = 12.sp, color = PrimaryGreen, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(fullName, { fullName = it }, label = { Text("Họ và tên") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(phone, { phone = it }, label = { Text("Số điện thoại") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
            Spacer(Modifier.height(8.dp))
            ExposedDropdownMenuBox(expanded = genderMenuOpen, onExpandedChange = { genderMenuOpen = it }) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Giới tính") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderMenuOpen) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = genderMenuOpen, onDismissRequest = { genderMenuOpen = false }) {
                    listOf("Nam", "Nữ", "Khác").forEach { option ->
                        DropdownMenuItem(text = { Text(option) }, onClick = { gender = option; genderMenuOpen = false })
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(email, {}, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true, enabled = false)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(address, { address = it }, label = { Text("Địa chỉ liên hệ (nội bộ)") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(20.dp))
            Text("Hồ sơ công khai", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkTextColor)
            Text("Chức danh và huy hiệu do admin gán. Bạn tự viết bio, ngôn ngữ, chuyên môn, tuyến phụ trách.", fontSize = 12.sp, color = SecondaryTextColor)

            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF3F4F6))
                    .clickable { coverPicker.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                val coverSrc = pendingCoverUri?.toString() ?: coverUrl
                if (coverSrc.isNotBlank()) {
                    AsyncImage(coverSrc, contentDescription = "Ảnh bìa", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CameraAlt, null, tint = SecondaryTextColor)
                        Spacer(Modifier.size(8.dp))
                        Text("Tải ảnh bìa", color = SecondaryTextColor)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            OutlinedTextField(baseLocation, { baseLocation = it }, label = { Text("Tuyến / vùng phụ trách") }, placeholder = { Text("Bangkok – Pattaya, Thái Lan") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                experienceYears,
                { experienceYears = it.filter { ch -> ch.isDigit() }.take(2) },
                label = { Text("Số năm kinh nghiệm") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(shortBio, { if (it.length <= 280) shortBio = it }, label = { Text("Bio ngắn (card danh sách)") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(fullBio, { if (it.length <= 4000) fullBio = it }, label = { Text("Giới thiệu đầy đủ") }, modifier = Modifier.fillMaxWidth(), minLines = 4)

            Spacer(Modifier.height(12.dp))
            Text("Ngôn ngữ", fontWeight = FontWeight.SemiBold, color = DarkTextColor)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GUIDE_LANGUAGE_OPTIONS.forEach { item ->
                    FilterChip(
                        selected = languages.contains(item),
                        onClick = { languages = toggleChip(languages, item) },
                        label = { Text(item) }
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("Chuyên môn", fontWeight = FontWeight.SemiBold, color = DarkTextColor)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GUIDE_SPECIALTY_OPTIONS.forEach { item ->
                    FilterChip(
                        selected = specialties.contains(item),
                        onClick = { specialties = toggleChip(specialties, item) },
                        label = { Text(item) }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { save() },
                enabled = !saving,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                if (saving) CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                else Text("Lưu hồ sơ", fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

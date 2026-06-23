package com.example.flourishtavelapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flourishtavelapp.data.api.RetrofitClient
import com.example.flourishtavelapp.data.model.FloraFeedbackTagDto
import com.example.flourishtavelapp.data.model.FloraPostTourFeedbackContextDto
import com.example.flourishtavelapp.data.model.FloraPreferencePreviewDto
import com.example.flourishtavelapp.data.repository.FeedbackLoadResult
import com.example.flourishtavelapp.data.repository.FeedbackPreviewResult
import com.example.flourishtavelapp.data.repository.FeedbackSubmitResult
import com.example.flourishtavelapp.data.repository.FloraPostTourFeedbackRepository
import com.example.flourishtavelapp.data.repository.FloraPreferenceRepository
import com.example.flourishtavelapp.data.repository.PreferenceSaveResult
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FloraPostTourFeedbackPanel(
    bookingId: String,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val repository = remember {
        FloraPostTourFeedbackRepository(
            RetrofitClient.floraApiService,
            RetrofitClient.reviewApiService,
            FloraPreferenceRepository(RetrofitClient.floraApiService)
        )
    }

    var loading by remember { mutableStateOf(true) }
    var ctx by remember { mutableStateOf<FloraPostTourFeedbackContextDto?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(setOf<String>()) }
    var preview by remember { mutableStateOf<FloraPreferencePreviewDto?>(null) }
    var previewLoading by remember { mutableStateOf(false) }
    var submitting by remember { mutableStateOf(false) }
    var savingPrefs by remember { mutableStateOf(false) }
    var reviewSubmitted by remember { mutableStateOf(false) }
    var prefsSaved by remember { mutableStateOf(false) }
    var actionError by remember { mutableStateOf<String?>(null) }

    fun reload() {
        scope.launch {
            loading = true
            error = null
            when (val result = repository.loadContext(bookingId)) {
                is FeedbackLoadResult.Success -> ctx = result.data
                is FeedbackLoadResult.Unauthorized -> error = "Vui lòng đăng nhập để gửi đánh giá."
                is FeedbackLoadResult.Error -> error = result.message
            }
            loading = false
        }
    }

    LaunchedEffect(bookingId) { reload() }

    LaunchedEffect(ctx?.personalizationEnabled, selectedTags) {
        val enabled = ctx?.personalizationEnabled == true
        if (!enabled || selectedTags.isEmpty()) {
            preview = null
            return@LaunchedEffect
        }
        previewLoading = true
        when (val result = repository.previewPreferences(selectedTags.toList())) {
            is FeedbackPreviewResult.Success -> preview = result.data
            else -> preview = null
        }
        previewLoading = false
    }

    if (loading) {
        Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            CircularProgressIndicator()
        }
        return
    }
    if (error != null) {
        Text(error!!, color = MaterialTheme.colorScheme.error, modifier = modifier.padding(8.dp))
        return
    }
    val context = ctx ?: return
    if (!context.eligible) return

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        if (context.alreadySubmitted && context.existingFeedback != null) {
            Text(
                "Bạn đã gửi đánh giá cho chuyến đi này rồi. Cảm ơn bạn đã chia sẻ cùng Flora.",
                style = MaterialTheme.typography.bodyMedium
            )
            StarRow(rating = context.existingFeedback.rating ?: 5, onSelect = {}, enabled = false)
            context.existingFeedback.comment?.let {
                Text(it, style = MaterialTheme.typography.bodySmall)
            }
            return@Column
        }

        if (reviewSubmitted) {
            Text("Cảm ơn bạn đã chia sẻ cùng Flora.", style = MaterialTheme.typography.titleSmall)
            if (prefsSaved) {
                Text("Đã lưu sở thích cho những chuyến đi sau.", style = MaterialTheme.typography.bodySmall)
            } else if (context.personalizationEnabled && selectedTags.isNotEmpty() && !preview?.changes.isNullOrEmpty()) {
                PreviewChanges(preview)
                Button(
                    onClick = {
                        val patch = preview?.patchRequest ?: return@Button
                        scope.launch {
                            savingPrefs = true
                            when (val result = repository.savePreferences(patch)) {
                                is PreferenceSaveResult.Success -> prefsSaved = true
                                is PreferenceSaveResult.Error -> actionError = result.message
                                PreferenceSaveResult.Unauthorized -> actionError = "Vui lòng đăng nhập lại."
                            }
                            savingPrefs = false
                        }
                    },
                    enabled = !savingPrefs,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Lưu sở thích này") }
            }
            return@Column
        }

        Text("Đánh giá chuyến đi", style = MaterialTheme.typography.titleMedium)
        Text(
            "Chuyến đi của bạn đã kết thúc rồi. Flora rất muốn biết trải nghiệm của bạn để lần sau gợi ý phù hợp hơn.",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(8.dp))
        StarRow(rating = rating, onSelect = { rating = it }, enabled = !submitting)
        OutlinedTextField(
            value = comment,
            onValueChange = { if (it.length <= 2000) comment = it },
            label = { Text("Nhận xét (tuỳ chọn)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            enabled = !submitting
        )

        if (context.personalizationEnabled) {
            Spacer(Modifier.height(12.dp))
            Text("Gợi ý để Flora hiểu bạn hơn", style = MaterialTheme.typography.titleSmall)
            TagSection(
                tags = context.availableTags.orEmpty(),
                selected = selectedTags,
                onToggle = { id ->
                    selectedTags = if (selectedTags.contains(id)) selectedTags - id else selectedTags + id
                },
                enabled = !submitting
            )
            if (previewLoading) {
                Text("Đang xem trước sở thích…", style = MaterialTheme.typography.bodySmall)
            } else if (!preview?.changes.isNullOrEmpty()) {
                PreviewChanges(preview)
            }
        } else {
            Text(
                "Bạn vẫn có thể gửi đánh giá. Hãy bật cá nhân hóa trong Cài đặt Flora AI nếu muốn Flora ghi nhớ sở thích cho những chuyến đi sau.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        actionError?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                scope.launch {
                    submitting = true
                    actionError = null
                    val tags = if (context.personalizationEnabled) selectedTags.toList() else null
                    when (val result = repository.submitReview(bookingId, rating, comment, tags)) {
                        FeedbackSubmitResult.Success -> {
                            reviewSubmitted = true
                            reload()
                        }
                        FeedbackSubmitResult.Unauthorized -> actionError = "Vui lòng đăng nhập lại."
                        is FeedbackSubmitResult.Error -> actionError = result.message
                    }
                    submitting = false
                }
            },
            enabled = !submitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (context.personalizationEnabled && selectedTags.isNotEmpty()) "Chỉ gửi đánh giá" else "Gửi đánh giá")
        }
    }
}

@Composable
private fun StarRow(rating: Int, onSelect: (Int) -> Unit, enabled: Boolean) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        (1..5).forEach { star ->
            OutlinedButton(onClick = { onSelect(star) }, enabled = enabled) {
                Text(if (star <= rating) "★" else "☆")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagSection(
    tags: List<FloraFeedbackTagDto>,
    selected: Set<String>,
    onToggle: (String) -> Unit,
    enabled: Boolean
) {
    val liked = tags.filter { it.category == "LIKED" }
    val improve = tags.filter { it.category == "IMPROVE" }
    if (liked.isNotEmpty()) {
        Text("Bạn thích điều gì nhất trong chuyến đi này?", style = MaterialTheme.typography.bodySmall)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            liked.forEach { tag ->
                FilterChip(
                    selected = selected.contains(tag.id),
                    onClick = { onToggle(tag.id) },
                    label = { Text(tag.label) },
                    enabled = enabled
                )
            }
        }
    }
    if (improve.isNotEmpty()) {
        Text("Điều gì Flora có thể cải thiện cho lần sau?", style = MaterialTheme.typography.bodySmall)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            improve.forEach { tag ->
                FilterChip(
                    selected = selected.contains(tag.id),
                    onClick = { onToggle(tag.id) },
                    label = { Text(tag.label) },
                    enabled = enabled
                )
            }
        }
    }
}

@Composable
private fun PreviewChanges(preview: FloraPreferencePreviewDto?) {
    val changes = preview?.changes.orEmpty()
    if (changes.isEmpty()) return
    Text("Gợi ý sở thích cho những chuyến đi tiếp theo", style = MaterialTheme.typography.bodySmall)
    changes.forEach { change ->
        Text("• ${change.field}: ${change.after}", style = MaterialTheme.typography.bodySmall)
    }
}

package com.example.flourishtravelapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.data.api.RetrofitClient
import com.example.flourishtravelapp.data.model.CreateGuideSessionExpenseRequest
import com.example.flourishtravelapp.data.model.GuideSessionExpenseDto
import com.example.flourishtravelapp.data.model.GuideSessionSummaryDto
import com.example.flourishtravelapp.ui.theme.*
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

private val EXPENSE_CATEGORIES = listOf("Vận chuyển", "Ăn uống", "Vé tham quan", "Lưu trú", "Khác")

private fun formatVnd(amount: Long): String =
    NumberFormat.getNumberInstance(Locale("vi", "VN")).format(amount) + " đ"

private fun expenseStatusLabel(status: String?): String = when (status?.lowercase()) {
    "approved" -> "Đã duyệt"
    "rejected" -> "Từ chối"
    else -> "Chờ duyệt"
}

private fun expenseStatusColor(status: String?): Color = when (status?.lowercase()) {
    "approved" -> Color(0xFF2E7D32)
    "rejected" -> Color(0xFFC62828)
    else -> Color(0xFFE65100)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideExpensesScreen(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var sessions by remember { mutableStateOf<List<GuideSessionSummaryDto>>(emptyList()) }
    var sessionId by remember { mutableStateOf("") }
    var expenses by remember { mutableStateOf<List<GuideSessionExpenseDto>>(emptyList()) }
    var loadingSessions by remember { mutableStateOf(true) }
    var loadingExpenses by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf(EXPENSE_CATEGORIES.first()) }
    var description by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var saving by remember { mutableStateOf(false) }

    val selectedSession = sessions.find { it.sessionId == sessionId }

    fun reloadExpenses(sid: String) {
        if (sid.isBlank()) return
        scope.launch {
            loadingExpenses = true
            error = null
            try {
                val response = RetrofitClient.guideApiService.listSessionExpenses(sid)
                if (response.isSuccessful && response.body()?.success == true) {
                    expenses = response.body()?.data.orEmpty()
                } else {
                    expenses = emptyList()
                    error = response.body()?.message ?: "Không tải được chi phí"
                }
            } catch (e: Exception) {
                expenses = emptyList()
                error = e.localizedMessage ?: "Lỗi kết nối"
            } finally {
                loadingExpenses = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadingSessions = true
        try {
            val response = RetrofitClient.guideApiService.getSessions()
            if (response.isSuccessful && response.body()?.success == true) {
                sessions = response.body()?.data.orEmpty()
                if (sessionId.isBlank()) {
                    sessionId = sessions.firstOrNull()?.sessionId.orEmpty()
                }
            }
        } catch (e: Exception) {
            error = e.localizedMessage
        } finally {
            loadingSessions = false
        }
    }

    LaunchedEffect(sessionId) {
        if (sessionId.isNotBlank()) reloadExpenses(sessionId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Chi phí tour", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DarkTextColor)
        Text(
            "Ghi nhận chi phí — đồng bộ kế toán trên hệ thống.",
            fontSize = 13.sp,
            color = SecondaryTextColor
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (loadingSessions) {
            Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
            return@Column
        }

        if (sessions.isEmpty()) {
            Text("Chưa có tour để ghi chi phí.", color = SecondaryTextColor)
            return@Column
        }

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedSession?.tourTitle ?: "Chọn tour",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                label = { Text("Chuyến đang quản lý") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                sessions.forEach { s ->
                    DropdownMenuItem(
                        text = { Text(s.tourTitle ?: s.sessionId) },
                        onClick = {
                            sessionId = s.sessionId
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val total = expenses.sumOf { it.amount ?: 0L }
            Text("Tổng: ${formatVnd(total)}", fontWeight = FontWeight.SemiBold, color = PrimaryGreen)
            FilledTonalButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Thêm")
            }
        }

        error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Color(0xFFC62828), fontSize = 13.sp)
        }

        Spacer(Modifier.height(12.dp))

        if (loadingExpenses) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else if (expenses.isEmpty()) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Chưa có khoản chi.", color = SecondaryTextColor)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(expenses, key = { it.id }) { row ->
                    ExpenseRow(
                        row = row,
                        sessionId = sessionId,
                        onDeleted = { reloadExpenses(sessionId) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { if (!saving) showDialog = false },
            title = { Text("Thêm chi phí") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    var catExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(expanded = catExpanded, onExpandedChange = { catExpanded = it }) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            label = { Text("Loại") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(catExpanded) }
                        )
                        ExposedDropdownMenu(expanded = catExpanded, onDismissRequest = { catExpanded = false }) {
                            EXPENSE_CATEGORIES.forEach { c ->
                                DropdownMenuItem(
                                    text = { Text(c) },
                                    onClick = { category = c; catExpanded = false }
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Mô tả") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Số tiền (VND)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !saving && description.isNotBlank() && amountText.isNotBlank(),
                    onClick = {
                        val amount = amountText.toLongOrNull() ?: return@TextButton
                        saving = true
                        scope.launch {
                            try {
                                val response = RetrofitClient.guideApiService.createSessionExpense(
                                    sessionId,
                                    CreateGuideSessionExpenseRequest(
                                        category = category,
                                        description = description.trim(),
                                        amount = amount
                                    )
                                )
                                if (response.isSuccessful && response.body()?.success == true) {
                                    description = ""
                                    amountText = ""
                                    showDialog = false
                                    reloadExpenses(sessionId)
                                } else {
                                    error = response.body()?.message ?: "Không lưu được"
                                }
                            } catch (e: Exception) {
                                error = e.localizedMessage
                            } finally {
                                saving = false
                            }
                        }
                    }
                ) { Text(if (saving) "Đang lưu…" else "Lưu") }
            },
            dismissButton = {
                TextButton(onClick = { if (!saving) showDialog = false }) { Text("Hủy") }
            }
        )
    }
}

@Composable
private fun ExpenseRow(
    row: GuideSessionExpenseDto,
    sessionId: String,
    onDeleted: () -> Unit
) {
    val scope = rememberCoroutineScope()
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(row.category ?: "—", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(row.description ?: "", fontSize = 13.sp, color = SecondaryTextColor)
                Spacer(Modifier.height(4.dp))
                Text(formatVnd(row.amount ?: 0), fontWeight = FontWeight.SemiBold, color = Color(0xFFFF5722))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    expenseStatusLabel(row.status),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = expenseStatusColor(row.status)
                )
                if (row.status.equals("pending", ignoreCase = true)) {
                    IconButton(onClick = {
                        scope.launch {
                            try {
                                RetrofitClient.guideApiService.deleteSessionExpense(sessionId, row.id)
                                onDeleted()
                            } catch (_: Exception) { }
                        }
                    }) {
                        Icon(Icons.Default.Delete, null, tint = Color(0xFFC62828))
                    }
                }
            }
        }
    }
}

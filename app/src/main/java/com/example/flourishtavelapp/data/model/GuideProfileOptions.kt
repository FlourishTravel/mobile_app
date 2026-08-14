package com.example.flourishtravelapp.data.model

val GUIDE_LANGUAGE_OPTIONS = listOf(
    "Tiếng Việt",
    "Tiếng Anh",
    "Tiếng Thái",
    "Tiếng Trung",
    "Tiếng Pháp",
    "Tiếng Khmer"
)

val GUIDE_SPECIALTY_OPTIONS = listOf(
    "Ẩm thực",
    "Văn hóa",
    "Phiêu lưu",
    "Wellness",
    "Bền vững",
    "Nghệ thuật",
    "Trong nước",
    "Thái Lan"
)

fun genderBeToUi(raw: String?): String = when (raw?.lowercase()) {
    "female" -> "Nữ"
    "other" -> "Khác"
    else -> "Nam"
}

fun genderUiToBe(ui: String): String = when (ui) {
    "Nữ" -> "female"
    "Khác" -> "other"
    else -> "male"
}

fun toggleChip(list: List<String>, value: String): List<String> =
    if (list.contains(value)) list.filter { it != value } else list + value

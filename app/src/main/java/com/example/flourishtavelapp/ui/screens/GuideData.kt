package com.example.flourishtavelapp.ui.screens

// ─── Data Models ────────────────────────────────────────────────────────────

data class GuideAccount(
    val username: String,
    val password: String,
    val name: String,
    val handle: String,
    val phone: String,
    val rating: Float,
    val totalTours: Int,
    val specialty: String
)

data class GuideTourDay(
    val day: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val isCurrent: Boolean = false
)

data class GuideTour(
    val id: String,
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val durationDays: Int,
    val totalCustomers: Int,
    val status: TourStatus,
    val imageDescription: String,
    val meetingPoint: String,
    val itinerary: List<GuideTourDay>,
    val customers: List<TourCustomer>
)

enum class TourStatus(val label: String, val color: Long) {
    ONGOING("Đang diễn ra", 0xFF00796B),
    UPCOMING("Sắp khởi hành", 0xFF1565C0),
    COMPLETED("Đã hoàn thành", 0xFF616161)
}

data class TourCustomer(
    val id: String,
    val name: String,
    val phone: String,
    val idCard: String,
    val gender: String,
    val adultCount: Int,
    val childCount: Int,
    val paymentStatus: PaymentStatus,
    val note: String = ""
)

enum class PaymentStatus(val label: String, val color: Long) {
    PAID("Đã thanh toán", 0xFF2E7D32),
    PENDING("Chờ xác nhận", 0xFFE65100),
    DEPOSIT("Đặt cọc 30%", 0xFF1565C0)
}

// ─── Mock Data ───────────────────────────────────────────────────────────────

val mockGuideAccounts = listOf(
    GuideAccount(
        username = "guide123",
        password = "guide123",
        name = "Minh Quân",
        handle = "@minhquan_guide",
        phone = "+84 912 345 678",
        rating = 4.9f,
        totalTours = 128,
        specialty = "Đông Nam Á · Biển đảo"
    )
)

val mockGuideTours = listOf(
    GuideTour(
        id = "T001",
        name = "Khám Phá Thái Lan Chill",
        destination = "Bangkok · Pattaya · Phi Phi",
        startDate = "12/05/2025",
        endDate = "16/05/2025",
        durationDays = 5,
        totalCustomers = 18,
        status = TourStatus.ONGOING,
        imageDescription = "Thái Lan",
        meetingPoint = "Sân bay Tân Sơn Nhất, Cửa số 2, 06:00",
        itinerary = listOf(
            GuideTourDay(1, "TP.HCM – Bangkok – Pattaya",
                "Trưa: Đáp chuyến bay đến Bangkok, xe đón đoàn đi thẳng về Pattaya.\nChiều: Check-in khách sạn, đi The Sky Gallery hoặc Tutu Beach.\nTối: Ngắm hoàng hôn, uống nước dừa, nghe tiếng sóng.",
                isCompleted = true),
            GuideTourDay(2, "Đảo Coral – Về lại Bangkok",
                "Sáng: Đi cano ra đảo Coral, nằm dưới tán cây đọc sách hoặc bơi lội.\nTrưa: Ăn hải sản tươi tại đảo.\nTối: Check-in khách sạn, dạo quanh khu vực hoặc ghé Rooftop Bar.",
                isCurrent = true),
            GuideTourDay(3, "Chậm Rãi Giữa Lòng Bangkok",
                "Sáng: Tham quan Wat Arun (2-3 tiếng thong thả).\nTrưa: Ăn món Thái ven sông.\nChiều: Ghé The Commons (Thonglor) - không gian mở cực chill."),
            GuideTourDay(4, "Nghệ Thuật & Tầm Nhìn",
                "Sáng: Bảo tàng MOCA Bangkok.\nChiều: Lên sàn kính Mahanakhon Skywalk ngắm hoàng hôn và thành phố lên đèn."),
            GuideTourDay(5, "Tạm Biệt Thái Lan",
                "Sáng: Tự do dạo công viên Lumpini hoặc ghé tiệm cafe yêu thích.\nTrưa: Xe đưa đoàn ra sân bay về lại TP.HCM.")
        ),
        customers = listOf(
            TourCustomer("C001", "Nguyễn Văn Bảo", "0901 234 567", "012345678901", "Nam", 2, 1, PaymentStatus.PAID),
            TourCustomer("C002", "Trần Thị Mai", "0912 345 678", "034567890123", "Nữ", 2, 0, PaymentStatus.PAID, "Dị ứng hải sản"),
            TourCustomer("C003", "Lê Hoàng Nam", "0933 456 789", "056789012345", "Nam", 1, 0, PaymentStatus.DEPOSIT),
            TourCustomer("C004", "Phạm Thị Lan", "0944 567 890", "078901234567", "Nữ", 2, 2, PaymentStatus.PAID),
            TourCustomer("C005", "Hoàng Minh Tuấn", "0955 678 901", "090123456789", "Nam", 1, 1, PaymentStatus.PENDING),
            TourCustomer("C006", "Vũ Thị Hoa", "0966 789 012", "012345098765", "Nữ", 2, 0, PaymentStatus.PAID),
            TourCustomer("C007", "Đặng Văn Cường", "0977 890 123", "034567654321", "Nam", 1, 0, PaymentStatus.PAID),
            TourCustomer("C008", "Bùi Thị Ngọc", "0988 901 234", "056789876543", "Nữ", 2, 1, PaymentStatus.DEPOSIT, "Yêu cầu phòng tầng cao")
        )
    ),
    GuideTour(
        id = "T002",
        name = "Bali – Thiên Đường Nhiệt Đới",
        destination = "Ubud · Seminyak · Nusa Penida",
        startDate = "20/05/2025",
        endDate = "25/05/2025",
        durationDays = 6,
        totalCustomers = 12,
        status = TourStatus.UPCOMING,
        imageDescription = "Bali",
        meetingPoint = "Sân bay Tân Sơn Nhất, Cửa số 4, 07:30",
        itinerary = listOf(
            GuideTourDay(1, "TP.HCM – Bali – Ubud", "Bay đến Bali, xe đưa thẳng về Ubud. Check-in resort, nghỉ ngơi."),
            GuideTourDay(2, "Khám Phá Ubud", "Sáng: Thăm Rừng Khỉ Ubud và đền Tirta Empul.\nChiều: Tham quan ruộng bậc thang Tegallalang.\nTối: Xem múa Kecak tại Uluwatu."),
            GuideTourDay(3, "Nusa Penida", "Ngày tour đảo Nusa Penida. Thăm Kelingking Beach, Crystal Bay."),
            GuideTourDay(4, "Seminyak & Mua Sắm", "Tự do tham quan và mua sắm tại Seminyak."),
            GuideTourDay(5, "Tanah Lot & Spa", "Sáng: Tham quan đền Tanah Lot.\nChiều: Thư giãn spa truyền thống Bali."),
            GuideTourDay(6, "Tạm Biệt Bali", "Tự do, bay về TP.HCM buổi chiều.")
        ),
        customers = listOf(
            TourCustomer("C101", "Nguyễn Thị Thanh", "0901 111 222", "011122334455", "Nữ", 2, 0, PaymentStatus.PAID),
            TourCustomer("C102", "Trần Văn Hùng", "0912 222 333", "033344556677", "Nam", 1, 0, PaymentStatus.PAID),
            TourCustomer("C103", "Lý Mỹ Châu", "0933 333 444", "055566778899", "Nữ", 2, 2, PaymentStatus.DEPOSIT),
            TourCustomer("C104", "Phan Quốc Khánh", "0944 444 555", "077788990011", "Nam", 2, 1, PaymentStatus.PENDING)
        )
    ),
    GuideTour(
        id = "T003",
        name = "Hạ Long – Vịnh Di Sản",
        destination = "Hạ Long · Cát Bà · Lan Hạ",
        startDate = "01/04/2025",
        endDate = "03/04/2025",
        durationDays = 3,
        totalCustomers = 22,
        status = TourStatus.COMPLETED,
        imageDescription = "Hạ Long",
        meetingPoint = "Khách sạn Novotel Hạ Long, 08:00",
        itinerary = listOf(
            GuideTourDay(1, "Hà Nội – Hạ Long – Du Thuyền", "Xe đưa từ Hà Nội xuống cảng, lên du thuyền, khám phá hang Sửng Sốt.", isCompleted = true),
            GuideTourDay(2, "Chèo Kayak – Cát Bà", "Chèo kayak qua các hang động nước, thăm đảo Cát Bà.", isCompleted = true),
            GuideTourDay(3, "Vịnh Lan Hạ – Về Hà Nội", "Bơi tại Vịnh Lan Hạ, ăn trưa trên thuyền, xe đưa về Hà Nội.", isCompleted = true)
        ),
        customers = listOf(
            TourCustomer("C201", "Trịnh Thị Bích", "0901 999 888", "099988776655", "Nữ", 2, 0, PaymentStatus.PAID),
            TourCustomer("C202", "Ngô Văn Đức", "0912 888 777", "088877665544", "Nam", 1, 1, PaymentStatus.PAID)
        )
    )
)

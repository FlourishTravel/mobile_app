package com.example.flourishtavelapp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*

@Composable
fun TourDetailScreen(
    onBack: () -> Unit,
    onBookNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize().background(NatureGreenBackground)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkTextColor)
                }
                Text(
                    "Destination Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor
                )
                IconButton(onClick = { /* Share */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = PrimaryGreen)
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // Hero Image
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Box {
                        Image(
                            painter = painterResource(id = com.example.flourishtavelapp.R.drawable.maya_bg),
                            contentDescription = "Vịnh Maya",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(24.dp)
                        ) {
                            Surface(
                                color = Color(0xFF00BFA5),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "PARADISE FOUND",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Vịnh Maya (Phi Phi)",
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(14.dp))
                                Text(" Krabi, Phuket, Thái Lan", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(16.dp))
                                Icon(Icons.Default.Timer, null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(14.dp))
                                Text(" Cả ngày", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Price Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("GIÁ TỪ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SecondaryTextColor)
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text("$45", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = PrimaryGreen)
                            Text("/khách", fontSize = 14.sp, color = SecondaryTextColor, modifier = Modifier.padding(bottom = 6.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Đã bao gồm phí tham quan, bữa trưa buffet và bảo hiểm du lịch.",
                            fontSize = 13.sp,
                            color = SecondaryTextColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Feature Icons
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FeatureIconCard(
                        modifier = Modifier.weight(1f),
                        title = "Cano cao tốc",
                        description = "Di chuyển nhanh chóng, êm ái qua các đảo.",
                        iconRes = com.example.flourishtavelapp.R.drawable.thailan // Placeholder
                    )
                    FeatureIconCard(
                        modifier = Modifier.weight(1f),
                        title = "Lặn ống thở",
                        description = "Ngắm nhìn rạn san hô rực rỡ và cá nhiệt đới.",
                        iconRes = com.example.flourishtavelapp.R.drawable.thailan // Placeholder
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Itinerary
                Text(
                    "Lịch trình chi tiết",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                ItineraryItem(
                    time = "08:00",
                    title = "Đón tại khách sạn",
                    description = "Xe đời mới đón quý khách tại sảnh khách sạn Phuket/Krabi di chuyển đến bến cảng."
                )
                
                ItineraryItem(
                    time = "09:30",
                    title = "Check-in Vịnh Maya",
                    description = "Tham quan bãi biển thiên đường nổi tiếng nhất Thái Lan, tự do chụp ảnh và thư giãn."
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Side-by-side Special Itinerary Item (Lunch) - Redesigned to match image
                Row(
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = com.example.flourishtavelapp.R.drawable.phiphi_bg),
                        contentDescription = "Bữa trưa Phi Phi",
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(32.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Card(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(containerColor = NatureGreenBackground)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp).fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("12:00", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Bữa trưa\nBuffet", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkTextColor, lineHeight = 22.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Thưởng thức ẩm thực Thái Lan truyền thống trên đảo Phi Phi Don.",
                                fontSize = 12.sp,
                                color = SecondaryTextColor,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ItineraryItem(
                    time = "14:00",
                    title = "Lặn ngắm san hổ tại Pileh Lagoon",
                    description = "Nhảy xuống làn nước xanh ngọc bích, khám phá thế giới đại dương đầy màu sắc."
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Map Section Placeholder
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize().background(Color.LightGray)) {
                        Image(
                            painter = painterResource(id = com.example.flourishtavelapp.R.drawable.bandodulichthailan_bg),
                            contentDescription = "Map",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Surface(
                            modifier = Modifier.align(Alignment.Center),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White
                        ) {
                            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, null, tint = PrimaryGreen, modifier = Modifier.size(16.dp))
                                Text(" Vịnh Maya, Đảo Phi Phi", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Fixed Bottom Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Button(
                onClick = onBookNowClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Đặt ngay ->", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun FeatureIconCard(modifier: Modifier, title: String, description: String, iconRes: Int) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.6f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // Using a default icon for now, ideally specific icons
                    Icon(Icons.Default.Timer, null, tint = PrimaryGreen, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkTextColor)
            Text(description, fontSize = 11.sp, color = SecondaryTextColor, lineHeight = 16.sp)
        }
    }
}

@Composable
fun ItineraryItem(time: String, title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Text(time, color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkTextColor)
                Spacer(modifier = Modifier.height(4.dp))
                Text(description, fontSize = 12.sp, color = SecondaryTextColor, lineHeight = 18.sp)
            }
        }
    }
}

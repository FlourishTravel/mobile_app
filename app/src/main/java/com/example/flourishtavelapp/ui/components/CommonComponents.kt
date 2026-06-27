package com.example.flourishtravelapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtravelapp.ui.theme.*

@Composable
fun FeatureCard(icon: ImageVector, title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DarkTextColor
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = SecondaryTextColor
            )
        }
    }
}

@Composable
fun LoginTextField(label: String, value: String, onValueChange: (String) -> Unit, isPassword: Boolean = false) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = DarkTextColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = LightGreenBackground.copy(alpha = 0.3f),
                unfocusedContainerColor = LightGreenBackground.copy(alpha = 0.3f),
                disabledContainerColor = LightGreenBackground.copy(alpha = 0.3f),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            )
        )
    }
}

@Composable
fun TourCard(title: String, location: String, price: String, imageRes: Int?) {
    Card(
        modifier = Modifier.width(260.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                if (imageRes != null) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE0E0E0)))
                }
                Surface(
                    modifier = Modifier.padding(12.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.9f)
                ) {
                    Text(
                        text = price,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = DarkTextColor
                    )
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkTextColor)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = SecondaryTextColor, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(location, color = SecondaryTextColor, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun SmallCuratedCard(modifier: Modifier, icon: ImageVector, title: String, iconColor: Color, iconBg: Color) {
    Surface(
        modifier = modifier.height(160.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = iconBg
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = iconColor, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DarkTextColor,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

private data class BottomNavItem(
    val label: String,
    val outlined: ImageVector,
    val filled: ImageVector
)

@Composable
private fun FlourishStyleBottomNavigation(
    items: List<BottomNavItem>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(80.dp)
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedTab == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(height = 32.dp, width = 48.dp)
                            .background(
                                color = if (isSelected) NavSelectedBg else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.filled else item.outlined,
                            contentDescription = item.label,
                            tint = if (isSelected) NavSelectedIcon else SecondaryTextColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 10.sp
                        ),
                        color = if (isSelected) NavSelectedIcon else SecondaryTextColor,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}

@Composable
fun FlourishBottomNavigation(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    FlourishStyleBottomNavigation(
        items = listOf(
            BottomNavItem("Trang chủ", Icons.Outlined.Home, Icons.Filled.Home),
            BottomNavItem("Chuyến đi", Icons.Outlined.Luggage, Icons.Filled.Luggage),
            BottomNavItem("Trợ lý AI", Icons.Outlined.SmartToy, Icons.Filled.SmartToy),
            BottomNavItem("Khám phá", Icons.Outlined.Explore, Icons.Filled.Explore),
            BottomNavItem("Hồ sơ", Icons.Outlined.Person, Icons.Filled.Person)
        ),
        selectedTab = selectedTab,
        onTabSelected = onTabSelected
    )
}

@Composable
fun GuideBottomNavigation(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    FlourishStyleBottomNavigation(
        items = listOf(
            BottomNavItem("Trang chủ", Icons.Outlined.Home, Icons.Filled.Home),
            BottomNavItem("Tour", Icons.Outlined.Map, Icons.Filled.Map),
            BottomNavItem("Đoàn", Icons.Outlined.Groups, Icons.Filled.Groups),
            BottomNavItem("Vận hành", Icons.Outlined.Engineering, Icons.Filled.Engineering),
            BottomNavItem("Hồ sơ", Icons.Outlined.Person, Icons.Filled.Person)
        ),
        selectedTab = selectedTab,
        onTabSelected = onTabSelected
    )
}

@Composable
fun TransportCard(title: String, points: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(32.dp).background(LightGreenBackground, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.DirectionsBus, null, tint = PrimaryGreen, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            points.forEach { point ->
                Text("• $point", color = SecondaryTextColor, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 8.dp))
            }
        }
    }
}

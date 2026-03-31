package com.example.flourishtavelapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flourishtavelapp.ui.theme.*

@Composable
fun MapLocationItem(title: String, subtitle: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LightGreenBackground.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Text(subtitle, color = SecondaryTextColor, style = MaterialTheme.typography.bodySmall)
            }
            TextButton(onClick = { /* Open Map */ }) {
                Icon(Icons.Outlined.LocationOn, null, tint = PrimaryGreen, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Mở map", color = DarkTextColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CityGuideCard(
    cityName: String,
    description: String,
    tips: List<String>,
    foods: List<String>,
    prices: List<String>,
    phrases: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(cityName, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(description, color = SecondaryTextColor, style = MaterialTheme.typography.bodyMedium)
            
            Spacer(modifier = Modifier.height(16.dp))
            Surface(shape = RoundedCornerShape(8.dp), color = LightGreenBackground) {
                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Outlined.MenuBook, null, tint = PrimaryGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Easy read", color = PrimaryGreen, style = MaterialTheme.typography.labelSmall)
                }
            }

            GuideSection(title = "Nên nhớ", items = tips)
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Món nên thử", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                foods.take(3).forEach { food ->
                    SuggestionChip(food)
                }
            }

            GuideSection(title = "Giá tham khảo", items = prices)

            Spacer(modifier = Modifier.height(16.dp))
            Text("Câu tiếng Anh nhanh", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            phrases.forEach { phrase ->
                PhraseCard(phrase.first, phrase.second)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun GuideSection(title: String, items: List<String>) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(title, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    items.forEach { item ->
        Text("• $item", color = SecondaryTextColor, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 4.dp))
    }
}

@Composable
fun SuggestionChip(text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = LightGreenBackground.copy(alpha = 0.5f)
    ) {
        Text(text, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun PhraseCard(english: String, vietnamese: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF0F7FF)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(english, fontWeight = FontWeight.Bold, color = DarkTextColor)
            Text(vietnamese, color = SecondaryTextColor, style = MaterialTheme.typography.bodySmall)
        }
    }
}

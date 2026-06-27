package com.example.flourishtravelapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GroupCommunicationScreen(
    bookingId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BookingGroupChatScreen(
        bookingId = bookingId,
        onBack = onBack,
        modifier = modifier
    )
}

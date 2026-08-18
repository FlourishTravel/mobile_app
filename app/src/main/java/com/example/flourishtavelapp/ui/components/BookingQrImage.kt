package com.example.flourishtravelapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.flourishtravelapp.data.util.BookingCodes
import com.example.flourishtravelapp.data.util.BookingQrBitmaps

@Composable
fun BookingQrImage(
    bookingCode: String?,
    bookingId: String?,
    modifier: Modifier = Modifier,
    sizeDp: Int = 180,
) {
    val payload = remember(bookingCode, bookingId) { BookingCodes.qrPayload(bookingCode, bookingId) }
    val bmp = remember(payload) { BookingQrBitmaps.encode(payload) }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (bmp != null) {
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = "QR $payload",
                modifier = Modifier.size(sizeDp.dp),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

package com.example.feedbook.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.example.feedbook.core.ui.theme.FeedBookColors

@Composable
fun RemoteBookCover(
    title: String,
    coverImageUrl: String?,
    modifier: Modifier = Modifier,
    fallbackBackground: Color = FeedBookColors.AccentSoft,
    fallbackContent: @Composable BoxScope.() -> Unit = {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = FeedBookColors.SecondaryText,
            textAlign = TextAlign.Center
        )
    }
) {
    Box(
        modifier = modifier.background(fallbackBackground),
        contentAlignment = Alignment.Center
    ) {
        if (!coverImageUrl.isNullOrBlank()) {
            val imageRequest = rememberFeedBookImageRequest(coverImageUrl)
            AsyncImage(
                model = imageRequest,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            fallbackContent()
        }
    }
}

package com.example.feedbook.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.feedbook.core.ui.components.rememberFeedBookImageRequest
import com.example.feedbook.features.profile.presentation.AvatarPreset
import com.example.feedbook.features.profile.presentation.AvatarStyle

@Composable
internal fun ProfileAvatarArtwork(
    avatarStyle: AvatarStyle,
    avatarPreset: AvatarPreset?,
    avatarImageUri: String?,
    modifier: Modifier = Modifier,
    imageShape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(10.dp),
    fallbackContent: @Composable BoxScope.() -> Unit = {}
) {
    val imageModel = avatarImageUri ?: avatarPreset?.imageUrl

    Box(
        modifier = modifier
            .clip(imageShape)
            .background(
                Brush.verticalGradient(
                    listOf(avatarStyle.topColor.copy(alpha = 0.18f), avatarStyle.bottomColor.copy(alpha = 0.34f))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (imageModel != null) {
            val imageRequest = rememberFeedBookImageRequest(imageModel)
            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().clip(imageShape),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
        } else {
            fallbackContent()
        }
    }
}

@Composable
internal fun ProfileTopBarAvatarFill(
    avatarStyle: AvatarStyle,
    avatarPreset: AvatarPreset?,
    avatarImageUri: String?,
    modifier: Modifier = Modifier
) {
    ProfileAvatarArtwork(
        avatarStyle = avatarStyle,
        avatarPreset = avatarPreset,
        avatarImageUri = avatarImageUri,
        modifier = modifier,
        imageShape = CircleShape
    ) {}
}

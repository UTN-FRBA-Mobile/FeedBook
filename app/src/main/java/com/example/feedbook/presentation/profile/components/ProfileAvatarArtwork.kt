package com.example.feedbook.presentation.profile.components

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.feedbook.presentation.profile.AvatarStyle

@Composable
internal fun ProfileAvatarArtwork(
    avatarStyle: AvatarStyle,
    avatarImageUri: String?,
    modifier: Modifier = Modifier,
    imageShape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(10.dp),
    fallbackContent: @Composable BoxScope.() -> Unit = {}
) {
    val context = LocalContext.current
    val imageBitmap = remember(context, avatarImageUri) {
        avatarImageUri?.let { uriString ->
            runCatching {
                context.contentResolver.openInputStream(Uri.parse(uriString))?.use { stream ->
                    BitmapFactory.decodeStream(stream)?.asImageBitmap()
                }
            }.getOrNull()
        }
    }

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
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().clip(imageShape),
                contentScale = ContentScale.Crop
            )
        } else {
            fallbackContent()
        }
    }
}

@Composable
internal fun ProfileTopBarAvatarFill(
    avatarStyle: AvatarStyle,
    avatarImageUri: String?,
    modifier: Modifier = Modifier
) {
    ProfileAvatarArtwork(
        avatarStyle = avatarStyle,
        avatarImageUri = avatarImageUri,
        modifier = modifier,
        imageShape = CircleShape
    ) {}
}

package com.example.feedbook.features.profile.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.compositionLocalOf
import com.example.feedbook.features.profile.domain.model.ReaderProfile

data class AvatarPresentation(
    val style: AvatarStyle,
    val preset: AvatarPreset?,
    val imageUri: String?
)

data class TopBarAvatarState(
    val style: AvatarStyle,
    val preset: AvatarPreset?,
    val imageUri: String?
)

val LocalFeedBookTopBarAvatar = compositionLocalOf<TopBarAvatarState?> { null }

fun ReaderProfile.toAvatarPresentation(): AvatarPresentation {
    val style = AvatarStyle(
        topColor = Color(avatar.topColorHex),
        bottomColor = Color(avatar.bottomColorHex)
    )
    return AvatarPresentation(
        style = style,
        preset = null,
        imageUri = avatar.imageUri
    )
}

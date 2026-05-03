package com.example.feedbook.features.profile.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.profile.domain.model.ReaderProfile

data class AvatarPresentation(
    val style: AvatarStyle,
    val preset: AvatarPreset?,
    val imageUri: String?
)

fun ReaderProfile.toAvatarPresentation(): AvatarPresentation {
    val style = AvatarStyle(
        topColor = Color(avatar.topColorHex),
        bottomColor = Color(avatar.bottomColorHex)
    )
    return AvatarPresentation(
        style = style,
        preset = avatarPresetFromData(avatar.avatarPresetId, style, avatar.presetImageUrl),
        imageUri = avatar.imageUri
    )
}

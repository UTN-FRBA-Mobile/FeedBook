package com.example.feedbook.features.profile.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.profile.domain.model.ReaderProfile

data class AvatarPresentation(
    val style: AvatarStyle,
    val imageUri: String?
)

fun ReaderProfile.toAvatarPresentation(): AvatarPresentation = AvatarPresentation(
    style = AvatarStyle(
        topColor = Color(avatar.topColorHex),
        bottomColor = Color(avatar.bottomColorHex)
    ),
    imageUri = avatar.imageUri
)

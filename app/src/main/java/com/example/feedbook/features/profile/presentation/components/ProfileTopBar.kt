package com.example.feedbook.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.feedbook.R
import com.example.feedbook.features.profile.presentation.AvatarPreset
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.ProfileVariant

@Composable
internal fun ProfileTopBar(
    variant: ProfileVariant,
    avatarStyle: AvatarStyle = AvatarStyle(
        topColor = Color(0xFF315A73),
        bottomColor = Color(0xFFF0C6A8)
    ),
    avatarPreset: AvatarPreset? = null,
    avatarImageUri: String? = null,
    title: String = stringResource(R.string.profile_topbar_title),
    onAvatarClick: () -> Unit = {},
    trailingContent: @Composable RowScope.(Dp) -> Unit = { iconSize ->
        if (variant == ProfileVariant.OWN) {
            ProfileTopBarActionIcon(
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = stringResource(R.string.profile_topbar_recent_activity),
                iconSize = iconSize
            )
            ProfileTopBarActionIcon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = stringResource(R.string.profile_topbar_settings),
                iconSize = iconSize
            )
        } else {
            ProfileTopBarActionIcon(
                imageVector = Icons.AutoMirrored.Outlined.Chat,
                contentDescription = stringResource(R.string.profile_topbar_direct_message),
                iconSize = iconSize
            )
            ProfileTopBarActionIcon(
                imageVector = Icons.Outlined.MoreHoriz,
                contentDescription = stringResource(R.string.profile_topbar_more_options),
                iconSize = iconSize
            )
        }
    },
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F8F6))
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val iconSize = (maxWidth * 0.05f).coerceIn(20.dp, 26.dp)
            val avatarSize = (maxWidth * 0.085f).coerceIn(32.dp, 42.dp)
            val innerAvatarSize = (avatarSize * 0.76f).coerceIn(24.dp, 31.dp)
            val horizontalPadding = (maxWidth * 0.041f).coerceIn(16.dp, 20.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(avatarSize)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE3E2E2))
                        .border(1.dp, Color(0x4DC4C6CD), RoundedCornerShape(12.dp))
                        .clickable(onClick = onAvatarClick),
                    contentAlignment = Alignment.Center
                ) {
                    ProfileTopBarAvatarFill(
                        avatarStyle = avatarStyle,
                        avatarPreset = avatarPreset,
                        avatarImageUri = avatarImageUri,
                        modifier = Modifier.size(innerAvatarSize).clip(CircleShape)
                    )
                }

                Text(
                    text = title,
                    style = ProfileTypography.AppTitle,
                    color = Color(0xFF0F172A)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy((iconSize * 0.55f).coerceIn(10.dp, 16.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    trailingContent(iconSize)
                }
            }
        }

        HorizontalDivider(color = ProfileColors.Divider)
    }
}

@Composable
internal fun ProfileTopBarActionIcon(
    imageVector: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    iconSize: Dp,
    onClick: (() -> Unit)? = null,
    tint: Color = ProfileColors.SecondaryText
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(iconSize)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            ),
        tint = tint
    )
}

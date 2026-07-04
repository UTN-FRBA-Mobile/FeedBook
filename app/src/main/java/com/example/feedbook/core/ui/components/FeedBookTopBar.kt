package com.example.feedbook.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.feedbook.R
import com.example.feedbook.features.profile.presentation.AvatarPreset
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.ProfileVariant
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle
import com.example.feedbook.features.profile.presentation.components.ProfileTopBarAvatarFill
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileTypography

@Composable
internal fun FeedBookTopBar(
    variant: ProfileVariant,
    avatarStyle: AvatarStyle = defaultAvatarStyle(),
    avatarPreset: AvatarPreset? = null,
    avatarImageUri: String? = null,
    title: String = stringResource(R.string.profile_topbar_title),
    onAvatarClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ProfileColors.Background)
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
                        .background(ProfileColors.AccentSoft)
                        .border(1.dp, ProfileColors.Border, RoundedCornerShape(12.dp))
                        .clickable(onClick = onAvatarClick),
                    contentAlignment = Alignment.Center
                ) {
                    ProfileTopBarAvatarFill(
                        avatarStyle = avatarStyle,
                        avatarPreset = avatarPreset,
                        avatarImageUri = avatarImageUri,
                        modifier = Modifier
                            .size(innerAvatarSize)
                            .clip(CircleShape)
                    )
                }

                Text(
                    text = title,
                    style = ProfileTypography.AppTitle,
                    color = ProfileColors.PrimaryText
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy((iconSize * 0.55f).coerceIn(10.dp, 16.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DefaultTopBarActions(
                        iconSize = iconSize,
                        onLogoutClick = onLogoutClick
                    )
                }
            }
        }

        HorizontalDivider(color = ProfileColors.Divider)
    }
}

@Composable
private fun RowScope.DefaultTopBarActions(
    iconSize: Dp,
    onLogoutClick: () -> Unit
) {
    var showSettingsMenu by remember { mutableStateOf(false) }
    val onScannerClick = LocalScannerClickHandler.current

    Icon(
        imageVector = Icons.Outlined.QrCodeScanner,
        contentDescription = null,
        tint = ProfileColors.SecondaryText,
        modifier = Modifier
            .size(iconSize)
            .clickable(onClick = onScannerClick)
    )
    Box {
        Icon(
            imageVector = Icons.Outlined.Settings,
            contentDescription = null,
            tint = ProfileColors.SecondaryText,
            modifier = Modifier
                .size(iconSize)
                .clickable { showSettingsMenu = true }
        )
        DropdownMenu(
            expanded = showSettingsMenu,
            onDismissRequest = { showSettingsMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Logout") },
                onClick = {
                    showSettingsMenu = false
                    onLogoutClick()
                }
            )
        }
    }
}

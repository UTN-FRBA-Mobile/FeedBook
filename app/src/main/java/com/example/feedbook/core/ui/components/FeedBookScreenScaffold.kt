package com.example.feedbook.core.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.feedbook.features.profile.presentation.AvatarPreset
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.ProfileVariant
import com.example.feedbook.features.profile.presentation.LocalFeedBookTopBarAvatar
import com.example.feedbook.features.profile.presentation.components.ProfileColors

@Composable
internal fun FeedBookScreenScaffold(
    modifier: Modifier = Modifier,
    variant: ProfileVariant,
    activeTab: BottomBarTab,
    avatarStyle: AvatarStyle,
    avatarPreset: AvatarPreset? = null,
    avatarImageUri: String? = null,
    title: String = "FeedBook",
    onAvatarClick: () -> Unit = {},
    onRefreshClick: (() -> Unit)? = null,
    onFeedClick: () -> Unit = {},
    onExploreClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    showBottomBar: Boolean = true,
    snackbarHost: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val topBarAvatar = LocalFeedBookTopBarAvatar.current
    Scaffold(
        modifier = modifier,
        containerColor = ProfileColors.Background,
        topBar = {
            FeedBookTopBar(
                variant = variant,
                avatarStyle = topBarAvatar?.style ?: avatarStyle,
                avatarPreset = topBarAvatar?.preset ?: avatarPreset,
                avatarImageUri = topBarAvatar?.imageUri ?: avatarImageUri,
                title = title,
                onAvatarClick = onAvatarClick,
                onRefreshClick = onRefreshClick,
                onLogoutClick = onLogoutClick
            )
        },
        bottomBar = {
            if (showBottomBar) {
                FeedBookBottomBar(
                    activeTab = activeTab,
                    onFeedClick = onFeedClick,
                    onExploreClick = onExploreClick,
                    onLibraryClick = onLibraryClick,
                    onStatsClick = onStatsClick,
                    onNotificationsClick = onNotificationsClick
                )
            }
        },
        snackbarHost = {
            snackbarHost?.invoke()
        },
        content = content
    )
}

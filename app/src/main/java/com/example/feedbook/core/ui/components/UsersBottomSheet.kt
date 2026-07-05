package com.example.feedbook.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.features.books.domain.model.ExploreUser
import com.example.feedbook.features.profile.presentation.components.ProfileAvatarArtwork
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersBottomSheet(
    title: String,
    users: List<ExploreUser>,
    onUserClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ProfileColors.Background,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(modifier = Modifier.padding(bottom = 32.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = ProfileColors.PrimaryText,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
            if (users.isEmpty()) {
                Text(
                    text = "No users yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ProfileColors.SecondaryText,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)
                )
            } else {
                val sortedUsers = users.sortedByDescending { it.isFollowing }
                LazyColumn {
                    items(sortedUsers, key = { it.id }) { user ->
                        UserRow(user = user, onClick = { onUserClick(user.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun UserRow(
    user: ExploreUser,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (user.avatarImageUrl != null) Color.Transparent
                    else Color(user.avatarTopColorHex)
                )
        ) {
            if (user.avatarImageUrl != null) {
                ProfileAvatarArtwork(
                    avatarStyle = defaultAvatarStyle(),
                    avatarPreset = null,
                    avatarImageUri = user.avatarImageUrl,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                ProfileAvatarArtwork(
                    avatarStyle = defaultAvatarStyle(),
                    avatarPreset = null,
                    avatarImageUri = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
                color = ProfileColors.PrimaryText
            )
            Text(
                text = user.handle,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                color = ProfileColors.SecondaryText
            )
        }
        if (user.isFollowing) {
            Text(
                text = "Following",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                color = ProfileColors.Accent
            )
        }
    }
}

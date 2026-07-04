package com.example.feedbook.features.profile.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feedbook.R
import com.example.feedbook.core.ui.components.BottomBarTab
import com.example.feedbook.core.ui.components.FeedBookScreenScaffold
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTypography
import com.example.feedbook.features.profile.presentation.components.ProfileAvatarArtwork

@Composable
fun EditProfileScreen(
    state: ProfileUiState,
    onBackClick: () -> Unit,
    onFeedClick: () -> Unit = {},
    onExploreClick: () -> Unit = {},
    onProfileClick: () -> Unit,
    onLibraryClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onSave: (ProfileUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember(state.name) { mutableStateOf(state.name) }
    var handle by remember(state.handle) { mutableStateOf(state.handle) }
    var quote by remember(state.quote) { mutableStateOf(state.quote) }
    var targetPagesInput by remember(state.readingGoal?.targetPagesPerDay) {
        mutableStateOf(state.readingGoal?.targetPagesPerDay?.toString().orEmpty())
    }
    var selectedAvatarImageUri by remember(state.avatarImageUri) { mutableStateOf(state.avatarImageUri) }
    val selectedAvatarStyle = state.avatarStyle
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = ProfileColors.PrimaryText,
        unfocusedTextColor = ProfileColors.PrimaryText,
        focusedLabelColor = ProfileColors.SecondaryText,
        unfocusedLabelColor = ProfileColors.SecondaryText,
        focusedPlaceholderColor = ProfileColors.SecondaryText.copy(alpha = 0.85f),
        unfocusedPlaceholderColor = ProfileColors.SecondaryText.copy(alpha = 0.85f),
        focusedBorderColor = ProfileColors.SurfaceStrong,
        unfocusedBorderColor = ProfileColors.Border,
        cursorColor = ProfileColors.Accent
    )
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedAvatarImageUri = uri.toString()
        }
    }

    fun buildUpdatedState(): ProfileUiState {
        val target = targetPagesInput.toIntOrNull()
        val readingGoal = if (target != null && target > 0) {
            ReadingGoal(
                targetPagesPerDay = target,
                currentAveragePagesPerDay = (state.readingGoal?.currentAveragePagesPerDay ?: (target * 0.7f).toInt()).coerceAtLeast(0)
            )
        } else {
            null
        }

        return state.copy(
            name = name.trim().ifEmpty { state.name },
            handle = handle.trim().ifEmpty { state.handle },
            quote = quote.trim().ifEmpty { state.quote },
            avatarStyle = selectedAvatarStyle,
            avatarImageUri = selectedAvatarImageUri,
            readingGoal = readingGoal
        )
    }

    FeedBookScreenScaffold(
        modifier = modifier.fillMaxSize(),
        variant = state.variant,
        activeTab = BottomBarTab.FEED,
        avatarStyle = selectedAvatarStyle,
        avatarImageUri = selectedAvatarImageUri,
        title = stringResource(R.string.edit_profile_title),
        onAvatarClick = onProfileClick,
        onFeedClick = onFeedClick,
        onExploreClick = onExploreClick,
        onLibraryClick = onLibraryClick,
        onStatsClick = onStatsClick,
        onNotificationsClick = onNotificationsClick,
        onLogoutClick = onLogoutClick
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(ProfileColors.Background)
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProfileSurfaceCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        Text(
                            text = stringResource(R.string.edit_profile_basic_details),
                            style = ProfileTypography.SectionTitle,
                            color = ProfileColors.PrimaryText
                        )
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(R.string.edit_profile_name_label)) },
                            placeholder = { Text(stringResource(R.string.edit_profile_name_placeholder)) },
                            colors = fieldColors,
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = handle,
                            onValueChange = { input ->
                                handle = buildString {
                                    append("@")
                                    append(input.removePrefix("@").replace(" ", ""))
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(R.string.edit_profile_handle_label)) },
                            placeholder = { Text(stringResource(R.string.edit_profile_handle_placeholder)) },
                            colors = fieldColors,
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = quote,
                            onValueChange = { quote = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(R.string.edit_profile_bio_label)) },
                            placeholder = { Text(stringResource(R.string.edit_profile_bio_placeholder)) },
                            colors = fieldColors,
                            minLines = 4
                        )
                    }
                }
            }

            item {
                ProfileSurfaceCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        Text(
                            text = stringResource(R.string.edit_profile_avatar_title),
                            style = ProfileTypography.SectionTitle,
                            color = ProfileColors.PrimaryText
                        )
                        Text(
                            text = stringResource(R.string.edit_profile_avatar_body),
                            style = ProfileTypography.Body,
                            color = ProfileColors.SecondaryText
                        )
                        val hasCustomPhoto = !selectedAvatarImageUri.isNullOrBlank()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(92.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF4F2F0))
                                .border(
                                    width = if (hasCustomPhoto) 2.dp else 1.dp,
                                    color = if (hasCustomPhoto) ProfileColors.SurfaceStrong else ProfileColors.Border,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                ) {
                                    ProfileAvatarArtwork(
                                        avatarStyle = selectedAvatarStyle,
                                        avatarPreset = null,
                                        avatarImageUri = selectedAvatarImageUri,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.edit_profile_avatar_custom_title),
                                        style = ProfileTypography.Body.copy(fontWeight = FontWeight.SemiBold),
                                        color = ProfileColors.PrimaryText
                                    )
                                    Text(
                                        text = if (!hasCustomPhoto) {
                                            stringResource(R.string.edit_profile_avatar_custom_empty)
                                        } else {
                                            stringResource(R.string.edit_profile_avatar_custom_selected)
                                        },
                                        style = ProfileTypography.Label,
                                        color = ProfileColors.SecondaryText
                                    )
                                }
                            }
                        }
                        if (hasCustomPhoto) {
                            TextButton(
                                onClick = { selectedAvatarImageUri = "" },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.edit_profile_avatar_clear),
                                    style = ProfileTypography.Label,
                                    color = ProfileColors.Accent
                                )
                            }
                        }
                    }
                }
            }

            item {
                ProfileSurfaceCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        Text(
                            text = stringResource(R.string.edit_profile_goal_title),
                            style = ProfileTypography.SectionTitle,
                            color = ProfileColors.PrimaryText
                        )
                        Text(
                            text = stringResource(R.string.edit_profile_goal_body),
                            style = ProfileTypography.Body,
                            color = ProfileColors.SecondaryText
                        )
                        OutlinedTextField(
                            value = targetPagesInput,
                            onValueChange = { targetPagesInput = it.filter(Char::isDigit).take(3) },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(R.string.edit_profile_goal_label)) },
                            placeholder = { Text(stringResource(R.string.edit_profile_goal_placeholder)) },
                            colors = fieldColors,
                            singleLine = true
                        )
                        HorizontalDivider(color = ProfileColors.Divider)
                        Text(
                            text = if (targetPagesInput.isBlank()) {
                                stringResource(R.string.edit_profile_goal_empty_hint)
                            } else {
                                stringResource(R.string.edit_profile_goal_filled_hint)
                            },
                            style = ProfileTypography.Label,
                            color = ProfileColors.SecondaryText
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = { onSave(buildUpdatedState()) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ProfileColors.SurfaceStrong,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = stringResource(R.string.edit_profile_save),
                        style = ProfileTypography.Button.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun EditProfileScreenPreview() {
    FeedBookTheme(dynamicColor = false) {
        EditProfileScreen(
            state = previewOwnProfileUiState(),
            onBackClick = {},
            onProfileClick = {},
            onSave = {}
        )
    }
}

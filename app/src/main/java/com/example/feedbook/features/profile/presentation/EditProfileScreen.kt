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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTopBar
import com.example.feedbook.features.profile.presentation.components.ProfileTopBarActionIcon
import com.example.feedbook.features.profile.presentation.components.ProfileTypography
import com.example.feedbook.features.profile.presentation.components.ProfileAvatarArtwork

private val avatarPresets = listOf(
    AvatarStyle(Color(0xFF315A73), Color(0xFFF0C6A8)),
    AvatarStyle(Color(0xFF5C6D8A), Color(0xFFD8C1A0)),
    AvatarStyle(Color(0xFF6E918B), Color(0xFFE8D7BF)),
    AvatarStyle(Color(0xFF8A5C52), Color(0xFFF1D4B5))
)

@Composable
fun EditProfileScreen(
    state: ProfileUiState,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSave: (ProfileUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember(state.name) { mutableStateOf(state.name) }
    var handle by remember(state.handle) { mutableStateOf(state.handle) }
    var quote by remember(state.quote) { mutableStateOf(state.quote) }
    var targetPagesInput by remember(state.readingGoal?.targetPagesPerDay) {
        mutableStateOf(state.readingGoal?.targetPagesPerDay?.toString().orEmpty())
    }
    var selectedAvatarStyle by remember(state.avatarStyle) { mutableStateOf(state.avatarStyle) }
    var selectedAvatarImageUri by remember(state.avatarImageUri) { mutableStateOf(state.avatarImageUri) }
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ProfileColors.Background,
        topBar = {
            ProfileTopBar(
                variant = state.variant,
                avatarStyle = selectedAvatarStyle,
                avatarImageUri = selectedAvatarImageUri,
                title = "Edit Profile",
                onAvatarClick = onProfileClick,
                trailingContent = { iconSize ->
                    ProfileTopBarActionIcon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                        iconSize = iconSize,
                        onClick = onBackClick
                    )
                }
            )
        }
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
                            text = "Basic Details",
                            style = ProfileTypography.SectionTitle,
                            color = ProfileColors.PrimaryText
                        )
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Name") },
                            placeholder = { Text("Evelyn Vance") },
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
                            label = { Text("Handle") },
                            placeholder = { Text("@evelynv") },
                            colors = fieldColors,
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = quote,
                            onValueChange = { quote = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Bio / Quote") },
                            placeholder = { Text("Write a short line about your reading taste") },
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
                            text = "Avatar Style",
                            style = ProfileTypography.SectionTitle,
                            color = ProfileColors.PrimaryText
                        )
                        Text(
                            text = "Pick the palette used by your profile mark.",
                            style = ProfileTypography.Body,
                            color = ProfileColors.SecondaryText
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            avatarPresets.forEach { preset ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(72.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(preset.topColor, preset.bottomColor)
                                            )
                                        )
                                        .border(
                                            width = if (preset == selectedAvatarStyle) 2.dp else 1.dp,
                                            color = if (preset == selectedAvatarStyle && selectedAvatarImageUri == null) ProfileColors.SurfaceStrong else ProfileColors.Border,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable {
                                            selectedAvatarStyle = preset
                                            selectedAvatarImageUri = null
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.22f))
                                    )
                                }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(84.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF4F2F0))
                                .border(
                                    width = if (selectedAvatarImageUri != null) 2.dp else 1.dp,
                                    color = if (selectedAvatarImageUri != null) ProfileColors.SurfaceStrong else ProfileColors.Border,
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
                                        avatarImageUri = selectedAvatarImageUri,
                                        modifier = Modifier.fillMaxSize(),
                                        fallbackContent = {
                                            Text(
                                                text = "EV",
                                                style = ProfileTypography.LabelUppercase,
                                                color = ProfileColors.Accent
                                            )
                                        }
                                    )
                                }
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "Custom photo",
                                        style = ProfileTypography.Body.copy(fontWeight = FontWeight.SemiBold),
                                        color = ProfileColors.PrimaryText
                                    )
                                    Text(
                                        text = if (selectedAvatarImageUri == null) {
                                            "Choose an image from your device"
                                        } else {
                                            "Photo selected. Tap to replace it."
                                        },
                                        style = ProfileTypography.Label,
                                        color = ProfileColors.SecondaryText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                ProfileSurfaceCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        Text(
                            text = "Reading Goal",
                            style = ProfileTypography.SectionTitle,
                            color = ProfileColors.PrimaryText
                        )
                        Text(
                            text = "Leave target empty if you do not want a daily goal on your profile.",
                            style = ProfileTypography.Body,
                            color = ProfileColors.SecondaryText
                        )
                        OutlinedTextField(
                            value = targetPagesInput,
                            onValueChange = { targetPagesInput = it.filter(Char::isDigit).take(3) },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Daily target pages") },
                            placeholder = { Text("40") },
                            colors = fieldColors,
                            singleLine = true
                        )
                        HorizontalDivider(color = ProfileColors.Divider)
                        Text(
                            text = if (targetPagesInput.isBlank()) {
                                "No reading goal will be shown."
                            } else {
                                "The profile card will compare your current pace against this daily target."
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
                        text = "SAVE CHANGES",
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
            state = sampleProfileUiState(),
            onBackClick = {},
            onProfileClick = {},
            onSave = {}
        )
    }
}

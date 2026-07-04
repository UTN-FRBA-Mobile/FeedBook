package com.example.feedbook.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.feedbook.R
import com.example.feedbook.features.profile.presentation.AvatarPreset
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.ProfileVariant

@Composable
internal fun ProfileHeaderSection(
    variant: ProfileVariant,
    name: String,
    handle: String,
    quote: String,
    actionLabelRes: Int,
    avatarStyle: AvatarStyle,
    avatarPreset: AvatarPreset?,
    avatarImageUri: String?,
    onActionClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(112.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Transparent)
                .border(1.dp, Color(0x80C4C6CD), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(86.dp)
            ) {
                ProfileAvatarArtwork(
                    avatarStyle = avatarStyle,
                    avatarPreset = avatarPreset,
                    avatarImageUri = avatarImageUri,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Text(
            text = name,
            style = ProfileTypography.HeroName,
            color = ProfileColors.PrimaryText
        )
        Text(
            text = handle,
            style = ProfileTypography.SmallCaps,
            color = ProfileColors.SecondaryText
        )
        Text(
            text = quote,
            style = ProfileTypography.Body,
            color = ProfileColors.SecondaryText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.76f)
        )
        Button(
            onClick = onActionClick,
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 25.dp, vertical = 9.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = ProfileColors.SecondaryText
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .height(36.dp)
                .border(1.dp, Color(0xFF74777D), RoundedCornerShape(12.dp))
        ) {
            Text(
                text = stringResource(actionLabelRes),
                style = ProfileTypography.Button
            )
        }

        if (variant == ProfileVariant.PUBLIC) {
            Text(
                text = stringResource(R.string.profile_public_summary),
                style = ProfileTypography.Label,
                color = ProfileColors.SecondaryText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.82f)
            )
        }
    }
}

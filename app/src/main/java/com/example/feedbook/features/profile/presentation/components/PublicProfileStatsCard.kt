package com.example.feedbook.features.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.features.profile.presentation.ProfileStat

@Composable
internal fun PublicProfileStatsCard(
    stats: List<ProfileStat>,
    onFollowersClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (stats.isEmpty()) return

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        stats.forEach { stat ->
            ProfileSurfaceCard(
                modifier = Modifier.weight(1f),
                onClick = if (onFollowersClick != null && stat.label.equals("Followers", ignoreCase = true)) onFollowersClick else null
            ) {
                BoxWithConstraints {
                    val valueStyle = when {
                        maxWidth < 120.dp -> ProfileTypography.HeroName.copy(
                            fontSize = 21.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        stat.value.length > 10 -> ProfileTypography.HeroName.copy(
                            fontSize = 25.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        else -> ProfileTypography.HeroName.copy(fontWeight = FontWeight.SemiBold)
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stat.label.uppercase(),
                            style = ProfileTypography.LabelUppercase,
                            color = ProfileColors.SecondaryText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = stat.value,
                            style = valueStyle,
                            color = ProfileColors.PrimaryText,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

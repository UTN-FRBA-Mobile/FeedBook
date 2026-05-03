package com.example.feedbook.features.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.feedbook.features.profile.presentation.ProfileStat

@Composable
internal fun PublicProfileStatsCard(
    stats: List<ProfileStat>,
    modifier: Modifier = Modifier
) {
    if (stats.isEmpty()) return

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        stats.forEach { stat ->
            ProfileSurfaceCard(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stat.label.uppercase(),
                        style = ProfileTypography.LabelUppercase,
                        color = ProfileColors.SecondaryText
                    )
                    Text(
                        text = stat.value,
                        style = ProfileTypography.HeroName.copy(fontWeight = FontWeight.SemiBold),
                        color = ProfileColors.PrimaryText,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

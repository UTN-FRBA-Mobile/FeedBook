package com.example.feedbook.presentation.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.presentation.profile.components.ProfileColors
import com.example.feedbook.presentation.profile.components.ProfileSurfaceCard
import com.example.feedbook.presentation.profile.components.ProfileTypography
import com.example.feedbook.presentation.stats.StatsMetric

@Composable
internal fun StatsMetricCard(
    metric: StatsMetric,
    modifier: Modifier = Modifier
) {
    ProfileSurfaceCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = metric.label,
                style = ProfileTypography.LabelUppercase.copy(
                    fontSize = 9.sp,
                    letterSpacing = 0.9.sp
                ),
                color = ProfileColors.SecondaryText
            )
            Text(
                text = metric.value,
                style = ProfileTypography.HeroName.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 31.sp,
                    lineHeight = 34.sp
                ),
                color = ProfileColors.PrimaryText
            )
        }
    }
}

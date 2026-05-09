package com.example.feedbook.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.R
import com.example.feedbook.features.profile.presentation.ReadingGoal

@Composable
internal fun ReadingGoalCard(
    readingGoal: ReadingGoal?,
    onEditGoalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileSurfaceCard(modifier = modifier.fillMaxWidth()) {
        if (readingGoal == null) {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Text(
                    text = stringResource(R.string.profile_reading_goal_title),
                    style = ProfileTypography.SectionTitle,
                    color = ProfileColors.PrimaryText
                )
                Text(
                    text = stringResource(R.string.profile_reading_goal_empty_body),
                    style = ProfileTypography.Body,
                    color = ProfileColors.SecondaryText
                )
                OutlinedButton(
                    onClick = onEditGoalClick,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(42.dp)
                ) {
                    Text(
                        text = stringResource(R.string.profile_reading_goal_create),
                        style = ProfileTypography.Button
                    )
                }
            }
            return@ProfileSurfaceCard
        }

        val progress = (readingGoal.currentAveragePagesPerDay.toFloat() / readingGoal.targetPagesPerDay.toFloat())
            .coerceIn(0f, 1.3f)

        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = stringResource(R.string.profile_reading_goal_title),
                        style = ProfileTypography.SectionTitle,
                        color = ProfileColors.PrimaryText
                    )
                    Text(
                        text = stringResource(R.string.profile_reading_goal_pages_per_day),
                        style = ProfileTypography.Label,
                        color = ProfileColors.SecondaryText
                    )
                }
                Text(
                    text = stringResource(
                        R.string.profile_reading_goal_progress,
                        readingGoal.currentAveragePagesPerDay,
                        readingGoal.targetPagesPerDay
                    ),
                    style = ProfileTypography.StatNumber.copy(fontSize = 34.sp, lineHeight = 38.sp),
                    color = ProfileColors.Accent
                )
            }

            HorizontalDivider(color = ProfileColors.Divider)

            Text(
                text = when {
                    readingGoal.currentAveragePagesPerDay >= readingGoal.targetPagesPerDay ->
                        stringResource(R.string.profile_reading_goal_above_target)
                    else -> stringResource(
                        R.string.profile_reading_goal_below_target,
                        readingGoal.targetPagesPerDay - readingGoal.currentAveragePagesPerDay
                    )
                },
                style = ProfileTypography.Body,
                color = ProfileColors.SecondaryText
            )

            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(ProfileColors.AccentSoft, RoundedCornerShape(999.dp))
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceAtMost(1f))
                        .height(8.dp)
                        .background(ProfileColors.Accent, RoundedCornerShape(999.dp))
                )
            }

            Button(
                onClick = onEditGoalClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ProfileColors.SurfaceStrong,
                    contentColor = Color.White
                ),
                modifier = Modifier.height(42.dp)
            ) {
                Text(
                    text = stringResource(R.string.profile_reading_goal_modify),
                    style = ProfileTypography.Button.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

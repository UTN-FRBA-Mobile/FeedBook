package com.example.feedbook.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.R
import com.example.feedbook.features.profile.presentation.ReadingStreak

@Composable
internal fun ReadingStreakCard(
    streak: ReadingStreak,
    modifier: Modifier = Modifier
) {
    ProfileSurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = stringResource(R.string.profile_reading_streak_title),
                        style = ProfileTypography.SectionTitle,
                        color = ProfileColors.PrimaryText
                    )
                    Text(
                        text = stringResource(R.string.profile_reading_streak_subtitle),
                        style = ProfileTypography.Label,
                        color = ProfileColors.SecondaryText
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = streak.days.toString(),
                        style = ProfileTypography.StatNumber,
                        color = ProfileColors.Accent
                    )
                    Text(
                        text = stringResource(R.string.profile_reading_streak_days),
                        style = ProfileTypography.LabelUppercase.copy(fontSize = 10.sp, lineHeight = 10.sp),
                        color = ProfileColors.SecondaryText
                    )
                }
            }

            HorizontalDivider(color = ProfileColors.Divider)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                streak.week.forEach { day ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        if (day.isToday) {
                            Box(
                                modifier = Modifier
                                    .height(104.dp)
                                    .fillMaxWidth()
                                    .border(
                                        width = 2.dp,
                                        color = ProfileColors.TodayOutline,
                                        shape = RoundedCornerShape(2.dp)
                                    )
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .height(106.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(day.fillFraction.coerceIn(0f, 1f))
                                        .clip(RoundedCornerShape(topStart = 1.dp, topEnd = 1.dp))
                                        .background(
                                            if (day.completed) ProfileColors.Accent else ProfileColors.AccentSoft
                                        )
                                )
                            }
                        }

                        Text(
                            text = day.label,
                            style = ProfileTypography.LabelUppercase.copy(
                                fontSize = 10.sp,
                                lineHeight = 10.sp,
                                fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (day.isToday) ProfileColors.SurfaceStrong else {
                                if (day.completed) ProfileColors.PrimaryText else Color(0xFF74777D)
                            }
                        )
                    }
                }
            }
        }
    }
}

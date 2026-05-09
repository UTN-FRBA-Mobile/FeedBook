package com.example.feedbook.features.library.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTypography

@Composable
internal fun LibraryOverviewCard(
    readingCount: Int,
    shelfCount: Int,
    completedBooks: Int,
    modifier: Modifier = Modifier
) {
    ProfileSurfaceCard(
        modifier = modifier.fillMaxWidth(),
        containerColor = ProfileColors.SurfaceStrong,
        borderColor = Color.Transparent
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = "LIBRARY OVERVIEW",
                style = ProfileTypography.LabelUppercase,
                color = ProfileColors.ArchiveText
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                LibraryMetric("Reading", readingCount.toString())
                LibraryMetric("On shelf", shelfCount.toString())
                LibraryMetric("Read", completedBooks.toString())
            }
            HorizontalDivider(color = Color.White.copy(alpha = 0.10f))
            Text(
                text = "All content is sourced from the fake backend to avoid hardcoded UI data.",
                style = ProfileTypography.Label,
                color = Color.White.copy(alpha = 0.76f)
            )
        }
    }
}

@Composable
private fun LibraryMetric(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.width(84.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = ProfileTypography.SectionTitle,
            color = Color.White
        )
        Text(
            text = label,
            style = ProfileTypography.Label,
            color = ProfileColors.ArchiveText
        )
    }
}

package com.example.feedbook.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun LibraryArchiveCard(
    completedBooks: Int,
    modifier: Modifier = Modifier
) {
    ProfileSurfaceCard(
        modifier = modifier.fillMaxWidth(),
        containerColor = ProfileColors.SurfaceStrong,
        borderColor = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 4.dp)
                .clip(RoundedCornerShape(topStart = 18.dp))
                .background(Color.White.copy(alpha = 0.04f))
                .padding(horizontal = 18.dp, vertical = 24.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "LIBRARY ARCHIVE",
                style = ProfileTypography.LabelUppercase,
                color = ProfileColors.ArchiveText
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = completedBooks.toString(),
                    style = ProfileTypography.StatNumber,
                    color = Color.White
                )
                Text(
                    text = "Books completed",
                    style = ProfileTypography.Body,
                    color = ProfileColors.ArchiveText
                )
            }
            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View Collection",
                    style = ProfileTypography.Label,
                    color = Color.White
                )
                Text(
                    text = "\u2192",
                    style = ProfileTypography.Body,
                    color = Color.White
                )
            }
        }
    }
}

package com.example.feedbook.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.presentation.profile.QueuedBook

@Composable
internal fun UpNextCard(
    books: List<QueuedBook>,
    modifier: Modifier = Modifier
) {
    ProfileSurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "UP NEXT",
                    style = ProfileTypography.SmallCaps,
                    color = ProfileColors.PrimaryText
                )
                Text(
                    text = "\u2317",
                    style = ProfileTypography.Body,
                    color = ProfileColors.SecondaryText
                )
            }

            books.forEachIndexed { index, book ->
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = book.title,
                        style = ProfileTypography.Body.copy(fontSize = 18.sp, lineHeight = 28.8.sp),
                        color = ProfileColors.PrimaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = book.author,
                        style = ProfileTypography.Label,
                        color = ProfileColors.SecondaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (index != books.lastIndex) {
                    HorizontalDivider(color = ProfileColors.Divider)
                }
            }
        }
    }
}

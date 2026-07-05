package com.example.feedbook.features.library.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.feedbook.features.library.presentation.ReadBookItem
import com.example.feedbook.R
import coil.compose.AsyncImage
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTypography
import java.util.Locale

@Composable
internal fun ReadHistoryCard(
    books: List<ReadBookItem>,
    onBookClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    ProfileSurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = stringResource(R.string.library_read_history_title),
                style = ProfileTypography.LabelUppercase,
                color = ProfileColors.PrimaryText
            )

            if (books.isEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = stringResource(R.string.library_read_history_empty_title),
                        style = ProfileTypography.Body.copy(fontSize = 15.sp, lineHeight = 20.sp),
                        color = ProfileColors.PrimaryText
                    )
                    Text(
                        text = stringResource(R.string.library_read_history_empty_body),
                        style = ProfileTypography.Label,
                        color = ProfileColors.SecondaryText
                    )
                }
            } else {
                books.forEachIndexed { index, book ->
                    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                        val detailsWidth = maxWidth - 110.dp

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = book.id.isNotBlank()) { onBookClick(book.id) },
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 48.dp, height = 68.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(book.coverAccent)
                            ) {
                                if (!book.coverImageUrl.isNullOrBlank()) {
                                    AsyncImage(
                                        model = book.coverImageUrl,
                                        contentDescription = book.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .size(width = 8.dp, height = 56.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color.White.copy(alpha = 0.16f))
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier.width(detailsWidth),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = book.title,
                                    style = ProfileTypography.Body.copy(fontSize = 15.sp, lineHeight = 20.sp),
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
                                Text(
                                    text = "Started ${book.startedOn}  •  Finished ${book.finishedOn}",
                                    style = ProfileTypography.Label.copy(fontSize = 11.sp, lineHeight = 15.sp),
                                    color = ProfileColors.SecondaryText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                RatingStars(rating = book.personalRating)
                            }
                        }
                    }

                    if (index != books.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 2.dp),
                            color = ProfileColors.Divider
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RatingStars(rating: Float) {
    val clampedRating = rating.coerceIn(0f, 5f)
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            val starRating = clampedRating - index
            val icon = when {
                starRating >= 1f -> Icons.Filled.Star
                starRating >= 0.5f -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Outlined.StarOutline
            }
            val tint = when {
                starRating >= 1f -> ProfileColors.Accent
                starRating >= 0.5f -> ProfileColors.Accent
                else -> ProfileColors.SecondaryText
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = String.format(Locale.US, "%.1f/5", clampedRating),
            style = ProfileTypography.Label.copy(fontWeight = FontWeight.SemiBold),
            color = ProfileColors.Accent
        )
    }
}

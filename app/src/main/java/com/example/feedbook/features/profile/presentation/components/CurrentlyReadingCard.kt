package com.example.feedbook.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.R
import com.example.feedbook.core.ui.components.RemoteBookCover
import com.example.feedbook.features.profile.presentation.CurrentBook

@Composable
internal fun CurrentlyReadingCard(
    modifier: Modifier = Modifier,
    currentBook: CurrentBook,
    onBookClick: ((String) -> Unit)? = null,
    emphasized: Boolean = false
) {
    val hasCurrentBook = currentBook.id.isNotBlank() && currentBook.title.isNotBlank()
    ProfileSurfaceCard(
        modifier = modifier.fillMaxWidth(),
        onClick = if (hasCurrentBook && onBookClick != null) {
            { onBookClick(currentBook.id) }
        } else {
            null
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(Color(0x80F5F3F3), Color.Transparent)
                    )
                )
        )
        if (!hasCurrentBook) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.profile_currently_reading_label),
                    style = ProfileTypography.LabelUppercase,
                    color = ProfileColors.Accent
                )
                Text(
                    text = stringResource(R.string.profile_currently_reading_empty_title),
                    style = if (emphasized) {
                        ProfileTypography.LargeBookTitle.copy(fontSize = 34.sp, lineHeight = 42.sp)
                    } else {
                        ProfileTypography.SectionTitle
                    },
                    color = ProfileColors.PrimaryText
                )
                Text(
                    text = stringResource(R.string.profile_currently_reading_empty_body),
                    style = ProfileTypography.Body,
                    color = ProfileColors.SecondaryText
                )
            }
        } else {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val spacing = if (emphasized) 28.dp else 24.dp
                val coverWidth = if (emphasized) 122.dp else 96.dp
                val detailsWidth = maxWidth - coverWidth - spacing

                Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                    BookCover(
                        title = currentBook.title,
                        coverImageUrl = currentBook.coverImageUrl,
                        emphasized = emphasized
                    )
                    Column(
                        modifier = Modifier.width(detailsWidth),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "\u25A4",
                                style = ProfileTypography.LabelUppercase,
                                color = ProfileColors.Accent
                            )
                            Text(
                                text = stringResource(R.string.profile_currently_reading_label),
                                style = ProfileTypography.LabelUppercase,
                                color = ProfileColors.Accent
                            )
                        }
                        Text(
                            text = currentBook.title,
                            style = if (emphasized) {
                                ProfileTypography.LargeBookTitle.copy(fontSize = 36.sp, lineHeight = 44.sp)
                            } else {
                                ProfileTypography.LargeBookTitle
                            },
                            color = ProfileColors.PrimaryText,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = currentBook.author,
                            style = ProfileTypography.Body,
                            color = ProfileColors.SecondaryText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.profile_currently_reading_progress,
                                    currentBook.page,
                                    currentBook.totalPages
                                ),
                                style = if (emphasized) {
                                    ProfileTypography.Body.copy(fontSize = 14.sp, lineHeight = 20.sp)
                                } else {
                                    ProfileTypography.Label
                                },
                                color = ProfileColors.SecondaryText
                            )
                            Text(
                                text = "${(currentBook.progress * 100).toInt()}%",
                                style = if (emphasized) {
                                    ProfileTypography.Body.copy(
                                        fontSize = 15.sp,
                                        lineHeight = 20.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                } else {
                                    ProfileTypography.Label.copy(fontWeight = FontWeight.SemiBold)
                                },
                                color = ProfileColors.SurfaceStrong
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (emphasized) 8.dp else 6.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ProfileColors.AccentSoft)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(currentBook.progress.coerceIn(0f, 1f))
                                    .height(if (emphasized) 8.dp else 6.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ProfileColors.Accent)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookCover(
    modifier: Modifier = Modifier,
    title: String,
    coverImageUrl: String?,
    emphasized: Boolean = false
) {
    val coverWidth = if (emphasized) 122.dp else 96.dp
    val coverHeight = if (emphasized) 184.dp else 144.dp
    RemoteBookCover(
        title = title,
        coverImageUrl = coverImageUrl,
        modifier = modifier
            .size(width = coverWidth, height = coverHeight)
            .clip(RoundedCornerShape(2.dp)),
        fallbackBackground = Color(0xFFE8E3DE)
    )
}

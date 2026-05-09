package com.example.feedbook.features.library.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.features.library.presentation.ReadBookItem
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTypography

@Composable
internal fun ReadHistoryCard(
    books: List<ReadBookItem>,
    modifier: Modifier = Modifier
) {
    ProfileSurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = "READ COLLECTION",
                style = ProfileTypography.LabelUppercase,
                color = ProfileColors.PrimaryText
            )

            books.forEachIndexed { index, book ->
                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val detailsWidth = maxWidth - 110.dp

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 48.dp, height = 68.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(book.coverAccent)
                                .padding(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 8.dp, height = 56.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.White.copy(alpha = 0.16f))
                            )
                        }

                        Column(
                            modifier = Modifier.width(detailsWidth),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
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
                        }

                        Text(
                            text = "${book.personalRating}/5",
                            style = ProfileTypography.Label.copy(fontWeight = FontWeight.SemiBold),
                            color = ProfileColors.Accent
                        )
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

package com.example.feedbook.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.features.profile.presentation.LibraryBook

@Composable
internal fun PublicLibraryCard(
    books: List<LibraryBook>,
    modifier: Modifier = Modifier
) {
    if (books.isEmpty()) return

    ProfileSurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PUBLIC LIBRARY",
                    style = ProfileTypography.LabelUppercase,
                    color = ProfileColors.PrimaryText
                )
                Text(
                    text = "${books.size} books",
                    style = ProfileTypography.Label,
                    color = ProfileColors.SecondaryText
                )
            }

            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val coverWidth = ((maxWidth - 24.dp) / 3f).coerceAtLeast(88.dp)
                val coverHeight = (coverWidth * 1.55f)

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(books) { book ->
                        Column(
                            modifier = Modifier.width(coverWidth),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(coverHeight)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(book.accent)
                                    .padding(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width((coverWidth * 0.12f).coerceAtLeast(10.dp))
                                        .height(coverHeight - 20.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.White.copy(alpha = 0.16f))
                                )
                            }
                            Text(
                                text = book.title,
                                style = ProfileTypography.Label.copy(fontSize = 13.sp, lineHeight = 18.sp),
                                color = ProfileColors.PrimaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

package com.example.feedbook.features.library.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.core.ui.components.RemoteBookCover
import com.example.feedbook.features.profile.presentation.LibraryBook
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTypography

@Composable
internal fun LibraryCollectionCard(
    modifier: Modifier = Modifier,
    books: List<LibraryBook>,
    title: String = "ON YOUR SHELF",
    onBookClick: (String) -> Unit = {}
) {
    if (books.isEmpty()) return

    ProfileSurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
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
                val itemWidth = (maxWidth - 7.dp)

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    books.chunked(2).forEach { rowBooks ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            rowBooks.forEach { book ->
                                Column(
                                    modifier = Modifier
                                        .width(itemWidth / 2)
                                        .clickable { onBookClick(book.id) },
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(182.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                    ) {
                                        RemoteBookCover(
                                            title = book.title,
                                            coverImageUrl = book.coverImageUrl,
                                            modifier = Modifier.fillMaxWidth(),
                                            fallbackBackground = Color(0xFFE8E3DE)
                                        )
                                    }
                                    Text(
                                        text = book.title,
                                        style = ProfileTypography.Body.copy(fontSize = 15.sp, lineHeight = 22.sp),
                                        color = ProfileColors.PrimaryText,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            if (rowBooks.size == 1) {
                                Box(modifier = Modifier.width(itemWidth / 2))
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.example.feedbook.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.R
import com.example.feedbook.core.ui.components.RemoteBookCover
import com.example.feedbook.features.profile.presentation.FeaturedReview

@Composable
internal fun FeaturedReviewCard(
    reviews: List<FeaturedReview>,
    modifier: Modifier = Modifier
) {
    if (reviews.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { reviews.size })

    ProfileSurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.profile_recent_reviews),
                    style = ProfileTypography.LabelUppercase,
                    color = ProfileColors.PrimaryText
                )
                Text(
                    text = stringResource(
                        R.string.profile_review_pager,
                        pagerState.currentPage + 1,
                        reviews.size
                    ),
                    style = ProfileTypography.Label,
                    color = ProfileColors.SecondaryText
                )
            }

            HorizontalPager(
                state = pagerState,
                pageSpacing = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val review = reviews[page]

                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        RemoteBookCover(
                            title = review.bookTitle,
                            coverImageUrl = review.coverImageUrl,
                            modifier = Modifier
                                .size(width = 48.dp, height = 72.dp)
                                .clip(RoundedCornerShape(6.dp))
                        )
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = review.bookTitle,
                                style = ProfileTypography.Body.copy(lineHeight = 20.sp),
                                color = ProfileColors.PrimaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = buildString { repeat(review.rating.coerceIn(0, 5)) { append("★") } },
                                style = ProfileTypography.LabelUppercase,
                                color = ProfileColors.Accent
                            )
                        }
                        Text(
                            text = review.timeAgo,
                            style = ProfileTypography.Label,
                            color = ProfileColors.SecondaryText
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.45f))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = review.excerpt,
                            style = ProfileTypography.Body,
                            color = ProfileColors.SecondaryText,
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(reviews.size) { index ->
                        Box(
                            modifier = Modifier
                                .width(if (index == pagerState.currentPage) 18.dp else 8.dp)
                                .height(8.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(
                                    if (index == pagerState.currentPage) ProfileColors.SurfaceStrong
                                    else ProfileColors.AccentSoft
                                )
                        )
                    }
                }
            }
        }
    }
}

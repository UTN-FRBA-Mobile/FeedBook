package com.example.feedbook.features.books.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.features.books.domain.model.ReviewPart
import com.example.feedbook.features.profile.presentation.components.ProfileColors

internal fun reviewHasSpoilers(parts: List<ReviewPart>): Boolean {
    return parts.any { it.spoiler }
}

internal fun reviewPartsFromText(
    text: String,
    spoilerRanges: List<IntRange>
): List<ReviewPart> {
    if (text.isEmpty()) return emptyList()

    val normalizedRanges = mergeRanges(
        spoilerRanges.mapNotNull { range ->
            val start = range.first.coerceIn(0, text.length)
            val end = (range.last + 1).coerceIn(0, text.length)
            if (start >= end) null else start until end
        }
    )
    if (normalizedRanges.isEmpty()) {
        return listOf(ReviewPart(text = text, spoiler = false))
    }

    val parts = mutableListOf<ReviewPart>()
    var cursor = 0
    for (range in normalizedRanges) {
        if (cursor < range.first) {
            parts += ReviewPart(text = text.substring(cursor, range.first), spoiler = false)
        }
        parts += ReviewPart(text = text.substring(range.first, range.last + 1), spoiler = true)
        cursor = range.last + 1
    }
    if (cursor < text.length) {
        parts += ReviewPart(text = text.substring(cursor), spoiler = false)
    }
    return parts.filter { it.text.isNotEmpty() }
}

internal fun reviewPartsToRanges(parts: List<ReviewPart>): List<IntRange> {
    val ranges = mutableListOf<IntRange>()
    var cursor = 0
    for (part in parts) {
        val end = cursor + part.text.length
        if (part.spoiler && cursor < end) {
            ranges += cursor until end
        }
        cursor = end
    }
    return mergeRanges(ranges)
}

internal fun reviewPartsAnnotatedString(
    parts: List<ReviewPart>,
    showSpoilers: Boolean
): AnnotatedString = buildAnnotatedString {
    parts.forEach { part ->
        val start = length
        append(part.text)
        if (part.spoiler && !showSpoilers) {
            addStyle(
                SpanStyle(
                    color = Color.Transparent,
                    background = ProfileColors.SecondaryText.copy(alpha = 0.28f)
                ),
                start,
                length
            )
        }
    }
}

@Composable
internal fun ReviewSpoilerText(
    review: ReviewUiModel,
    showSpoilers: Boolean,
    onToggleSpoilers: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val content = if (review.parts.isEmpty()) {
            AnnotatedString(review.text)
        } else {
            reviewPartsAnnotatedString(review.parts, showSpoilers)
        }
        Text(
            text = content,
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,
                lineHeight = 20.sp
            ),
            color = ProfileColors.PrimaryText
        )
        if (reviewHasSpoilers(review.parts) && onToggleSpoilers != null) {
            Spacer(modifier = Modifier.height(6.dp))
            TextButton(onClick = onToggleSpoilers) {
                if (showSpoilers) {
                    Icon(
                        imageVector = Icons.Outlined.VisibilityOff,
                        contentDescription = null,
                        tint = ProfileColors.Accent
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Visibility,
                        contentDescription = null,
                        tint = ProfileColors.Accent
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (showSpoilers) "Ocultar spoiler" else "Ver spoiler",
                    color = ProfileColors.Accent
                )
            }
        }
    }
}

private fun mergeRanges(ranges: List<IntRange>): List<IntRange> {
    if (ranges.isEmpty()) return emptyList()

    val sorted = ranges.sortedBy { it.first }
    val merged = mutableListOf<IntRange>()
    var currentStart = sorted.first().first
    var currentEnd = sorted.first().last

    for (range in sorted.drop(1)) {
        if (range.first <= currentEnd + 1) {
            currentEnd = maxOf(currentEnd, range.last)
        } else {
            merged += currentStart..currentEnd
            currentStart = range.first
            currentEnd = range.last
        }
    }
    merged += currentStart..currentEnd
    return merged
}

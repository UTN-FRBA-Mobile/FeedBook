package com.example.feedbook.features.books.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.feedbook.R
import com.example.feedbook.core.ui.components.BottomBarTab
import com.example.feedbook.core.ui.components.FeedBookScreenScaffold
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.features.profile.presentation.components.ProfileColors

// ─── Stateful Wrapper ──────────────────────────────────────────────────────
@Composable
fun BookDetailScreen(
    modifier: Modifier = Modifier,
    viewModelFactory: ViewModelProvider.Factory,
    onBackClick: () -> Unit,
    onFeedClick: () -> Unit = {},
    onExploreClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    val viewModel: BookDetailViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookDetailScreen(
        state = state,
        onRetry = viewModel::loadBook,
        onBackClick = onBackClick,
        onFeedClick = onFeedClick,
        onExploreClick = onExploreClick,
        onProfileClick = onProfileClick,
        onLibraryClick = onLibraryClick,
        onStatsClick = onStatsClick,
        onNotificationsClick = onNotificationsClick,
        onLogoutClick = onLogoutClick,
        modifier = modifier
    )
}

// ─── Screen Entry Point (Stateless) ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    modifier: Modifier = Modifier,
    state: BookDetailUiState,
    onRetry: () -> Unit,
    onBackClick: () -> Unit,
    onUpdateProgress: () -> Unit = {},
    onAddToList: () -> Unit = {},
    onSaveProgress: (Int) -> Unit = {},
    onWriteReview: () -> Unit = {},
    onFeedClick: () -> Unit = {},
    onExploreClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onModeSelected: (String) -> Unit = {},
) {

    FeedBookScreenScaffold(
        modifier = modifier.fillMaxSize(),
        variant = com.example.feedbook.features.profile.presentation.ProfileVariant.OWN,
        activeTab = BottomBarTab.EXPLORE,
        avatarStyle = state.avatarStyle,
        avatarPreset = state.avatarPreset,
        avatarImageUri = state.avatarImageUri,
        onAvatarClick = onProfileClick,
        onFeedClick = onFeedClick,
        onExploreClick = onExploreClick,
        onLibraryClick = onLibraryClick,
        onStatsClick = onStatsClick,
        onNotificationsClick = onNotificationsClick,
        onLogoutClick = onLogoutClick
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ProfileColors.Background)
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                when {
                    state.isLoading -> LoadingContent()
                    state.error != null -> ErrorContent(message = state.error, onRetry = onRetry)
                    state.book != null -> BookDetailContent(
                        state = state,
                        onUpdateProgress = onUpdateProgress,
                        onAddToList = onAddToList,
                        onSaveProgress = onSaveProgress,
                        onWriteReview = onWriteReview
                    )
                }
            }
        }
    }
}

// ─── Main Content ──────────────────────────────────────────────────────────
@Composable
private fun BookDetailContent(
    state: BookDetailUiState,
    onUpdateProgress: () -> Unit,
    onAddToList: () -> Unit,
    onSaveProgress: (Int) -> Unit,
    onWriteReview: () -> Unit
) {
    val book = state.book!!
    var showProgressCard by remember { mutableStateOf(false) }
    val totalPages = state.readingProgress?.totalPages ?: 218
    var sliderValue by remember {
        mutableFloatStateOf(state.readingProgress?.currentPage?.toFloat() ?: 0f)
    }
    val averageRating = state.reviews
        .map { it.rating }
        .average()
        .toFloat()
        .takeIf { state.reviews.isNotEmpty() } ?: 0f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item { CoverSection(coverUrl = book.coverImageUrl) }
        item { BookInfoSection(book = book, rating = averageRating) }
        item {
            ActionButtonsSection(
                onUpdateProgress = {
                    showProgressCard = !showProgressCard
                    onUpdateProgress()
                },
                onAddToList = onAddToList
            )
        }
        if (showProgressCard) {
            item {
                ProgressCard(
                    currentPage = sliderValue.toInt(),
                    totalPages = totalPages,
                    percentage = ((sliderValue / totalPages) * 100).toInt(),
                    onSliderChange = { sliderValue = it },
                    onSave = { onSaveProgress(sliderValue.toInt()) }
                )
            }
        }
        item { FriendsSection() }
        item { ReviewsHeader(onWriteReview = onWriteReview) }
        items(state.reviews) { review -> ReviewCard(review = review) }
        item {
            BookMetadataSection(
                book.isbn,
                book.pages,
                book.language,
                book.published
            )
        }
        item { PullQuoteSection(description = book.description, genre = book.genre) }
    }
}

// ─── Cover Section ─────────────────────────────────────────────────────────
@Composable
private fun CoverSection(coverUrl: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        )
        if (LocalInspectionMode.current || coverUrl.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(240.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ProfileColors.AccentSoft),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Cover",
                    style = MaterialTheme.typography.labelSmall,
                    color = ProfileColors.PrimaryText
                )
            }
        } else {
            AsyncImage(
                model = coverUrl,
                contentDescription = "Portada del libro",
                modifier = Modifier
                    .width(160.dp)
                    .height(240.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

// ─── Book Info Section ─────────────────────────────────────────────────────
@Composable
private fun BookInfoSection(book: BookUiModel, rating: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = book.genre,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = book.title,
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "by ${book.author}",
            style = MaterialTheme.typography.titleSmall.copy(fontStyle = FontStyle.Italic),
        )
        Spacer(modifier = Modifier.height(10.dp))
        StarRatingRow(rating = rating)
    }
}

// ─── Star Rating ───────────────────────────────────────────────────────────
@Composable
private fun StarRatingRow(rating: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            Icon(
                imageVector = if (index < rating.toInt()) Icons.Filled.Star
                else Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "${"%.1f".format(rating)} Rating",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

// ─── Action Buttons ────────────────────────────────────────────────────────
@Composable
private fun ActionButtonsSection(
    onUpdateProgress: () -> Unit,
    onAddToList: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = onUpdateProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ProfileColors.SurfaceStrong,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.TrendingUp,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "UPDATE PROGRESS",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        OutlinedButton(
            onClick = onAddToList,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.BookmarkBorder,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Add to List",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp)
            )
        }
    }
}

// ─── Progress Card ─────────────────────────────────────────────────────────
@Composable
private fun ProgressCard(
    currentPage: Int,
    totalPages: Int,
    percentage: Int,
    onSliderChange: (Float) -> Unit,
    onSave: () -> Unit
) {
    val cardBg = ProfileColors.SurfaceStrong
    val trackBg = Color.White.copy(alpha = 0.2f)
    val saveBg = Color.White.copy(alpha = 0.1f)
    val accentColor = Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.TrendingUp,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "UPDATE PROGRESS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "$currentPage",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
                Text(
                    text = "  of $totalPages pages",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                    color = Color.White.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Slider(
                value = currentPage.toFloat(),
                onValueChange = onSliderChange,
                valueRange = 0f..totalPages.toFloat(),
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = accentColor,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = trackBg
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = saveBg)
            ) {
                Text(
                    text = "Save Progress",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
                    color = Color.White
                )
            }
        }
    }
}

// ─── Friends Section ───────────────────────────────────────────────────────
@Composable
private fun FriendsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "FRIENDS WHO READ THIS",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 1.3.sp,
                fontWeight = FontWeight.Bold
            ),
            color = ProfileColors.SecondaryText.copy(alpha = 0.85f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .offset(x = (-index * 8).dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                        .background(
                            listOf(
                                Color(0xFF7B9EA8),
                                Color(0xFFB8956A),
                                Color(0xFF8FA882)
                            )[index]
                        )
                )
            }
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Marcus, Elena, and 12 others\nhave read this volume.",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp, lineHeight = 17.sp),
                color = ProfileColors.SecondaryText
            )
        }
    }
}

// ─── Reviews Header ────────────────────────────────────────────────────────
@Composable
private fun ReviewsHeader(onWriteReview: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Community Reviews",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
            color = ProfileColors.PrimaryText
        )
        TextButton(onClick = onWriteReview) {
            Text(
                text = "Write a review",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                color = ProfileColors.Accent
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Outlined.Create,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = ProfileColors.Accent
            )
        }
    }
}

// ─── Review Card ───────────────────────────────────────────────────────────
@Composable
private fun ReviewCard(review: ReviewUiModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ProfileColors.Surface),
        border = BorderStroke(1.dp, ProfileColors.Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(ProfileColors.AccentSoft),
                    contentAlignment = Alignment.Center
                ) {
                    if (LocalInspectionMode.current || review.reviewerAvatar == null) {
                        Text(
                            text = review.reviewerName.first().toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = ProfileColors.SurfaceStrong
                        )
                    } else {
                        AsyncImage(
                            model = review.reviewerAvatar,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.reviewerName,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                        color = ProfileColors.Accent
                    )
                    StarRatingRow(rating = review.rating)
                }
                Text(
                    text = review.createdAt,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = ProfileColors.SecondaryText
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = review.text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                ),
                color = ProfileColors.PrimaryText
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(
                color = ProfileColors.Divider,
                thickness = 0.5.dp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.ThumbUp,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = ProfileColors.SecondaryText
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = review.likes.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = ProfileColors.SecondaryText
                    )
                }
            }
        }
    }
}

// ─── Book Metadata Section ─────────────────────────────────────────────────
@Composable
private fun BookMetadataSection(
    isbn: String,
    totalPages: Int,
    language: String,
    published: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        MetadataRow(label = "ISBN", value = isbn)
        HorizontalDivider(color = ProfileColors.Divider, modifier = Modifier.padding(vertical = 14.dp))
        MetadataRow(label = "PUBLISHED", value = published)
        HorizontalDivider(color = ProfileColors.Divider, modifier = Modifier.padding(vertical = 14.dp))
        MetadataRow(label = "PAGES", value = totalPages.toString())
        HorizontalDivider(color = ProfileColors.Divider, modifier = Modifier.padding(vertical = 14.dp))
        MetadataRow(label = "LANGUAGE", value = language)
    }
}

@Composable
private fun MetadataRow(
    label: String,
    value: String,
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 10.sp,
                letterSpacing = 1.2.sp
            ),
            color = ProfileColors.SecondaryText
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 18.sp),
            color = ProfileColors.PrimaryText
        )
    }
}

@Composable
private fun PullQuoteSection(
    description: String,
    genre: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ProfileColors.Surface)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\"${description.ifBlank { "A book worth keeping close." }}\"",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontStyle = FontStyle.Italic
                ),
                color = ProfileColors.SecondaryText
            )
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, ProfileColors.Border),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ProfileColors.PrimaryText,
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = genre.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

// ─── Loading & Error ───────────────────────────────────────────────────────
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileColors.Background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Reintentar")
            }
        }
    }
}

// ─── Previews ──────────────────────────────────────────────────────────────
@Preview(showBackground = true, name = "Light", apiLevel = 34, heightDp = 1800)
@Composable
private fun BookDetailLightPreview() {
    FeedBookTheme(darkTheme = false, dynamicColor = false) {
        BookDetailScreen(
            state = BookDetailPreviewData.sampleState,
            onRetry = {},
            onBackClick = {}
        )
    }
}

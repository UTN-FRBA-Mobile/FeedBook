package com.example.feedbook.features.books.presentation.detail

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Settings
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
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.features.profile.presentation.components.BottomBarTab
import com.example.feedbook.features.profile.presentation.components.ProfileBottomBar
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileTopBar

// ─── Stateful Wrapper ──────────────────────────────────────────────────────
@Composable
fun BookDetailScreen(
    modifier: Modifier = Modifier,
    viewModelFactory: ViewModelProvider.Factory,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
) {
    val viewModel: BookDetailViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookDetailScreen(
        state = state,
        onRetry = viewModel::loadBook,
        onBackClick = onBackClick,
        onProfileClick = onProfileClick,
        onLibraryClick = onLibraryClick,
        onStatsClick = onStatsClick,
        onNotificationsClick = onNotificationsClick,
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
    onProfileClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onModeSelected: (String) -> Unit = {},
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ProfileColors.Background,
        topBar = {
            ProfileTopBar(
                variant = com.example.feedbook.features.profile.presentation.ProfileVariant.OWN,
                avatarStyle = state.avatarStyle,
                avatarPreset = state.avatarPreset,
                avatarImageUri = state.avatarImageUri,
                onAvatarClick = onProfileClick,
                trailingContent = { iconSize ->
                    Icon(
                        imageVector = Icons.Outlined.NotificationsNone,
                        contentDescription = stringResource(com.example.feedbook.R.string.notifications_icon),
                        tint = ProfileColors.SecondaryText,
                        modifier = Modifier.size(iconSize)
                    )
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = stringResource(R.string.profile_topbar_settings),
                        tint = ProfileColors.SecondaryText,
                        modifier = Modifier.size(iconSize)
                    )
                }
            )
        },
        bottomBar = {
            ProfileBottomBar(
                activeTab = BottomBarTab.EXPLORE,
                onLibraryClick = onLibraryClick,
                onStatsClick = onStatsClick,
                onNotificationsClick = onNotificationsClick
            )
        }
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
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Cover",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
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
                containerColor = MaterialTheme.colorScheme.primary,
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
    val cardBg = MaterialTheme.colorScheme.secondary
    val trackBg = Color.White.copy(alpha = 0.2f)
    val saveBg = Color.White.copy(alpha = 0.1f)
    val accentColor = MaterialTheme.colorScheme.onSecondary

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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "FRIENDS WHO READ THIS",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .offset(x = (-index * 10).dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                listOf(
                                    Color(0xFF7B9EA8),
                                    Color(0xFFB8956A),
                                    Color(0xFF8FA882)
                                )[index]
                            )
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Marcus, Elena, and 12 others\nhave read this volume.",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 13.sp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    lineHeight = 18.sp
                )
            }
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
            style = MaterialTheme.typography.titleLarge,
        )
        TextButton(onClick = onWriteReview) {
            Text(
                text = "Write a review",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Outlined.Create,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.secondary
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (LocalInspectionMode.current || review.reviewerAvatar == null) {
                        Text(
                            text = review.reviewerName.first().toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
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
                        style = MaterialTheme.typography.titleMedium,
                    )
                    StarRatingRow(rating = review.rating)
                }
                Text(
                    text = review.createdAt,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = review.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 0.5.dp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.ThumbUp,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = review.likes.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            MetadataRow(label = "ISBN", value = isbn)
            Spacer(modifier = Modifier.height(14.dp))
            MetadataRow(
                label = "PUBLISHED",
                value = published,
            )
            Spacer(modifier = Modifier.height(14.dp))
            MetadataRow(
                label = "PAGES",
                value = totalPages.toString(),
            )
            Spacer(modifier = Modifier.height(14.dp))
            MetadataRow(
                label = "LANGUAGE",
                value = language,
            )
        }
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
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// ─── Loading & Error ───────────────────────────────────────────────────────
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
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
            .background(MaterialTheme.colorScheme.surface),
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

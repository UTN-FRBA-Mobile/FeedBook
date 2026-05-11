package com.example.feedbook.features.authors.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.features.profile.presentation.components.BottomBarTab
import com.example.feedbook.features.profile.presentation.components.ProfileBottomBar
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileTopBar
import androidx.compose.animation.animateContentSize

// ─── Stateful Wrapper ──────────────────────────────────────────────────────
@Composable
fun AuthorDetailScreen(
    modifier: Modifier = Modifier,
    viewModelFactory: ViewModelProvider.Factory,
    onBackClick: () -> Unit,
    onBookClick: (String) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
) {
    val viewModel: AuthorDetailViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    AuthorDetailScreen(
        state = state,
        onRetry = viewModel::loadAuthor,
        onBackClick = onBackClick,
        onBookClick = onBookClick,
        onFollowClick = viewModel::toggleFollow,
        onProfileClick = onProfileClick,
        onLibraryClick = onLibraryClick,
        onStatsClick = onStatsClick,
        onNotificationsClick = onNotificationsClick,
        modifier = modifier
    )
}

// ─── Stateless ─────────────────────────────────────────────────────────────
@Composable
fun AuthorDetailScreen(
    modifier: Modifier = Modifier,
    state: AuthorDetailUiState,
    onRetry: () -> Unit,
    onBackClick: () -> Unit,
    onBookClick: (String) -> Unit = {},
    onFollowClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
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
            )
        },
        bottomBar = {
            ProfileBottomBar(
                activeTab = BottomBarTab.EXPLORE,
                onProfileClick = onProfileClick,
                onLibraryClick = onLibraryClick,
                onStatsClick = onStatsClick,
                onNotificationsClick = onNotificationsClick
            )
        }
    ) { innerPadding ->
        when {
            state.isLoading -> LoadingContent(modifier = Modifier.padding(innerPadding))
            state.error != null -> ErrorContent(
                message = state.error,
                onRetry = onRetry,
                modifier = Modifier.padding(innerPadding)
            )
            state.author != null -> AuthorDetailContent(
                author = state.author,
                onBookClick = onBookClick,
                onFollowClick = onFollowClick,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

// ─── Main Content ──────────────────────────────────────────────────────────
@Composable
private fun AuthorDetailContent(
    modifier: Modifier = Modifier,
    author: AuthorUiModel,
    onBookClick: (String) -> Unit,
    onFollowClick: () -> Unit,
    onSeeAllBooks: () -> Unit = {},
) {
    var showFullBio by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Foto + nombre + años + descripción corta
        item { AuthorHeaderSection(author = author, onFollowClick = onFollowClick) }

        // Biografía
        item {
            BiographySection(
                biography = author.biography,
                showFull = showFullBio,
                onToggle = { showFullBio = !showFullBio }
            )
        }

        // Obras destacadas
        item {
            BooksHeader(onSeeAll = onSeeAllBooks)
        }

        items(author.books) { book ->
            BookRow(book = book, onClick = { onBookClick(book.id) })
        }

        // Lectores que siguen
        item { FollowersSection(followersText = author.followersText) }
    }
}

// ─── Author Header ─────────────────────────────────────────────────────────
@Composable
private fun AuthorHeaderSection(
    author: AuthorUiModel,
    onFollowClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Foto
        Box(
            modifier = Modifier
                .width(160.dp)
                .height(240.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (LocalInspectionMode.current || author.imageUrl.isNullOrBlank()) {
                Text(
                    text = author.name.first().toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                AsyncImage(
                    model = author.imageUrl,
                    contentDescription = author.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre — serif grande como en la imagen
        Text(
            text = author.name,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 38.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Años de vida — acento dorado/marrón
        Text(
            text = author.lifespan,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
            color = MaterialTheme.colorScheme.secondary  // FeedBrown
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Descripción corta
        Text(
            text = author.description,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Fila: "Biografía" + botón Seguir
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Biografía",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                ),
            )

            Button(
                onClick = onFollowClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (author.isFollowing) "Siguiendo" else "Seguir Autor",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
            }
        }
    }
}

// ─── Biography Section ─────────────────────────────────────────────────────
@Composable
private fun BiographySection(
    biography: String,
    showFull: Boolean,
    onToggle: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        Text(
            text = biography,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp, lineHeight = 24.sp),
            maxLines = if (showFull) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.animateContentSize(),
        )
        TextButton(onClick = onToggle) {
            Text(
                text = if (showFull) "Ver menos" else "Ver más",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

// ─── Books Header ───────────────────────────────────────────────────────────
@Composable
private fun BooksHeader(onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Obras Destacadas",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
        )
        TextButton(onClick = onSeeAll) {
            Text(
                text = "Ver todas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// ─── Book Row ──────────────────────────────────────────────────────────────
@Composable
private fun BookRow(book: AuthorBookUiModel, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Portada
            Box(
                modifier = Modifier
                    .width(44.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                if (!book.coverUrl.isNullOrBlank() && !LocalInspectionMode.current) {
                    AsyncImage(
                        model = book.coverUrl,
                        contentDescription = book.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Título y género
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = book.genreAndYear,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 13.sp),
                )
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ─── Followers Section ─────────────────────────────────────────────────────
@Composable
private fun FollowersSection(followersText: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Lectores que siguen a este autor",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                ),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = followersText,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 13.sp),
            )
            Spacer(modifier = Modifier.height(10.dp))
            // Avatares decorativos
            Row(verticalAlignment = Alignment.CenterVertically) {
                val avatarColors = listOf(
                    Color(0xFF7B9EA8), Color(0xFFB8956A), Color(0xFF8FA882),
                    Color(0xFF9B7EB8), Color(0xFFB87E7E)
                )
                avatarColors.forEachIndexed { index, color ->
                    Box(
                        modifier = Modifier
                            .offset(x = (-index * 10).dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "+99",
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

// ─── Loading & Error ───────────────────────────────────────────────────────
@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
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
@Preview(showBackground = true, name = "Light", apiLevel = 34, heightDp = 1200)
@Composable
private fun AuthorDetailLightPreview() {
    FeedBookTheme(darkTheme = false, dynamicColor = false) {
        AuthorDetailScreen(
            state = AuthorDetailPreviewData.sampleState,
            onRetry = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Loading", apiLevel = 34)
@Composable
private fun AuthorDetailLoadingPreview() {
    FeedBookTheme(darkTheme = false, dynamicColor = false) {
        AuthorDetailScreen(
            state = AuthorDetailPreviewData.loadingState,
            onRetry = {},
            onBackClick = {}
        )
    }
}
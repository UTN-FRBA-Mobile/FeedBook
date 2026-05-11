package com.example.feedbook.features.books.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.feedbook.core.ui.components.BottomBarTab
import com.example.feedbook.core.ui.components.FeedBookScreenScaffold
import com.example.feedbook.core.ui.components.RemoteBookCover
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.features.authors.domain.model.Author
import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.profile.presentation.ProfileVariant
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTypography

private enum class ExploreFilter {
    ALL,
    BOOKS,
    AUTHORS,
    USERS
}

@Composable
fun BookListScreen(
    onBookClick: (String) -> Unit,
    viewModelFactory: ViewModelProvider.Factory,
    onAuthorClick: (String) -> Unit = {},
    onFeedClick: () -> Unit = {},
    onExploreClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel: BookListViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookListContent(
        state = state,
        onBookClick = onBookClick,
        onAuthorClick = onAuthorClick,
        onFeedClick = onFeedClick,
        onExploreClick = onExploreClick,
        onProfileClick = onProfileClick,
        onLibraryClick = onLibraryClick,
        onStatsClick = onStatsClick,
        onNotificationsClick = onNotificationsClick,
        onLogoutClick = onLogoutClick,
        onRetry = viewModel::loadBooks,
        modifier = modifier
    )
}

@Composable
fun BookListContent(
    state: BookListState,
    onBookClick: (String) -> Unit,
    onAuthorClick: (String) -> Unit,
    onFeedClick: () -> Unit,
    onExploreClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLibraryClick: () -> Unit,
    onStatsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var query by rememberSaveable { mutableStateOf("") }
    var activeFilter by rememberSaveable { mutableStateOf(ExploreFilter.ALL) }

    val filteredBooks = remember(state.books, query) {
        state.books.filter { book ->
            val search = query.trim().lowercase()
            if (search.isBlank()) {
                true
            } else {
                listOf(book.title, book.author, book.genre, book.description)
                    .any { candidate -> candidate.lowercase().contains(search) }
            }
        }
    }

    val filteredAuthors = remember(state.authors, query) {
        state.authors.filter { author ->
            val search = query.trim().lowercase()
            if (search.isBlank()) {
                true
            } else {
                listOf(author.name, author.nationality, author.description)
                    .any { candidate -> candidate.lowercase().contains(search) }
            }
        }
    }

    val recentTags = remember(state.books, state.authors) {
        buildList {
            state.books.firstOrNull()?.genre?.takeIf { it.isNotBlank() }?.let(::add)
            state.authors.firstOrNull()?.name?.substringAfterLast(" ")?.let(::add)
            state.books.getOrNull(1)?.genre?.takeIf { it.isNotBlank() }?.let(::add)
        }.distinct().take(3)
    }

    LaunchedEffect(state.error) {
        state.error?.let { snackbarHostState.showSnackbar(it) }
    }

    FeedBookScreenScaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ProfileColors.Background),
        variant = ProfileVariant.OWN,
        activeTab = BottomBarTab.EXPLORE,
        avatarStyle = com.example.feedbook.features.profile.presentation.AvatarStyle(
            topColor = Color(0xFF315A73),
            bottomColor = Color(0xFFF0C6A8)
        ),
        onAvatarClick = onProfileClick,
        onFeedClick = onFeedClick,
        onExploreClick = onExploreClick,
        onLibraryClick = onLibraryClick,
        onStatsClick = onStatsClick,
        onNotificationsClick = onNotificationsClick,
        onLogoutClick = onLogoutClick,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ProfileColors.SurfaceStrong)
                }
            }

            state.books.isEmpty() && state.authors.isEmpty() && state.error == null -> {
                EmptyBookList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onRetry = onRetry
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ProfileColors.Background)
                        .padding(innerPadding),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item {
                        ExploreSearchField(
                            query = query,
                            onQueryChange = { query = it }
                        )
                    }
                    item {
                        ExploreFilterRow(
                            activeFilter = activeFilter,
                            onFilterSelected = { activeFilter = it }
                        )
                    }
                    if (recentTags.isNotEmpty()) {
                        item {
                            RecentTagsSection(tags = recentTags)
                        }
                    }
                    if (activeFilter != ExploreFilter.AUTHORS && filteredBooks.isNotEmpty()) {
                        item {
                            ExploreSectionHeader(
                                title = "Libros",
                                actionLabel = "Ver todos",
                                onActionClick = { activeFilter = ExploreFilter.BOOKS }
                            )
                        }
                    }
                    items(
                        items = if (activeFilter == ExploreFilter.AUTHORS) emptyList() else filteredBooks,
                        key = { book -> book.id }
                    ) { book ->
                        ExploreBookCard(
                            book = book,
                            onClick = { selectedBook -> onBookClick(selectedBook.id) }
                        )
                    }
                    if (activeFilter != ExploreFilter.BOOKS && filteredAuthors.isNotEmpty()) {
                        item {
                            ExploreSectionHeader(title = "Autores")
                        }
                        item {
                            AuthorsGrid(
                                authors = filteredAuthors,
                                onAuthorClick = onAuthorClick
                            )
                        }
                    }
                    if (activeFilter == ExploreFilter.USERS) {
                        item {
                            ComingSoonCard(
                                title = "Usuarios",
                                description = "La búsqueda de usuarios todavía no está disponible."
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyBookList(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onRetry) {
            Text("Retry", style = ProfileTypography.Body)
        }
    }
}

@Composable
private fun ExploreSearchField(
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = false,
        minLines = 2,
        shape = RoundedCornerShape(8.dp),
        placeholder = {
            Text(
                text = "Buscar títulos,\nautores, o temas...",
                style = ProfileTypography.Body,
                color = ProfileColors.SecondaryText
            )
        },
        textStyle = ProfileTypography.Body.copy(color = ProfileColors.PrimaryText),
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = ProfileColors.SecondaryText
            )
        },
        trailingIcon = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = Icons.Outlined.QrCodeScanner,
                    contentDescription = null,
                    tint = ProfileColors.SecondaryText
                )
                Icon(
                    imageVector = Icons.Outlined.Tune,
                    contentDescription = null,
                    tint = ProfileColors.SecondaryText
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF2F0F0),
            unfocusedContainerColor = Color(0xFFF2F0F0),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = ProfileColors.SurfaceStrong
        )
    )
}

@Composable
private fun ExploreFilterRow(
    activeFilter: ExploreFilter,
    onFilterSelected: (ExploreFilter) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ExploreFilter.entries.forEach { filter ->
            FilterChip(
                selected = filter == activeFilter,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = when (filter) {
                            ExploreFilter.ALL -> "Todo"
                            ExploreFilter.BOOKS -> "Libros"
                            ExploreFilter.AUTHORS -> "Autores"
                            ExploreFilter.USERS -> "Usuarios"
                        },
                        style = ProfileTypography.Label
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ProfileColors.SurfaceStrong,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = ProfileColors.SecondaryText
                )
            )
        }
    }
}

@Composable
private fun RecentTagsSection(tags: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "RECIENTES",
            style = ProfileTypography.LabelUppercase.copy(fontSize = 10.sp),
            color = ProfileColors.SecondaryText
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            tags.forEach { tag ->
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = tag,
                            style = ProfileTypography.Label.copy(fontSize = 11.sp)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color.White,
                        labelColor = ProfileColors.SecondaryText
                    ),
                    border = BorderStroke(1.dp, ProfileColors.Border)
                )
            }
        }
    }
}

@Composable
private fun ExploreSectionHeader(
    title: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = ProfileTypography.SectionTitle,
            color = ProfileColors.PrimaryText
        )
        if (actionLabel != null && onActionClick != null) {
            Text(
                text = actionLabel,
                style = ProfileTypography.Label,
                color = ProfileColors.SurfaceStrong,
                modifier = Modifier.clickable(onClick = onActionClick)
            )
        }
    }
}

@Composable
private fun ExploreBookCard(
    book: Book,
    onClick: (Book) -> Unit
) {
    ProfileSurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(book) }
    ) {
        Row(verticalAlignment = Alignment.Top) {
            RemoteBookCover(
                title = book.title,
                coverImageUrl = book.coverImageUrl,
                modifier = Modifier
                    .size(width = 72.dp, height = 108.dp)
                    .clip(RoundedCornerShape(2.dp)),
                fallbackBackground = Color(0xFF0E1820)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = book.title,
                    style = ProfileTypography.SectionTitle,
                    color = ProfileColors.PrimaryText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.author,
                    style = ProfileTypography.Body,
                    color = ProfileColors.SecondaryText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                MetaPill(text = book.genre)
            }
        }
    }
}

@Composable
private fun MetaPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(ProfileColors.AccentSoft)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = ProfileTypography.Label.copy(fontSize = 10.sp),
            color = ProfileColors.SecondaryText
        )
    }
}

@Composable
private fun AuthorsGrid(
    authors: List<Author>,
    onAuthorClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        authors.chunked(2).forEach { rowAuthors ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowAuthors.forEach { author ->
                    ExploreAuthorCard(
                        author = author,
                        modifier = Modifier.weight(1f),
                        onClick = { onAuthorClick(author.id) }
                    )
                }
                if (rowAuthors.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ExploreAuthorCard(
    author: Author,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ProfileSurfaceCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (!author.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = author.imageUrl,
                    contentDescription = author.name,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFDDE6F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = author.name.split(" ").take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString(""),
                        style = ProfileTypography.Body.copy(fontWeight = FontWeight.Bold),
                        color = ProfileColors.SurfaceStrong
                    )
                }
            }
            Text(
                text = author.name,
                style = ProfileTypography.Body.copy(
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = ProfileColors.PrimaryText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${author.books.size} Obras",
                style = ProfileTypography.Label,
                color = ProfileColors.SecondaryText
            )
            OutlinedButton(
                onClick = onClick,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Seguir",
                    style = ProfileTypography.Label,
                    color = ProfileColors.SecondaryText
                )
            }
        }
    }
}

@Composable
private fun ComingSoonCard(
    title: String,
    description: String
) {
    ProfileSurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = title,
                style = ProfileTypography.SectionTitle,
                color = ProfileColors.PrimaryText
            )
            Text(
                text = description,
                style = ProfileTypography.Body,
                color = ProfileColors.SecondaryText
            )
        }
    }
}

@Preview(apiLevel = 36, showBackground = true)
@Composable
fun BookListContentPreview() {
    val mockBooks = listOf(
        Book(
            "1",
            "The Great Gatsby",
            "F. Scott Fitzgerald",
            "A story of wealth and love",
            coverImageUrl = null,
            isbn = "9788445015407",
            language = "English",
            genre = "Suspense",
            pages = 512,
            published = "March 27, 1921"
        ),
        Book(
            "2",
            "1984",
            "George Orwell",
            "A dystopian future",
            coverImageUrl = null,
            isbn = "9788445015207",
            language = "English",
            genre = "Suspense",
            pages = 212,
            published = "March 27, 1925"
        )
    )
    val mockAuthors = listOf(
        Author(
            id = "1",
            name = "Jorge Luis Borges",
            birthYear = 1899,
            deathYear = 1986,
            nationality = "Argentina",
            description = "Short stories and essays",
            biography = "",
            imageUrl = null,
            books = mockBooks,
            followers = 1200
        ),
        Author(
            id = "2",
            name = "Gabriel Garcia Marquez",
            birthYear = 1927,
            deathYear = 2014,
            nationality = "Colombia",
            description = "Magical realism",
            biography = "",
            imageUrl = null,
            books = mockBooks,
            followers = 900
        )
    )
    FeedBookTheme(dynamicColor = false) {
        BookListContent(
            state = BookListState(books = mockBooks, authors = mockAuthors),
            onBookClick = {},
            onAuthorClick = {},
            onFeedClick = {},
            onExploreClick = {},
            onProfileClick = {},
            onLibraryClick = {},
            onStatsClick = {},
            onNotificationsClick = {},
            onLogoutClick = {},
            onRetry = {}
        )
    }
}

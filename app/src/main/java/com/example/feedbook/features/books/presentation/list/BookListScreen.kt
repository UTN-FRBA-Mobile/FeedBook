package com.example.feedbook.features.books.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.feedbook.R
import com.example.feedbook.core.ui.components.BottomBarTab
import com.example.feedbook.core.ui.components.FeedBookScreenScaffold
import com.example.feedbook.core.ui.components.RemoteBookCover
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.features.authors.domain.model.Author
import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.domain.model.ExploreUser
import com.example.feedbook.features.profile.presentation.ProfileVariant
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.components.ProfileAvatarArtwork
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTypography

private enum class ExploreFilter {
    ALL,
    BOOKS,
    AUTHORS,
    USERS
}

private data class ExploreFacetOption(
    val value: String,
    val count: Int
)

@Composable
fun BookListScreen(
    onBookClick: (String) -> Unit,
    viewModelFactory: ViewModelProvider.Factory,
    onAuthorClick: (String) -> Unit = {},
    onUserClick: (String) -> Unit = {},
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
        onUserClick = onUserClick,
        onFeedClick = onFeedClick,
        onExploreClick = onExploreClick,
        onProfileClick = onProfileClick,
        onLibraryClick = onLibraryClick,
        onStatsClick = onStatsClick,
        onNotificationsClick = onNotificationsClick,
        onLogoutClick = onLogoutClick,
        onRetry = viewModel::loadBooks,
        onSearchChange = viewModel::updateSearch,
        modifier = modifier
    )
}

@Composable
fun BookListContent(
    state: BookListState,
    onBookClick: (String) -> Unit,
    onAuthorClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onFeedClick: () -> Unit,
    onExploreClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLibraryClick: () -> Unit,
    onStatsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRetry: () -> Unit,
    onSearchChange: (String, Set<String>, Set<String>) -> Unit = { _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var activeFilter by remember { mutableStateOf(ExploreFilter.ALL) }
    var showFilterSheet by remember { mutableStateOf(false) }
    val query = state.query
    val selectedGenres = state.selectedGenres
    val selectedAuthors = state.selectedAuthors

    val availableGenres = remember(state.books) {
        buildGenreFacetOptions(state.books)
    }

    val availableAuthorNames = remember(state.books, state.authors) {
        buildAuthorFacetOptions(state.books, state.authors)
    }

    val filteredBooks = state.books

    val filteredAuthors = remember(state.authors, selectedAuthors) {
        if (selectedAuthors.isEmpty()) {
            state.authors
        } else {
            state.authors.filter { author -> selectedAuthors.contains(author.name) }
        }
    }

    val filteredUsers = state.users

    val showingBooks = activeFilter == ExploreFilter.ALL || activeFilter == ExploreFilter.BOOKS
    val showingAuthors = activeFilter == ExploreFilter.ALL || activeFilter == ExploreFilter.AUTHORS
    val showingUsers = activeFilter == ExploreFilter.ALL || activeFilter == ExploreFilter.USERS
    val hasVisibleResults = (showingBooks && filteredBooks.isNotEmpty()) ||
        (showingAuthors && filteredAuthors.isNotEmpty()) ||
        (showingUsers && filteredUsers.isNotEmpty())

    LaunchedEffect(state.error) {
        state.error?.let { snackbarHostState.showSnackbar(it) }
    }

    if (showFilterSheet) {
        ExploreFilterSheet(
            genres = availableGenres,
            authors = availableAuthorNames,
            selectedGenres = selectedGenres,
            selectedAuthors = selectedAuthors,
            onGenreToggled = { genre ->
                val updatedGenres = selectedGenres.toMutableSet().apply {
                    if (contains(genre)) remove(genre) else add(genre)
                }
                onSearchChange(query, updatedGenres, selectedAuthors)
            },
            onAuthorToggled = { author ->
                val updatedAuthors = selectedAuthors.toMutableSet().apply {
                    if (contains(author)) remove(author) else add(author)
                }
                onSearchChange(query, selectedGenres, updatedAuthors)
            },
            onClearFilters = {
                onSearchChange(query, emptySet(), emptySet())
            },
            onDismiss = { showFilterSheet = false }
        )
    }

    FeedBookScreenScaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ProfileColors.Background),
        variant = ProfileVariant.OWN,
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

            state.books.isEmpty() && state.authors.isEmpty() && state.users.isEmpty() && state.error == null -> {
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
                            onQueryChange = { updatedQuery ->
                                onSearchChange(updatedQuery, selectedGenres, selectedAuthors)
                            },
                            onFilterClick = { showFilterSheet = true }
                        )
                    }
                    if (state.isRefreshing) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    color = ProfileColors.SurfaceStrong,
                                    modifier = Modifier.size(22.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    }
                    item {
                        ExploreFilterRow(
                            activeFilter = activeFilter,
                            onFilterSelected = { activeFilter = it }
                        )
                    }
                    if (selectedGenres.isNotEmpty() || selectedAuthors.isNotEmpty()) {
                        item {
                            ActiveExploreFilters(
                                selectedGenres = selectedGenres,
                                selectedAuthors = selectedAuthors,
                                onClearGenre = { genre ->
                                    onSearchChange(query, selectedGenres - genre, selectedAuthors)
                                },
                                onClearAuthor = { author ->
                                    onSearchChange(query, selectedGenres, selectedAuthors - author)
                                },
                                onClearAll = {
                                    onSearchChange(query, emptySet(), emptySet())
                                }
                            )
                        }
                    }
                    if (showingBooks && filteredBooks.isNotEmpty()) {
                        item {
                            ExploreSectionHeader(
                                title = stringResource(R.string.explore_books),
                                actionLabel = if (activeFilter == ExploreFilter.ALL) stringResource(R.string.explore_section_view_all) else null,
                                onActionClick = if (activeFilter == ExploreFilter.ALL) ({ activeFilter = ExploreFilter.BOOKS }) else null
                            )
                        }
                    }
                    items(items = if (showingBooks) filteredBooks else emptyList(), key = { book -> book.id }) { book ->
                        ExploreBookCard(
                            book = book,
                            onClick = { selectedBook -> onBookClick(selectedBook.id) }
                        )
                    }
                    if (showingAuthors && filteredAuthors.isNotEmpty()) {
                        item {
                            ExploreSectionHeader(
                                title = stringResource(R.string.explore_authors),
                                actionLabel = if (activeFilter == ExploreFilter.ALL) stringResource(R.string.explore_section_view_all) else null,
                                onActionClick = if (activeFilter == ExploreFilter.ALL) ({ activeFilter = ExploreFilter.AUTHORS }) else null
                            )
                        }
                    }
                    items(items = if (showingAuthors) filteredAuthors else emptyList(), key = { author -> author.id }) { author ->
                        ExploreAuthorCard(
                            author = author,
                            onClick = { onAuthorClick(author.id) }
                        )
                    }
                    if (showingUsers && filteredUsers.isNotEmpty()) {
                        item {
                            ExploreSectionHeader(
                                title = stringResource(R.string.explore_users),
                                actionLabel = if (activeFilter == ExploreFilter.ALL) stringResource(R.string.explore_section_view_all) else null,
                                onActionClick = if (activeFilter == ExploreFilter.ALL) ({ activeFilter = ExploreFilter.USERS }) else null
                            )
                        }
                    }
                    items(items = if (showingUsers) filteredUsers else emptyList(), key = { user -> user.id }) { user ->
                        ExploreUserCard(
                            user = user,
                            onClick = { onUserClick(user.id) }
                        )
                    }
                    if (!hasVisibleResults) {
                        item {
                            EmptySearchResults(
                                query = query,
                                activeFilter = activeFilter
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
            Text(stringResource(R.string.common_retry), style = ProfileTypography.Body)
        }
    }
}

@Composable
private fun ExploreSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        placeholder = {
            Text(
                text = stringResource(R.string.explore_search_hint),
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
                    imageVector = Icons.Outlined.Tune,
                    contentDescription = null,
                    tint = ProfileColors.SecondaryText,
                    modifier = Modifier.clickable(onClick = onFilterClick)
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = ProfileColors.AccentSoft,
            unfocusedContainerColor = ProfileColors.AccentSoft,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = ProfileColors.SurfaceStrong
        )
    )
}

@Composable
private fun ActiveExploreFilters(
    selectedGenres: Set<String>,
    selectedAuthors: Set<String>,
    onClearGenre: (String) -> Unit,
    onClearAuthor: (String) -> Unit,
    onClearAll: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.explore_active_filters),
                style = ProfileTypography.LabelUppercase.copy(fontSize = 10.sp),
                color = ProfileColors.SecondaryText
            )
            Text(
                text = stringResource(R.string.explore_clear_all),
                style = ProfileTypography.Label,
                color = ProfileColors.Accent,
                modifier = Modifier.clickable(onClick = onClearAll)
            )
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedGenres.forEach { genre ->
                AssistChip(
                    onClick = { onClearGenre(genre) },
                    label = { Text(text = stringResource(R.string.explore_genre_chip, genre), style = ProfileTypography.Label.copy(fontSize = 11.sp)) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = ProfileColors.AccentSoft,
                        labelColor = ProfileColors.PrimaryText
                    ),
                    border = BorderStroke(1.dp, ProfileColors.Border)
                )
            }
            selectedAuthors.forEach { author ->
                AssistChip(
                    onClick = { onClearAuthor(author) },
                    label = { Text(text = stringResource(R.string.explore_author_chip, author), style = ProfileTypography.Label.copy(fontSize = 11.sp)) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = ProfileColors.AccentSoft,
                        labelColor = ProfileColors.PrimaryText
                    ),
                    border = BorderStroke(1.dp, ProfileColors.Border)
                )
            }
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun ExploreFilterSheet(
    genres: List<ExploreFacetOption>,
    authors: List<ExploreFacetOption>,
    selectedGenres: Set<String>,
    selectedAuthors: Set<String>,
    onGenreToggled: (String) -> Unit,
    onAuthorToggled: (String) -> Unit,
    onClearFilters: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ProfileColors.Surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.explore_filter_results),
                    style = ProfileTypography.SectionTitle,
                    color = ProfileColors.PrimaryText
                )
                TextButton(onClick = onClearFilters) {
                    Text(
                        text = stringResource(R.string.explore_clear),
                        style = ProfileTypography.Label,
                        color = ProfileColors.Accent
                    )
                }
            }

            ExploreMultiSelectFilterGroup(
                title = stringResource(R.string.explore_genres),
                options = genres,
                selectedOptions = selectedGenres,
                onOptionToggled = onGenreToggled
            )

            ExploreMultiSelectFilterGroup(
                title = stringResource(R.string.explore_authors),
                options = authors,
                selectedOptions = selectedAuthors,
                onOptionToggled = onAuthorToggled
            )

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = ProfileColors.SurfaceStrong,
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(R.string.explore_apply), style = ProfileTypography.Button)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ExploreMultiSelectFilterGroup(
    title: String,
    options: List<ExploreFacetOption>,
    selectedOptions: Set<String>,
    onOptionToggled: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            style = ProfileTypography.LabelUppercase.copy(fontSize = 10.sp),
            color = ProfileColors.SecondaryText
        )
        if (options.isEmpty()) {
            Text(
                text = stringResource(R.string.explore_no_options),
                style = ProfileTypography.Body.copy(fontSize = 13.sp),
                color = ProfileColors.SecondaryText
            )
        } else {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
            options.forEach { option ->
                FilterChip(
                    selected = selectedOptions.contains(option.value),
                    onClick = { onOptionToggled(option.value) },
                    label = {
                        Text(
                            text = "${option.value} (${option.count})",
                            style = ProfileTypography.Label.copy(fontSize = 12.sp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ProfileColors.SurfaceStrong,
                        selectedLabelColor = Color.White,
                        containerColor = ProfileColors.Background,
                        labelColor = ProfileColors.SecondaryText
                    )
                )
            }
            }
        }
    }
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
                            ExploreFilter.ALL -> stringResource(R.string.explore_all)
                            ExploreFilter.BOOKS -> stringResource(R.string.explore_books_label)
                            ExploreFilter.AUTHORS -> stringResource(R.string.explore_authors_label)
                            ExploreFilter.USERS -> stringResource(R.string.explore_users_label)
                        },
                        style = ProfileTypography.Label
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ProfileColors.SurfaceStrong,
                    selectedLabelColor = Color.White,
                    containerColor = ProfileColors.Surface,
                    labelColor = ProfileColors.SecondaryText
                )
            )
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
                Text(
                    text = book.description,
                    style = ProfileTypography.Body.copy(fontSize = 13.sp, lineHeight = 19.sp),
                    color = ProfileColors.SecondaryText,
                    maxLines = 3,
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
private fun ExploreAuthorCard(
    author: Author,
    onClick: () -> Unit
) {
    ProfileSurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (!author.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = author.imageUrl,
                    contentDescription = author.name,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ProfileColors.AccentSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = author.name.split(" ").take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString(""),
                        style = ProfileTypography.SectionTitle.copy(fontWeight = FontWeight.Bold),
                        color = ProfileColors.SurfaceStrong
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = author.name,
                    style = ProfileTypography.SectionTitle.copy(fontSize = 22.sp),
                    color = ProfileColors.PrimaryText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = author.nationality,
                    style = ProfileTypography.LabelUppercase.copy(fontSize = 10.sp),
                    color = ProfileColors.Accent
                )
                Text(
                    text = author.description,
                    style = ProfileTypography.Body.copy(fontSize = 13.sp, lineHeight = 19.sp),
                    color = ProfileColors.SecondaryText,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MetaPill(text = "${author.books.size} obras")
                    MetaPill(text = "${author.followers} seguidores")
                }
            }
        }
    }
}

@Composable
private fun ExploreUserCard(
    user: ExploreUser,
    onClick: () -> Unit
) {
    ProfileSurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            ProfileAvatarArtwork(
                avatarStyle = AvatarStyle(
                    topColor = Color(user.avatarTopColorHex),
                    bottomColor = Color(user.avatarBottomColorHex)
                ),
                avatarPreset = null,
                avatarImageUri = user.avatarImageUrl,
                modifier = Modifier.size(72.dp),
                imageShape = RoundedCornerShape(20.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = user.name,
                    style = ProfileTypography.SectionTitle.copy(fontSize = 22.sp),
                    color = ProfileColors.PrimaryText
                )
                Text(
                    text = user.handle,
                    style = ProfileTypography.LabelUppercase.copy(fontSize = 10.sp),
                    color = ProfileColors.Accent
                )
                Text(
                    text = user.bio,
                    style = ProfileTypography.Body.copy(fontSize = 13.sp, lineHeight = 19.sp),
                    color = ProfileColors.SecondaryText,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MetaPill(text = user.followersLabel)
                    MetaPill(text = user.booksReadLabel)
                }
            }
        }
    }
}

@Composable
private fun EmptySearchResults(
    query: String,
    activeFilter: ExploreFilter
) {
    ProfileSurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.explore_no_results),
                style = ProfileTypography.SectionTitle,
                color = ProfileColors.PrimaryText
            )
            Text(
                text = when {
                    query.isBlank() -> stringResource(R.string.explore_no_results_for_filter)
                    activeFilter == ExploreFilter.ALL -> stringResource(R.string.explore_no_matches_all, query)
                    activeFilter == ExploreFilter.BOOKS -> stringResource(R.string.explore_no_matches_books, query)
                    activeFilter == ExploreFilter.AUTHORS -> stringResource(R.string.explore_no_matches_authors, query)
                    else -> stringResource(R.string.explore_no_matches_users, query)
                },
                style = ProfileTypography.Body.copy(fontSize = 14.sp, lineHeight = 20.sp),
                color = ProfileColors.SecondaryText
            )
        }
    }
}

private fun buildGenreFacetOptions(books: List<Book>): List<ExploreFacetOption> =
    books
        .asSequence()
        .map { it.genre.trim() }
        .filter { it.isNotBlank() }
        .groupingBy { it }
        .eachCount()
        .entries
        .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }.thenBy { it.key })
        .map { (genre, count) -> ExploreFacetOption(value = genre, count = count) }

private fun buildAuthorFacetOptions(
    books: List<Book>,
    authors: List<Author>
): List<ExploreFacetOption> {
    val bookCounts = books
        .asSequence()
        .map { it.author.trim() }
        .filter { it.isNotBlank() }
        .groupingBy { it }
        .eachCount()

    val authorCounts = authors
        .asSequence()
        .map { it.name.trim() }
        .filter { it.isNotBlank() }
        .groupingBy { it }
        .eachCount()

    return (bookCounts.keys + authorCounts.keys)
        .sorted()
        .map { authorName ->
            ExploreFacetOption(
                value = authorName,
                count = (bookCounts[authorName] ?: 0) + (authorCounts[authorName] ?: 0)
            )
        }
        .sortedWith(compareByDescending<ExploreFacetOption> { it.count }.thenBy { it.value })
}

@Preview(apiLevel = 36, showBackground = true)
@Composable
fun BookListContentPreview() {
    val mockBooks = listOf(
        Book(
            id = "1",
            authorId = "",
            title = "The Great Gatsby",
            author = "F. Scott Fitzgerald",
            description = "A story of wealth and love",
            coverImageUrl = null,
            isbn = "9788445015407",
            language = "English",
            genre = "Suspense",
            pages = 512,
            published = "March 27, 1921"
        ),
        Book(
            id = "2",
            authorId = "",
            title = "1984",
            author = "George Orwell",
            description = "A dystopian future",
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
    val mockUsers = listOf(
        ExploreUser(
            id = "u1",
            name = "Evelyn Vance",
            handle = "@evelynv",
            bio = "Reader of gothic fiction and annotated classics.",
            avatarImageUrl = null,
            avatarTopColorHex = 0xFF5B4A80,
            avatarBottomColorHex = 0xFFF0CCE9,
            followersLabel = "2.1K seguidores",
            booksReadLabel = "142 libros"
        )
    )
    FeedBookTheme(dynamicColor = false) {
        BookListContent(
            state = BookListState(books = mockBooks, authors = mockAuthors, users = mockUsers),
            onBookClick = {},
            onAuthorClick = {},
            onUserClick = {},
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

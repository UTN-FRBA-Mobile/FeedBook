package com.example.feedbook.features.books.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feedbook.core.ui.components.LoadingScreen
import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.presentation.list.components.BookCard

@Composable
fun BookListScreen(
    onBookClick: (String) -> Unit,
    viewModelFactory: ViewModelProvider.Factory,
    modifier: Modifier = Modifier
) {
    val viewModel: BookListViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookListContent(
        state = state,
        onBookClick = onBookClick,
        onRetry = viewModel::loadBooks,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListContent(
    state: BookListState,
    onBookClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("Remote Library") }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        when {
            state.isLoading -> {
                LoadingScreen(
                    modifier = Modifier.padding(innerPadding),
                    message = "Fetching books from the backend..."
                )
            }

            state.books.isEmpty() && state.error == null -> {
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
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = state.books,
                        key = { book -> book.id }
                    ) { book ->
                        BookCard(
                            book = book,
                            onClick = { selectedBook -> onBookClick(selectedBook.id) }
                        )
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
            Text("Retry")
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
    BookListContent(
        state = BookListState(books = mockBooks),
        onBookClick = {},
        onRetry = {}
    )
}

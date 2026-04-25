package com.example.feedbook.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.feedbook.FeedBookApplication
import com.example.feedbook.presentation.books.BookListScreen
import com.example.feedbook.presentation.books.BookListViewModel
import com.example.feedbook.presentation.detail.BookDetailScreen
import com.example.feedbook.presentation.detail.BookDetailViewModel

object AppRoutes {
    const val BOOKS = "books"
    const val BOOK_DETAIL = "bookDetail/{bookId}"

    fun detail(bookId: String): String = "bookDetail/$bookId"
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val application = LocalContext.current.applicationContext as FeedBookApplication
    val container = remember(application) { application.container }

    NavHost(
        navController = navController,
        startDestination = AppRoutes.BOOKS,
        modifier = modifier
    ) {
        composable(route = AppRoutes.BOOKS) {
            BookListScreen(
                viewModelFactory = BookListViewModel.provideFactory(container.getBooksUseCase),
                onBookClick = { bookId -> navController.navigate(AppRoutes.detail(bookId)) }
            )
        }

        composable(
            route = AppRoutes.BOOK_DETAIL,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId").orEmpty()
            BookDetailScreen(
                viewModelFactory = BookDetailViewModel.provideFactory(
                    bookId = bookId,
                    getBookByIdUseCase = container.getBookByIdUseCase
                ),
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

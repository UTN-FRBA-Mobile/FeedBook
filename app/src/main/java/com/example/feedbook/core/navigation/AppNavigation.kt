package com.example.feedbook.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
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
import com.example.feedbook.presentation.notifications.NotificationsScreen
import com.example.feedbook.presentation.profile.EditProfileScreen
import com.example.feedbook.presentation.profile.ProfileScreen
import com.example.feedbook.presentation.profile.sampleProfileUiState
import com.example.feedbook.presentation.profile.samplePublicProfileUiState
import com.example.feedbook.presentation.stats.StatsScreen

object AppRoutes {
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "editProfile"
    const val PUBLIC_PROFILE = "publicProfile"
    const val PUBLIC_PROFILE_PREVIEW = "publicProfilePreview"
    const val STATS = "stats"
    const val NOTIFICATIONS = "notifications"
    const val BOOKS = "books"
    const val BOOK_DETAIL = "bookDetail/{bookId}"

    fun detail(bookId: String): String = "bookDetail/$bookId"
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val application = LocalContext.current.applicationContext as FeedBookApplication
    val container = remember(application) { application.container }
    var ownProfileState by remember { mutableStateOf(sampleProfileUiState()) }
    val navigateToTopLevel: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppRoutes.PROFILE,
        modifier = modifier
    ) {
        composable(route = AppRoutes.PROFILE) {
            ProfileScreen(
                state = ownProfileState,
                onProfileClick = { navigateToTopLevel(AppRoutes.PROFILE) },
                onStatsClick = { navigateToTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navigateToTopLevel(AppRoutes.NOTIFICATIONS) },
                onEditProfileClick = { navController.navigate(AppRoutes.EDIT_PROFILE) },
                onPreviewPublicProfileClick = { navController.navigate(AppRoutes.PUBLIC_PROFILE_PREVIEW) }
            )
        }

        composable(route = AppRoutes.EDIT_PROFILE) {
            EditProfileScreen(
                state = ownProfileState,
                onBackClick = { navController.popBackStack() },
                onProfileClick = { navigateToTopLevel(AppRoutes.PROFILE) },
                onSave = { updatedState ->
                    ownProfileState = updatedState
                    navController.popBackStack()
                }
            )
        }

        composable(route = AppRoutes.PUBLIC_PROFILE) {
            ProfileScreen(
                state = samplePublicProfileUiState(),
                onProfileClick = { navigateToTopLevel(AppRoutes.PROFILE) },
                onStatsClick = { navigateToTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navigateToTopLevel(AppRoutes.NOTIFICATIONS) }
            )
        }

        composable(route = AppRoutes.PUBLIC_PROFILE_PREVIEW) {
            ProfileScreen(
                state = ownProfileState.asPublicPreview(),
                onProfileClick = { navigateToTopLevel(AppRoutes.PROFILE) },
                onStatsClick = { navigateToTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navigateToTopLevel(AppRoutes.NOTIFICATIONS) }
            )
        }

        composable(route = AppRoutes.STATS) {
            StatsScreen(
                avatarStyle = ownProfileState.avatarStyle,
                avatarImageUri = ownProfileState.avatarImageUri,
                onProfileClick = { navigateToTopLevel(AppRoutes.PROFILE) },
                onStatsClick = { navigateToTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navigateToTopLevel(AppRoutes.NOTIFICATIONS) }
            )
        }

        composable(route = AppRoutes.NOTIFICATIONS) {
            NotificationsScreen(
                avatarStyle = ownProfileState.avatarStyle,
                avatarImageUri = ownProfileState.avatarImageUri,
                onProfileClick = { navigateToTopLevel(AppRoutes.PROFILE) },
                onStatsClick = { navigateToTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navigateToTopLevel(AppRoutes.NOTIFICATIONS) }
            )
        }

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

private fun com.example.feedbook.presentation.profile.ProfileUiState.asPublicPreview() =
    copy(
        variant = com.example.feedbook.presentation.profile.ProfileVariant.PUBLIC,
        actionLabel = "FOLLOW",
        profileStats = listOf(
            com.example.feedbook.presentation.profile.ProfileStat(label = "Books read", value = completedBooks.toString()),
            com.example.feedbook.presentation.profile.ProfileStat(
                label = "Daily goal",
                value = readingGoal?.targetPagesPerDay?.let { "$it pgs" } ?: "None"
            )
        )
    )

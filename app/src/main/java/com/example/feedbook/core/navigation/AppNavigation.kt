package com.example.feedbook.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.feedbook.FeedBookApplication
import com.example.feedbook.features.auth.presentation.LoginScreen
import com.example.feedbook.features.authors.presentation.detail.AuthorDetailScreen
import com.example.feedbook.features.authors.presentation.detail.AuthorDetailViewModel
import com.example.feedbook.features.books.presentation.list.BookListScreen
import com.example.feedbook.features.books.presentation.list.BookListViewModel
import com.example.feedbook.features.books.presentation.detail.BookDetailScreen
import com.example.feedbook.features.books.presentation.detail.BookDetailViewModel
import com.example.feedbook.features.library.presentation.LibraryScreen
import com.example.feedbook.features.library.presentation.LibraryViewModel
import com.example.feedbook.features.notifications.presentation.NotificationsScreen
import com.example.feedbook.features.notifications.presentation.NotificationsViewModel
import com.example.feedbook.features.profile.presentation.EditProfileScreen
import com.example.feedbook.features.profile.presentation.EditProfileViewModel
import com.example.feedbook.features.profile.presentation.ProfileScreen
import com.example.feedbook.features.profile.presentation.ProfileViewModel
import com.example.feedbook.features.profile.presentation.PublicProfilePreviewViewModel
import com.example.feedbook.features.profile.presentation.PublicProfileViewModel
import com.example.feedbook.features.stats.presentation.StatsScreen
import com.example.feedbook.features.stats.presentation.StatsViewModel

object AppRoutes {
    const val LOGIN = "login"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "editProfile"
    const val PUBLIC_PROFILE = "publicProfile"
    const val PUBLIC_PROFILE_PREVIEW = "publicProfilePreview"
    const val LIBRARY = "library"
    const val STATS = "stats"
    const val NOTIFICATIONS = "notifications"
    const val BOOKS = "books"
    const val BOOK_DETAIL = "bookDetail/{bookId}"

    const val AUTHOR_DETAIL = "authorDetail/{authorId}"

    fun detail(bookId: String): String = "bookDetail/$bookId"
}

private fun NavHostController.navigateTopLevel(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val appContainer = (LocalContext.current.applicationContext as FeedBookApplication).container

    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN,
        modifier = modifier
    ) {
        composable(route = AppRoutes.LOGIN) {
            LoginScreen(
                onSignInClick = { navController.navigate(AppRoutes.PROFILE) }
            )
        }

        composable(route = AppRoutes.PROFILE) {
            val viewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModel.provideFactory(appContainer.observeOwnProfileUseCase)
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            ProfileScreen(
                state = state,
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onEditProfileClick = { navController.navigate(AppRoutes.EDIT_PROFILE) },
                onPreviewPublicProfileClick = { navController.navigate(AppRoutes.PUBLIC_PROFILE_PREVIEW) },
                onBookClick = { bookId -> navController.navigate(AppRoutes.detail(bookId)) },
                onRetry = viewModel::retry
            )
        }

        composable(route = AppRoutes.EDIT_PROFILE) {
            val viewModel: EditProfileViewModel = viewModel(
                factory = EditProfileViewModel.provideFactory(
                    observeOwnProfileUseCase = appContainer.observeOwnProfileUseCase,
                    updateProfileUseCase = appContainer.updateProfileUseCase
                )
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            EditProfileScreen(
                state = state,
                onBackClick = { navController.popBackStack() },
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onSave = { updatedState ->
                    viewModel.saveProfile(updatedState) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable(route = AppRoutes.PUBLIC_PROFILE) {
            val viewModel: PublicProfileViewModel = viewModel(
                factory = PublicProfileViewModel.provideFactory(appContainer.getPublicProfileUseCase)
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            ProfileScreen(
                state = state,
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onBookClick = { bookId -> navController.navigate(AppRoutes.detail(bookId)) },
                onRetry = viewModel::retry
            )
        }

        composable(route = AppRoutes.PUBLIC_PROFILE_PREVIEW) {
            val viewModel: PublicProfilePreviewViewModel = viewModel(
                factory = PublicProfilePreviewViewModel.provideFactory(
                    appContainer.observeOwnPublicProfilePreviewUseCase
                )
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            ProfileScreen(
                state = state,
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onBookClick = { bookId -> navController.navigate(AppRoutes.detail(bookId)) },
                onRetry = viewModel::retry
            )
        }

        composable(route = AppRoutes.LIBRARY) {
            val viewModel: LibraryViewModel = viewModel(
                factory = LibraryViewModel.provideFactory(appContainer.observeOwnLibraryUseCase)
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            LibraryScreen(
                state = state,
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onBookClick = { bookId -> navController.navigate(AppRoutes.detail(bookId)) },
            )
        }

        composable(route = AppRoutes.STATS) {
            val viewModel: StatsViewModel = viewModel(
                factory = StatsViewModel.provideFactory(
                    getStatsUseCase = appContainer.getStatsUseCase,
                    observeOwnProfileUseCase = appContainer.observeOwnProfileUseCase
                )
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            StatsScreen(
                state = state,
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onModeSelected = viewModel::selectMode,
                onRetry = viewModel::retry
            )
        }

        composable(route = AppRoutes.NOTIFICATIONS) {
            val viewModel: NotificationsViewModel = viewModel(
                factory = NotificationsViewModel.provideFactory(
                    getNotificationsUseCase = appContainer.getNotificationsUseCase,
                    observeOwnProfileUseCase = appContainer.observeOwnProfileUseCase
                )
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            NotificationsScreen(
                state = state,
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onRetry = viewModel::retry
            )
        }

        composable(route = AppRoutes.BOOKS) {
            BookListScreen(
                viewModelFactory = BookListViewModel.provideFactory(appContainer.getBooksUseCase),
                onBookClick = { bookId -> navController.navigate(AppRoutes.detail(bookId)) }
            )
        }

        composable(
            route = AppRoutes.BOOK_DETAIL,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            BookDetailScreen(
                viewModelFactory = BookDetailViewModel.provideFactory(
                    bookId = backStackEntry.arguments?.getString("bookId").orEmpty(),
                    getBookByIdUseCase = appContainer.getBookByIdUseCase,
                    getReviewsUseCase = appContainer.getReviewsUseCase,
                    getReadingProgressUseCase = appContainer.getReadingProgress,
                    observeOwnProfileUseCase = appContainer.observeOwnProfileUseCase
                ),
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = AppRoutes.AUTHOR_DETAIL,
            arguments = listOf(navArgument("authorId") { type = NavType.StringType })
        ) { backStackEntry ->
            AuthorDetailScreen(
                viewModelFactory = AuthorDetailViewModel.provideFactory(
                    authorId = backStackEntry.arguments?.getString("authorId").orEmpty(),
                    getAuthorByIdUseCase = appContainer.getAuthorByIdUseCase,
                    toggleFollowUseCase = appContainer.toggleAuthorFollowUseCase,
                    observeOwnProfileUseCase = appContainer.observeOwnProfileUseCase
                ),
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

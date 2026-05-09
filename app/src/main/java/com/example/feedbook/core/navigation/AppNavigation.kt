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
import com.example.feedbook.features.books.presentation.list.BookListScreen
import com.example.feedbook.features.books.presentation.list.BookListViewModel
import com.example.feedbook.features.books.presentation.detail.BookDetailScreen
import com.example.feedbook.features.books.presentation.detail.BookDetailViewModel
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
        startDestination = AppRoutes.PROFILE,
        modifier = modifier
    ) {
        composable(route = AppRoutes.PROFILE) {
            val viewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModel.provideFactory(appContainer.observeOwnProfileUseCase)
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            ProfileScreen(
                state = state,
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onEditProfileClick = { navController.navigate(AppRoutes.EDIT_PROFILE) },
                onPreviewPublicProfileClick = { navController.navigate(AppRoutes.PUBLIC_PROFILE_PREVIEW) },
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
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
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
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onRetry = viewModel::retry
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
        ) {
            BookDetailScreen(
                viewModelFactory = BookDetailViewModel.provideFactory(
                    bookId = it.arguments?.getString("bookId").orEmpty(),
                    getBookByIdUseCase = appContainer.getBookByIdUseCase
                ),
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

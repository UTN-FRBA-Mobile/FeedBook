package com.example.feedbook.core.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.example.feedbook.features.auth.presentation.AuthBiometricPrompt
import com.example.feedbook.features.auth.presentation.AuthGateDestination
import com.example.feedbook.features.auth.presentation.AuthGateUiState
import com.example.feedbook.features.auth.presentation.AuthGateViewModel
import com.example.feedbook.features.auth.presentation.LoginBiometricPrompt
import com.example.feedbook.features.auth.presentation.LoginScreen
import com.example.feedbook.features.authors.presentation.detail.AuthorDetailScreen
import com.example.feedbook.features.authors.presentation.detail.AuthorDetailViewModel
import com.example.feedbook.features.auth.presentation.LoginViewModel
import com.example.feedbook.features.books.presentation.list.BookListScreen
import com.example.feedbook.features.books.presentation.list.BookListViewModel
import com.example.feedbook.features.books.presentation.detail.BookDetailScreen
import com.example.feedbook.features.books.presentation.detail.BookDetailViewModel
import com.example.feedbook.features.home.presentation.HomeScreen
import com.example.feedbook.features.home.presentation.HomeViewModel
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
    const val AUTH_GATE = "authGate"
    const val LOGIN = "login"
    const val HOME = "home"
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
    fun authorDetail(authorId: String): String = "authorDetail/$authorId"
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
        startDestination = AppRoutes.AUTH_GATE,
        modifier = modifier
    ) {
        composable(route = AppRoutes.AUTH_GATE) {
            val viewModel: AuthGateViewModel = viewModel(
                factory = AuthGateViewModel.provideFactory(appContainer.sessionManager)
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            AuthGateScreen(
                state = state,
                onBiometricSuccess = viewModel::onBiometricSuccess,
                onBiometricError = { viewModel.onBiometricError() }
            )

            LaunchedEffect(state.destination) {
                when (state.destination) {
                    AuthGateDestination.HOME -> {
                        navController.navigate(AppRoutes.HOME) {
                            popUpTo(AppRoutes.AUTH_GATE) {
                                inclusive = true
                            }
                        }
                    }

                    AuthGateDestination.LOGIN -> {
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(AppRoutes.AUTH_GATE) {
                                inclusive = true
                            }
                        }
                    }

                    null -> Unit
                }
            }
        }

        composable(route = AppRoutes.LOGIN) {
            val viewModel: LoginViewModel = viewModel(
                factory = LoginViewModel.provideFactory(
                    loginUseCase = appContainer.loginUseCase,
                    sessionManager = appContainer.sessionManager
                )
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            LoginScreen(
                state = state,
                onUsernameChange = viewModel::updateUsername,
                onPasswordChange = viewModel::updatePassword,
                onSecureLoginChange = viewModel::updateSecureLoginEnabled,
                onSignInClick = {
                    viewModel.submitLogin {
                        navController.navigate(AppRoutes.HOME) {
                            popUpTo(AppRoutes.LOGIN) {
                                inclusive = true
                            }
                        }
                    }
                }
            )

            LoginBiometricPrompt(
                trigger = state.biometricPromptTrigger,
                onSuccess = {
                    viewModel.onSecureLoginAuthenticationSucceeded {
                        navController.navigate(AppRoutes.HOME) {
                            popUpTo(AppRoutes.LOGIN) {
                                inclusive = true
                            }
                        }
                    }
                },
                onError = viewModel::onSecureLoginAuthenticationError
            )
        }

        composable(route = AppRoutes.HOME) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory(appContainer.observeHomeFeedUseCase)
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            HomeScreen(
                state = state,
                onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) }
            )
        }

        composable(route = AppRoutes.PROFILE) {
            val viewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModel.provideFactory(appContainer.observeOwnProfileUseCase)
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            ProfileScreen(
                state = state,
                onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
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
                onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
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
                onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
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
                onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
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
                onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onBookClick = { bookId ->                  
                    navController.navigate(AppRoutes.detail(bookId))
                },
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
                onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
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
                onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onRetry = viewModel::retry
            )
        }

        composable(route = AppRoutes.BOOKS) {
            BookListScreen(
                viewModelFactory = BookListViewModel.provideFactory(
                    getBooksUseCase = appContainer.getBooksUseCase,
                    getAuthorsUseCase = appContainer.getAuthorsUseCase
                ),
                onBookClick = { bookId -> navController.navigate(AppRoutes.detail(bookId)) },
                onAuthorClick = { authorId -> navController.navigate(AppRoutes.authorDetail(authorId)) },
                onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) }
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
                onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
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
                onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun AuthGateScreen(
    state: AuthGateUiState,
    onBiometricSuccess: () -> Unit,
    onBiometricError: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            Text(text = "Checking session...")
        }
    }

    AuthBiometricPrompt(
        trigger = state.biometricPromptTrigger,
        title = "Unlock FeedBook",
        subtitle = "Secure login requires local authentication",
        description = "Use your fingerprint or device credential to open your saved session",
        onSuccess = onBiometricSuccess,
        onError = { onBiometricError() }
    )
}

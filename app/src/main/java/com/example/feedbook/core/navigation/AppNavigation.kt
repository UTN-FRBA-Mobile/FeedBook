package com.example.feedbook.core.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
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
import com.example.feedbook.R
import com.example.feedbook.core.ui.components.LocalScannerClickHandler
import com.example.feedbook.features.auth.presentation.AuthBiometricPrompt
import com.example.feedbook.features.auth.presentation.AuthGateDestination
import com.example.feedbook.features.auth.presentation.AuthGateUiState
import com.example.feedbook.features.auth.presentation.AuthGateViewModel
import com.example.feedbook.features.auth.presentation.LoginBiometricPrompt
import com.example.feedbook.features.auth.presentation.LoginScreen
import com.example.feedbook.features.authors.presentation.detail.AuthorBooksScreen
import com.example.feedbook.features.authors.presentation.detail.AuthorBooksViewModel
import com.example.feedbook.features.authors.presentation.detail.AuthorDetailScreen
import com.example.feedbook.features.authors.presentation.detail.AuthorDetailViewModel
import com.example.feedbook.features.auth.presentation.LoginViewModel
import com.example.feedbook.features.books.presentation.list.BookListScreen
import com.example.feedbook.features.books.presentation.list.BookListViewModel
import com.example.feedbook.features.books.presentation.scanner.IsbnScannerScreen
import com.example.feedbook.features.books.presentation.all.AllReviewsScreen
import com.example.feedbook.features.books.presentation.all.AllReviewsViewModel
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
import com.example.feedbook.features.profile.presentation.LocalFeedBookTopBarAvatar
import com.example.feedbook.features.profile.presentation.toAvatarPresentation
import com.example.feedbook.features.profile.presentation.ProfileScreen
import com.example.feedbook.features.profile.presentation.ProfileViewModel
import com.example.feedbook.features.profile.presentation.UserFollowersScreen
import com.example.feedbook.features.profile.presentation.UserFollowersViewModel
import com.example.feedbook.features.profile.presentation.UserProfileDetailViewModel
import com.example.feedbook.features.profile.presentation.PublicProfilePreviewViewModel
import com.example.feedbook.features.profile.presentation.PublicProfileViewModel
import com.example.feedbook.features.push.PushTokenRegistrar
import com.example.feedbook.features.readingrooms.presentation.ReadingRoomInfoScreen
import com.example.feedbook.features.readingrooms.presentation.ReadingRoomListScreen
import com.example.feedbook.features.readingrooms.presentation.ReadingRoomListViewModel
import com.example.feedbook.features.readingrooms.presentation.ReadingRoomScreen
import com.example.feedbook.features.readingrooms.presentation.ReadingRoomViewModel
import com.example.feedbook.features.stats.presentation.StatsScreen
import com.example.feedbook.features.stats.presentation.StatsViewModel

object AppRoutes {
    const val AUTH_GATE = "authGate"
    const val LOGIN = "login"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "editProfile"
    const val PUBLIC_PROFILE = "publicProfile/{userId}"

    fun publicProfile(userId: String): String = "publicProfile/$userId"
    const val PUBLIC_PROFILE_PREVIEW = "publicProfilePreview"
    const val USER_PROFILE = "userProfile/{userId}"
    const val USER_FOLLOWERS = "userProfile/{userId}/followers"
    const val LIBRARY = "library?showCollection={showCollection}"
    const val STATS = "stats"
    const val NOTIFICATIONS = "notifications"
    const val BOOKS = "books"
    const val ISBN_SCANNER = "isbnScanner"
    const val BOOK_DETAIL = "bookDetail/{bookId}"
    const val ALL_REVIEWS = "allReviews/{bookId}?title={title}"
    const val READING_ROOMS = "readingRooms"
    const val READING_ROOM = "readingRoom/{roomId}"
    const val READING_ROOM_INFO = "readingRoom/{roomId}/info"

    const val AUTHOR_DETAIL = "authorDetail/{authorId}"
    const val AUTHOR_BOOKS = "authorBooks/{authorId}?name={name}"

    fun detail(bookId: String): String = "bookDetail/$bookId"
    fun authorDetail(authorId: String): String = "authorDetail/$authorId"
    fun allReviews(bookId: String, title: String): String = "allReviews/$bookId?title=$title"
    fun readingRoom(roomId: String): String = "readingRoom/$roomId"
    fun readingRoomInfo(roomId: String): String = "readingRoom/$roomId/info"
    fun authorBooks(authorId: String, name: String): String = "authorBooks/$authorId?name=$name"
    fun userProfile(userId: String): String = "userProfile/$userId"
    fun library(showCollection: Boolean = false): String =
        if (showCollection) "library?showCollection=true" else "library"
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
fun AppNavigation(
    modifier: Modifier = Modifier,
    onAuthenticated: () -> Unit = {}
) {
    val navController = rememberNavController()
    val appContainer = (LocalContext.current.applicationContext as FeedBookApplication).container
    val currentSession by appContainer.sessionManager.session.collectAsStateWithLifecycle(initialValue = null)
    val ownProfile by appContainer.observeOwnProfileUseCase()
        .collectAsStateWithLifecycle(initialValue = null)
    val topBarAvatar = ownProfile?.toAvatarPresentation()
    val onLogout = {
        PushTokenRegistrar.unlinkCurrentToken()
        appContainer.sessionManager.clearSession()
        navController.navigate(AppRoutes.AUTH_GATE) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    CompositionLocalProvider(
        LocalFeedBookTopBarAvatar provides topBarAvatar?.let {
            com.example.feedbook.features.profile.presentation.TopBarAvatarState(
                style = it.style,
                preset = it.preset,
                imageUri = it.imageUri
            )
        },
        LocalScannerClickHandler provides {
            navController.navigate(AppRoutes.ISBN_SCANNER)
        }
    ) {
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
                            onAuthenticated()
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
                        registerUseCase = appContainer.registerUseCase,
                        sessionManager = appContainer.sessionManager,
                        backendServerConfig = appContainer.backendServerConfig
                    )
                )
                val state by viewModel.state.collectAsStateWithLifecycle()

                LoginScreen(
                    state = state,
                    onUsernameChange = viewModel::updateUsername,
                    onPasswordChange = viewModel::updatePassword,
                    onConfirmPasswordChange = viewModel::updateConfirmPassword,
                    onSecureLoginChange = viewModel::updateSecureLoginEnabled,
                    onServerOriginChange = viewModel::updateServerOriginDraft,
                    onSaveServerOrigin = viewModel::saveServerOrigin,
                    onResetServerOrigin = viewModel::resetServerOrigin,
                    onSignInClick = {
                        viewModel.submitLogin {
                            onAuthenticated()
                            navController.navigate(AppRoutes.HOME) {
                                popUpTo(AppRoutes.LOGIN) {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    onCreateAccountClick = viewModel::showRegisterMode,
                    onBackToSignInClick = viewModel::showLoginMode,
                    onRegisterClick = {
                        viewModel.submitRegister {}
                    }
                )

                LoginBiometricPrompt(
                    trigger = state.biometricPromptTrigger,
                    onSuccess = {
                        viewModel.onSecureLoginAuthenticationSucceeded {
                            onAuthenticated()
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
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onLogoutClick = onLogout,
                onRefreshClick = viewModel::retry,
                onBookClick = { bookId -> navController.navigate(AppRoutes.detail(bookId)) },
                onReadingRoomClick = { roomId -> navController.navigate(AppRoutes.readingRoom(roomId)) },
                onSeeAllReadingRoomsClick = { navController.navigate(AppRoutes.READING_ROOMS) }
            )
        }

        composable(route = AppRoutes.READING_ROOMS) {
            val viewModel: ReadingRoomListViewModel = viewModel(
                factory = ReadingRoomListViewModel.provideFactory(appContainer.readingRoomsRemoteDataSource)
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            ReadingRoomListScreen(
                state = state,
                onBackClick = { navController.popBackStack() },
                onRoomClick = { roomId -> navController.navigate(AppRoutes.readingRoom(roomId)) },
                onQueryChange = viewModel::updateQuery,
                onCreateRoom = { name, description, shortDescription, isAdult ->
                    viewModel.createRoom(name, description, shortDescription, isAdult) { roomId ->
                        navController.navigate(AppRoutes.readingRoom(roomId))
                    }
                }
            )
        }

        composable(
            route = AppRoutes.READING_ROOM,
            arguments = listOf(navArgument("roomId") { type = NavType.StringType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId").orEmpty()
            val viewModel: ReadingRoomViewModel = viewModel(
                factory = ReadingRoomViewModel.provideFactory(roomId, appContainer.readingRoomsRemoteDataSource)
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            ReadingRoomScreen(
                state = state,
                currentUsername = currentSession?.username,
                onBackClick = { navController.popBackStack() },
                onInfoClick = { navController.navigate(AppRoutes.readingRoomInfo(roomId)) },
                onJoinClick = viewModel::join,
                onChangeBook = viewModel::changeBook,
                onRate = viewModel::rate,
                onComment = viewModel::comment
            )
        }

        composable(
            route = AppRoutes.READING_ROOM_INFO,
            arguments = listOf(navArgument("roomId") { type = NavType.StringType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId").orEmpty()
            val viewModel: ReadingRoomViewModel = viewModel(
                factory = ReadingRoomViewModel.provideFactory(roomId, appContainer.readingRoomsRemoteDataSource)
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            ReadingRoomInfoScreen(
                state = state,
                currentUsername = currentSession?.username,
                onBackClick = { navController.popBackStack() },
                onSaveDescription = viewModel::updateDescription,
                onKick = viewModel::kick,
                onLeave = {
                    viewModel.leave {
                        navController.navigate(AppRoutes.READING_ROOMS) {
                            popUpTo(AppRoutes.READING_ROOMS) { inclusive = true }
                        }
                    }
                },
                onDelete = { confirmation ->
                    viewModel.delete(confirmation) {
                        navController.navigate(AppRoutes.HOME) {
                            popUpTo(AppRoutes.HOME) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
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
                onCollectionClick = { navController.navigateTopLevel(AppRoutes.library(true)) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onLogoutClick = onLogout,
                onRefreshClick = viewModel::retry,
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
                onLogoutClick = onLogout,
                onSave = { updatedState ->
                    viewModel.saveProfile(updatedState) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable(
            route = AppRoutes.PUBLIC_PROFILE,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").orEmpty()
            val viewModel: PublicProfileViewModel = viewModel(
                factory = PublicProfileViewModel.provideFactory(
                    userId = userId,
                    getPublicProfileUseCase = appContainer.getPublicProfileUseCase,
                    toggleUserFollowUseCase = appContainer.toggleUserFollowUseCase
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
                onLogoutClick = onLogout,
                onRefreshClick = viewModel::retry,
                onFollowersClick = { navController.navigate(AppRoutes.USER_FOLLOWERS.replace("{userId}", userId)) },
                onBookClick = { bookId -> navController.navigate(AppRoutes.detail(bookId)) },
                onFollowClick = viewModel::toggleFollow,
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
                onLogoutClick = onLogout,
                onRefreshClick = viewModel::retry,
                onBookClick = { bookId -> navController.navigate(AppRoutes.detail(bookId)) },
                onRetry = viewModel::retry
            )
        }

        composable(
            route = AppRoutes.USER_PROFILE,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").orEmpty()
            val viewModel: UserProfileDetailViewModel = viewModel(
                factory = UserProfileDetailViewModel.provideFactory(
                    userId = userId,
                    getExploreUsersUseCase = appContainer.getExploreUsersUseCase,
                    toggleUserFollowUseCase = appContainer.toggleUserFollowUseCase
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
                onLogoutClick = onLogout,
                onRefreshClick = viewModel::retry,
                onFollowClick = viewModel::toggleFollow,
                onFollowersClick = { navController.navigate(AppRoutes.USER_FOLLOWERS.replace("{userId}", userId)) },
                onRetry = viewModel::retry
            )
        }

        composable(
            route = AppRoutes.USER_FOLLOWERS,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").orEmpty()
            val viewModel: UserFollowersViewModel = viewModel(
                factory = UserFollowersViewModel.provideFactory(
                    userId = userId,
                    getUserFollowersUseCase = appContainer.getUserFollowersUseCase
                )
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            UserFollowersScreen(
                state = state,
                onBackClick = { navController.popBackStack() },
                onUserClick = { targetId -> navController.navigate(AppRoutes.userProfile(targetId)) }
            )
        }

        composable(
            route = AppRoutes.LIBRARY,
            arguments = listOf(navArgument("showCollection") {
                type = NavType.BoolType
                defaultValue = false
            })
        ) { backStackEntry ->
            val showCollection = backStackEntry.arguments?.getBoolean("showCollection") ?: false
            val viewModel: LibraryViewModel = viewModel(
                factory = LibraryViewModel.provideFactory(
                    observeOwnLibraryUseCase = appContainer.observeOwnLibraryUseCase,
                    getAuthorsUseCase = appContainer.getAuthorsUseCase
                )
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            LibraryScreen(
                state = state,
                initialShowReadCollection = showCollection,
                onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onLogoutClick = onLogout,
                onRefreshClick = viewModel::retry,
                onBookClick = { bookId ->
                    navController.navigate(AppRoutes.detail(bookId))
                },
                onAuthorClick = { authorId ->
                    navController.navigate(AppRoutes.authorDetail(authorId))
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
                onLogoutClick = onLogout,
                onRefreshClick = viewModel::retry,
                onModeSelected = viewModel::selectMode,
                onRetry = viewModel::retry
            )
        }

        composable(route = AppRoutes.NOTIFICATIONS) {
            val viewModel: NotificationsViewModel = viewModel(
                factory = NotificationsViewModel.provideFactory(
                    getNotificationsUseCase = appContainer.getNotificationsUseCase,
                    refreshBus = appContainer.userContentRefreshBus,
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
                onLogoutClick = onLogout,
                onRefreshClick = viewModel::retry,
                onRetry = viewModel::retry
            )
        }

            composable(route = AppRoutes.BOOKS) {
                BookListScreen(
                    viewModelFactory = BookListViewModel.provideFactory(
                        getBooksUseCase = appContainer.getBooksUseCase,
                        getAuthorsUseCase = appContainer.getAuthorsUseCase,
                        getExploreUsersUseCase = appContainer.getExploreUsersUseCase,
                        searchExploreUseCase = appContainer.searchExploreUseCase,
                        observeOwnProfileUseCase = appContainer.observeOwnProfileUseCase
                    ),
                    onBookClick = { bookId -> navController.navigate(AppRoutes.detail(bookId)) },
                    onAuthorClick = { authorId -> navController.navigate(AppRoutes.authorDetail(authorId)) },
                    onUserClick = { userId -> navController.navigate(AppRoutes.userProfile(userId)) },
                    onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                    onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
                    onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                    onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                    onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                    onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                    onLogoutClick = onLogout
                )
            }

            composable(route = AppRoutes.ISBN_SCANNER) {
                IsbnScannerScreen(
                    getBookByIsbnUseCase = appContainer.getBookByIsbnUseCase,
                    onClose = { navController.popBackStack() },
                    onScanComplete = { bookId ->
                        navController.navigate(AppRoutes.detail(bookId)) {
                            popUpTo(AppRoutes.ISBN_SCANNER) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

        composable(
            route = AppRoutes.BOOK_DETAIL,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId").orEmpty()
            val detailViewModel: BookDetailViewModel = viewModel(
                factory = BookDetailViewModel.provideFactory(
                    bookId = bookId,
                    getBookByIdUseCase = appContainer.getBookByIdUseCase,
                    getBookUsersUseCase = appContainer.getBookUsersUseCase,
                    getReviewsUseCase = appContainer.getReviewsUseCase,
                    getReadingProgressUseCase = appContainer.getReadingProgress,
                    saveReadingProgressUseCase = appContainer.saveReadingProgressUseCase,
                    saveReviewUseCase = appContainer.saveReviewUseCase,
                    toggleLikeUseCase = appContainer.toggleLikeUseCase,
                    addBookToLibraryUseCase = appContainer.addBookToLibraryUseCase,
                    removeBookFromLibraryUseCase = appContainer.removeBookFromLibraryUseCase,
                    observeOwnLibraryUseCase = appContainer.observeOwnLibraryUseCase,
                    observeOwnProfileUseCase = appContainer.observeOwnProfileUseCase
                )
            )
            val detailState by detailViewModel.state.collectAsStateWithLifecycle()
            val bookForTitle = detailState.book
            BookDetailScreen(
                state = detailState,
                onRetry = detailViewModel::loadBook,
                onToggleLibrary = detailViewModel::toggleBookInLibrary,
                onSaveProgress = detailViewModel::saveProgress,
                onSaveReview = detailViewModel::saveReview,
                onReviewFeedbackShown = detailViewModel::clearReviewFeedback,
                onLibraryFeedbackShown = detailViewModel::clearLibraryFeedback,
                onBackClick = { navController.popBackStack() },
                onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onLogoutClick = onLogout,
                onToggleLike = detailViewModel::toggleLike,
                onShowAllReviews = {
                    val title = bookForTitle?.title?.takeIf { it.isNotBlank() } ?: "Unknown"
                    navController.navigate(AppRoutes.allReviews(bookId, title))
                },
                onAuthorClick = { authorId ->
                    navController.navigate(AppRoutes.authorDetail(authorId))
                },
                onUserClick = { userId -> navController.navigate(AppRoutes.userProfile(userId)) }
            )
        }

        composable(
            route = AppRoutes.ALL_REVIEWS,
            arguments = listOf(
                navArgument("bookId") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType; defaultValue = "Unknown" }
            )
        ) { backStackEntry ->
            val reviewsBookId = backStackEntry.arguments?.getString("bookId").orEmpty()
            val reviewsTitle = backStackEntry.arguments?.getString("title") ?: "Unknown"
            val allReviewsViewModel: AllReviewsViewModel = viewModel(
                factory = AllReviewsViewModel.Factory(
                    bookId = reviewsBookId,
                    getReviewsUseCase = appContainer.getReviewsUseCase,
                    toggleLikeUseCase = appContainer.toggleLikeUseCase
                )
            )
            val allReviewsState by allReviewsViewModel.state.collectAsStateWithLifecycle()
            AllReviewsScreen(
                bookTitle = reviewsTitle,
                state = allReviewsState,
                onBackClick = { navController.popBackStack() },
                onToggleLike = allReviewsViewModel::toggleLike,
                onLoadMore = allReviewsViewModel::loadMore
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
                    getAuthorUsersUseCase = appContainer.getAuthorUsersUseCase,
                    toggleFollowUseCase = appContainer.toggleAuthorFollowUseCase,
                    observeOwnProfileUseCase = appContainer.observeOwnProfileUseCase
                ),
                onFeedClick = { navController.navigateTopLevel(AppRoutes.HOME) },
                onExploreClick = { navController.navigateTopLevel(AppRoutes.BOOKS) },
                onLibraryClick = { navController.navigateTopLevel(AppRoutes.LIBRARY) },
                onStatsClick = { navController.navigateTopLevel(AppRoutes.STATS) },
                onNotificationsClick = { navController.navigateTopLevel(AppRoutes.NOTIFICATIONS) },
                onProfileClick = { navController.navigateTopLevel(AppRoutes.PROFILE) },
                onLogoutClick = onLogout,
                onBackClick = { navController.popBackStack() },
                onBookClick = { bookId -> navController.navigate(AppRoutes.detail(bookId)) },
                onSeeAllBooks = {
                    val id = backStackEntry.arguments?.getString("authorId").orEmpty()
                    navController.navigate(AppRoutes.authorBooks(id, ""))
                },
                onUserClick = { userId -> navController.navigate(AppRoutes.userProfile(userId)) }
            )
        }

        composable(
            route = AppRoutes.AUTHOR_BOOKS,
            arguments = listOf(
                navArgument("authorId") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val authorId = backStackEntry.arguments?.getString("authorId").orEmpty()
            val authorName = backStackEntry.arguments?.getString("name") ?: ""
            AuthorBooksScreen(
                viewModelFactory = AuthorBooksViewModel.Factory(
                    authorId = authorId,
                    authorName = authorName,
                    getAuthorByIdUseCase = appContainer.getAuthorByIdUseCase
                ),
                onBackClick = { navController.popBackStack() },
                onBookClick = { bookId -> navController.navigate(AppRoutes.detail(bookId)) }
            )
        }
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
            Text(text = stringResource(R.string.app_checking_session))
        }
    }

    AuthBiometricPrompt(
        trigger = state.biometricPromptTrigger,
        title = stringResource(R.string.app_unlock_title),
        subtitle = stringResource(R.string.app_unlock_subtitle),
        description = stringResource(R.string.app_unlock_description),
        onSuccess = onBiometricSuccess,
        onError = { onBiometricError() }
    )
}

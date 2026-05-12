package com.example.feedbook.features.authors.presentation.detail
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.authors.domain.usecase.GetAuthorByIdUseCase
import com.example.feedbook.features.authors.domain.usecase.ToggleAuthorFollowUseCase
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnProfileUseCase
import com.example.feedbook.features.profile.presentation.AvatarPresentation
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.feedbook.features.profile.presentation.toAvatarPresentation
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

class AuthorDetailViewModel (
    private val authorId: String,
    private val getAuthorByIdUseCase: GetAuthorByIdUseCase,
    private val toggleFollowUseCase: ToggleAuthorFollowUseCase,
    observeOwnProfileUseCase: ObserveOwnProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthorDetailUiState(isLoading = true))
    val state: StateFlow<AuthorDetailUiState> = _state.asStateFlow()

    private var avatarPresentation = AvatarPresentation(
        style = defaultAvatarStyle(),
        preset = null,
        imageUri = null
    )

    init {
        loadAuthor()
        viewModelScope.launch {
            observeOwnProfileUseCase()
                .catch { }
                .collectLatest { profile ->
                    avatarPresentation = profile.toAvatarPresentation()
                    _state.value = _state.value.copy(
                        avatarStyle = avatarPresentation.style,
                        avatarPreset = avatarPresentation.preset,
                        avatarImageUri = avatarPresentation.imageUri
                    )
                }
        }
    }

    fun loadAuthor() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val authorDeferred = async { runCatching { getAuthorByIdUseCase(authorId) } }

            val authorResult = authorDeferred.await()

            _state.value = AuthorDetailUiState(
                isLoading = false,
                avatarStyle = avatarPresentation.style,
                avatarPreset = avatarPresentation.preset,
                avatarImageUri = avatarPresentation.imageUri,

                author = authorResult.getOrNull()?.toUiModel(),

                error = authorResult.exceptionOrNull()?.message
                    ?: if (authorResult.isSuccess && authorResult.getOrNull() == null) "Autor no encontrado" else null
            )
        }
    }

    fun toggleFollow() {
        val currentAuthor = _state.value.author ?: return
        viewModelScope.launch {
            _state.update {
                it.copy(author = currentAuthor.copy(isFollowing = !currentAuthor.isFollowing))
            }
            try {
                toggleFollowUseCase(authorId)
            } catch (e: Exception) {
                _state.update { it.copy(author = currentAuthor) }
            }
        }
    }

    companion object {
        fun provideFactory(
            authorId: String,
            getAuthorByIdUseCase: GetAuthorByIdUseCase,
            toggleFollowUseCase: ToggleAuthorFollowUseCase,
            observeOwnProfileUseCase: ObserveOwnProfileUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                AuthorDetailViewModel(
                    authorId,
                    getAuthorByIdUseCase,
                    toggleFollowUseCase,
                    observeOwnProfileUseCase
                ) as T
        }
    }
}

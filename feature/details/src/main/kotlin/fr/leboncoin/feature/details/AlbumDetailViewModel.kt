package fr.leboncoin.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.core.analytics.AnalyticsHelper
import fr.leboncoin.core.analytics.logFavoriteToggled
import fr.leboncoin.core.analytics.logScreenView
import fr.leboncoin.core.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * The album id comes from the navigation route and is passed through assisted injection:
 * the ViewModel stays free of navigation/Bundle APIs and is trivially unit-testable.
 */
@HiltViewModel(assistedFactory = AlbumDetailViewModel.Factory::class)
class AlbumDetailViewModel @AssistedInject constructor(
    @Assisted private val albumId: Int,
    private val albumRepository: AlbumRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    val uiState: StateFlow<AlbumDetailUiState> = albumRepository.observeAlbum(albumId)
        .map { album ->
            if (album != null) AlbumDetailUiState.Success(album) else AlbumDetailUiState.NotFound
        }
        .stateIn(
            scope = viewModelScope,
            // Keeps the upstream alive across configuration changes, stops it in background.
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = AlbumDetailUiState.Loading,
        )

    init {
        analyticsHelper.logScreenView(SCREEN_NAME)
    }

    fun onFavoriteClick() {
        val album = (uiState.value as? AlbumDetailUiState.Success)?.album ?: return
        val isFavorite = !album.isFavorite
        viewModelScope.launch {
            albumRepository.setFavorite(album.id, isFavorite)
            analyticsHelper.logFavoriteToggled(album.id, isFavorite)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(albumId: Int): AlbumDetailViewModel
    }

    internal companion object {
        const val SCREEN_NAME = "album_detail"
    }
}

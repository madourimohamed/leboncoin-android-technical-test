package fr.leboncoin.feature.albums

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.core.analytics.AnalyticsHelper
import fr.leboncoin.core.analytics.logAlbumSelected
import fr.leboncoin.core.analytics.logFavoriteToggled
import fr.leboncoin.core.analytics.logScreenView
import fr.leboncoin.core.domain.model.Album
import fr.leboncoin.core.domain.model.RefreshResult
import fr.leboncoin.core.domain.repository.AlbumRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val analyticsHelper: AnalyticsHelper,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        // The filter survives process death, not only configuration changes.
        AlbumsUiState(favoritesOnly = savedStateHandle[FAVORITES_ONLY_KEY] ?: false),
    )
    val uiState: StateFlow<AlbumsUiState> = _uiState.asStateFlow()

    /** Cached in the ViewModel scope: pages already loaded survive configuration changes. */
    @OptIn(ExperimentalCoroutinesApi::class)
    val albums: Flow<PagingData<Album>> = _uiState
        .map { it.favoritesOnly }
        .distinctUntilChanged()
        .flatMapLatest { favoritesOnly -> albumRepository.observeAlbums(favoritesOnly) }
        .cachedIn(viewModelScope)

    private var refreshJob: Job? = null

    init {
        // Done here rather than in the UI: runs once per screen, not on each configuration change.
        analyticsHelper.logScreenView(SCREEN_NAME)
        refresh()
    }

    fun refresh() {
        if (refreshJob?.isActive == true) return
        refreshJob = viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            val result = albumRepository.refresh()
            _uiState.update {
                it.copy(isRefreshing = false, error = (result as? RefreshResult.Failure)?.error)
            }
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(error = null) }
    }

    fun onFavoritesOnlyChange(favoritesOnly: Boolean) {
        savedStateHandle[FAVORITES_ONLY_KEY] = favoritesOnly
        _uiState.update { it.copy(favoritesOnly = favoritesOnly) }
    }

    fun onAlbumClick(album: Album) {
        analyticsHelper.logAlbumSelected(album.id)
    }

    fun onFavoriteClick(album: Album) {
        val isFavorite = !album.isFavorite
        viewModelScope.launch {
            albumRepository.setFavorite(album.id, isFavorite)
            analyticsHelper.logFavoriteToggled(album.id, isFavorite)
        }
    }

    internal companion object {
        const val SCREEN_NAME = "albums"
        const val FAVORITES_ONLY_KEY = "favorites_only"
    }
}

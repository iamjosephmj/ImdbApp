package com.ssemaj.imdbapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssemaj.imdbapp.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class
)
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "Search failed"
    }

    val searchResults = _searchQuery
        .debounce(300L)
        .flatMapLatest { query ->
            when {
                query.length < 2 -> flowOf(SearchUiState.Idle)
                else -> flow {
                    emit(SearchUiState.Loading)
                    searchMoviesUseCase(query).collect { movies ->
                        emit(SearchUiState.Success(movies))
                    }
                }.catch { emit(SearchUiState.Error(it.message ?: DEFAULT_ERROR_MESSAGE)) }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SearchUiState.Idle)

    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }
}

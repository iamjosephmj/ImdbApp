package com.ssemaj.imdbapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssemaj.imdbapp.data.api.exception.ApiException
import com.ssemaj.imdbapp.domain.ErrorMessageFormatter
import com.ssemaj.imdbapp.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
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
class SearchViewModel @Inject internal constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val errorMessageFormatter: ErrorMessageFormatter
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val searchResults = _searchQuery
        .debounce(DEBOUNCE_TIMEOUT_MS)
        .flatMapLatest { query -> createSearchFlow(query) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT_MS), SearchUiState.Idle)

    private fun createSearchFlow(query: String): Flow<SearchUiState> {
        return when {
            query.length < MIN_QUERY_LENGTH -> flowOf(SearchUiState.Idle)
            else -> performSearch(query)
        }
    }

    private fun performSearch(query: String): Flow<SearchUiState> {
        return flow {
            emit(SearchUiState.Loading)
            searchMoviesUseCase(query).collect { movies ->
                emit(SearchUiState.Success(movies))
            }
        }.catch { exception ->
            emit(SearchUiState.Error(formatError(exception)))
        }
    }

    private fun formatError(exception: Throwable): String {
        return when (exception) {
            is ApiException -> errorMessageFormatter.format(exception)
            else -> "$DEFAULT_SEARCH_ERROR_MESSAGE_PREFIX${exception.message ?: "Unknown error"}"
        }
    }

    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    companion object {
        private const val DEBOUNCE_TIMEOUT_MS = 300L
        private const val MIN_QUERY_LENGTH = 2
        private const val SUBSCRIPTION_TIMEOUT_MS = 5000L
        private const val DEFAULT_SEARCH_ERROR_MESSAGE_PREFIX = "Search failed: "
    }
}

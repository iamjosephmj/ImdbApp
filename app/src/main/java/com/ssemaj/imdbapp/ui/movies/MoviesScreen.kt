package com.ssemaj.imdbapp.ui.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssemaj.imdbapp.R
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.ssemaj.imdbapp.data.model.Movie
import com.ssemaj.imdbapp.ui.components.ErrorMessage
import com.ssemaj.imdbapp.ui.components.LoadingIndicator
import com.ssemaj.imdbapp.ui.components.LoadingItem
import com.ssemaj.imdbapp.ui.components.MovieCard
import com.ssemaj.imdbapp.ui.theme.ImdbAppTheme
import com.ssemaj.imdbapp.ui.util.WindowType
import com.ssemaj.imdbapp.ui.util.rememberWindowSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    movies: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.now_playing),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_movies)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val refreshState = movies.loadState.refresh) {
                is LoadState.Loading -> {
                    // Only show full screen loading on initial load
                    if (movies.itemCount == 0) {
                        LoadingIndicator()
                    }
                }

                is LoadState.Error -> {
                    ErrorMessage(
                        message = refreshState.error.localizedMessage
                            ?: stringResource(R.string.failed_to_load_movies),
                        onRetry = { movies.retry() }
                    )
                }

                is LoadState.NotLoading -> {
                    if (movies.itemCount == 0) {
                        EmptyState()
                    }
                }
            }

            // Show grid with pull-to-refresh when we have items
            if (movies.itemCount > 0) {
                MovieGridWithCarousel(
                    movies = movies,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieGridWithCarousel(
    movies: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSize = rememberWindowSize()
    val columns = when (windowSize) {
        WindowType.Compact -> 2
        WindowType.Medium -> 3
        WindowType.Expanded -> 4
    }

    val isRefreshing = movies.loadState.refresh is LoadState.Loading
    val pullToRefreshState = rememberPullToRefreshState()
    val gridState = rememberLazyGridState()


    // Check if the grid is scrolled to the top for pull-to-refresh
    val isAtTop by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            if (isAtTop) {
                movies.refresh()
            }
        },
        state = pullToRefreshState,
        modifier = modifier.fillMaxSize(),
        indicator = {
            // Only show the indicator when at top
            if (isAtTop || isRefreshing) {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    ) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Movie grid items
            items(
                count = movies.itemCount,
                key = { index -> "movie_grid_$index" }
            ) { index ->
                movies[index]?.let { movie ->
                    MovieCard(
                        movie = movie,
                        onClick = { onMovieClick(movie.id) }
                    )
                }
            }

            // Loading more indicator at bottom
            when (movies.loadState.append) {
                is LoadState.Loading -> {
                    item(span = { GridItemSpan(columns) }) {
                        LoadingItem()
                    }
                }

                is LoadState.Error -> {
                    item(span = { GridItemSpan(columns) }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.failed_to_load_more),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            FilledTonalButton(onClick = { movies.retry() }) {
                                Text(stringResource(R.string.retry))
                            }
                        }
                    }
                }

                is LoadState.NotLoading -> Unit
            }
        }
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_movies_available),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStatePreview() {
    ImdbAppTheme {
        EmptyState()
    }
}

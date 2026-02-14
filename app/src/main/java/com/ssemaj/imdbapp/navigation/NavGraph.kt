package com.ssemaj.imdbapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.ssemaj.imdbapp.di.LocalViewModelFactory
import com.ssemaj.imdbapp.ui.details.DetailsScreen
import com.ssemaj.imdbapp.ui.details.DetailsViewModel
import com.ssemaj.imdbapp.ui.movies.MoviesScreen
import com.ssemaj.imdbapp.ui.movies.MoviesViewModel
import com.ssemaj.imdbapp.ui.search.SearchScreen
import com.ssemaj.imdbapp.ui.search.SearchViewModel

sealed class Screen(val route: String) {
    data object Movies : Screen("movies")
    data object Details : Screen("details/{movieId}") {
        fun createRoute(movieId: Int) = "details/$movieId"
    }

    data object Search : Screen("search")
}

@Composable
fun NavGraph(navController: NavHostController) {
    val factory = LocalViewModelFactory.current

    NavHost(navController = navController, startDestination = Screen.Movies.route) {
        composable(Screen.Movies.route) {
            val vm: MoviesViewModel = viewModel(factory = factory)
            val movies = vm.movies.collectAsLazyPagingItems()

            MoviesScreen(
                movies = movies,
                onMovieClick = { navController.navigate(Screen.Details.createRoute(it)) },
                onSearchClick = { navController.navigate(Screen.Search.route) }
            )
        }

        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { entry ->
            val movieId = entry.arguments?.getInt("movieId") ?: return@composable
            val vm: DetailsViewModel = viewModel(factory = factory)

            DetailsScreen(
                movieId = movieId,
                viewModel = vm,
                onBackClick = navController::navigateUp
            )
        }

        composable(Screen.Search.route) {
            val vm: SearchViewModel = viewModel(factory = factory)

            SearchScreen(
                viewModel = vm,
                onMovieClick = { navController.navigate(Screen.Details.createRoute(it)) },
                onBackClick = navController::navigateUp
            )
        }
    }
}

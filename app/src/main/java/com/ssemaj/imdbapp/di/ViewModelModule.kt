package com.ssemaj.imdbapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ssemaj.imdbapp.ui.details.DetailsViewModel
import com.ssemaj.imdbapp.ui.movies.MoviesViewModel
import com.ssemaj.imdbapp.ui.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider

/**
 * Module that provides ViewModels to the multibinding map.
 * Each ViewModel is bound with its class as the key.
 * ViewModels are provided via their @Inject constructors.
 */
@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindMoviesViewModel(viewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindDetailsViewModel(viewModel: DetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel
}

/**
 * Generic ViewModelFactory that uses Dagger multibinding.
 * Automatically handles all ViewModels registered in ViewModelModule.
 */
class ViewModelFactory @Inject constructor(
    private val viewModels: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val provider = viewModels[modelClass]
            ?: error("Unknown ViewModel: $modelClass")
        return provider.get() as T
    }
}

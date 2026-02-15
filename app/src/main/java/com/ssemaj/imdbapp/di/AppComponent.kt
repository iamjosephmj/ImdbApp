package com.ssemaj.imdbapp.di

import android.app.Application
import android.content.Context
import com.ssemaj.imdbapp.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class, ContextModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }
}

/**
 * Provides Context from Application.
 * Since Application extends Context, we can use it directly.
 */
@Module
internal object ContextModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application
}

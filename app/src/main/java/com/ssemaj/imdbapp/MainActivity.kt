package com.ssemaj.imdbapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ssemaj.imdbapp.di.LocalViewModelFactory
import com.ssemaj.imdbapp.di.ViewModelFactory
import com.ssemaj.imdbapp.navigation.NavGraph
import com.ssemaj.imdbapp.ui.theme.ImdbAppTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ImdbApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CompositionLocalProvider(LocalViewModelFactory provides viewModelFactory) {
                ImdbAppTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavGraph(navController = rememberNavController())
                    }
                }
            }
        }
    }
}

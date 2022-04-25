package com.veygard.movies_recycler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.activityViewModels
import com.veygard.movies_recycler.presentation.navigation.CriticalErrorScreenRouter
import com.veygard.movies_recycler.presentation.navigation.CriticalErrorScreenRouterImpl
import com.veygard.movies_recycler.presentation.viewmodel.MoviesStateVM
import com.veygard.movies_recycler.presentation.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getMovies()
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                Log.d("splash_screen","setKeepOnScreenCondition")
                viewModel.getMoviesResponse.value != null && viewModel.getMoviesResponse.value != MoviesStateVM.Loading
            }
        }
        setContentView(R.layout.activity_main)

    }

}
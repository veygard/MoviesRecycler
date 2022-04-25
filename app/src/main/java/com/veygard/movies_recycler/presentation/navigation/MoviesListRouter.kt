package com.veygard.movies_recycler.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.veygard.movies_recycler.R

interface MoviesListRouter {
    fun routeToCriticalErrorScreen()

}

class MoviesListRouterImpl(
    private val fragment: Fragment
) : MoviesListRouter {


    override fun routeToCriticalErrorScreen() {
        fragment.findNavController()
            .navigate(R.id.action_moviesListScreen_to_criticalErrorFragment)
    }


}
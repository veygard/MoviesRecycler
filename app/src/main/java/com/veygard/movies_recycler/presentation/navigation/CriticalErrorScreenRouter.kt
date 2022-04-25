package com.veygard.movies_recycler.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.veygard.movies_recycler.R

interface CriticalErrorScreenRouter {
    fun routeToMoviesListScreen()
}

class CriticalErrorScreenRouterImpl(
    private val fragment: Fragment
) : CriticalErrorScreenRouter {


    override fun routeToMoviesListScreen() {
        fragment.findNavController().navigate(R.id.action_criticalErrorFragment_to_moviesListScreen)
    }

}
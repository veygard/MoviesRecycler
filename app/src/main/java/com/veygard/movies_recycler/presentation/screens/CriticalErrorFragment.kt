package com.veygard.movies_recycler.presentation.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.veygard.movies_recycler.presentation.navigation.CriticalErrorScreenRouter
import com.veygard.movies_recycler.presentation.navigation.CriticalErrorScreenRouterImpl
import com.veygard.movies_recycler.databinding.FragmentCriticalErrorBinding
import com.veygard.movies_recycler.presentation.viewmodel.MoviesViewModel

class CriticalErrorFragment : Fragment() {
    private var _binding: FragmentCriticalErrorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by activityViewModels()

    private val router: CriticalErrorScreenRouter by lazy {
        CriticalErrorScreenRouterImpl(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCriticalErrorBinding.inflate(inflater, container, false)

        reconnectButtonListener()
        return binding.root
    }

    private fun reconnectButtonListener() {
        _binding?.reconnectButton?.setOnClickListener {
            viewModel.getMovies()
            router.routeToMoviesListScreen()
        }
    }

}
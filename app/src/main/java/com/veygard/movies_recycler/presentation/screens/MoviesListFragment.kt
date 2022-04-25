package com.veygard.movies_recycler.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.veygard.movies_recycler.R
import com.veygard.movies_recycler.databinding.FragmentMoviesListScreenBinding
import com.veygard.movies_recycler.presentation.navigation.MoviesListRouter
import com.veygard.movies_recycler.presentation.navigation.MoviesListRouterImpl
import com.veygard.movies_recycler.presentation.viewmodel.MoviesStateVM
import com.veygard.movies_recycler.presentation.viewmodel.MoviesViewModel

class MoviesListFragment : Fragment() {

    private val viewModel: MoviesViewModel by activityViewModels()

    private val router: MoviesListRouter by lazy {
        MoviesListRouterImpl(this)
    }

    private var _binding: FragmentMoviesListScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    private fun observeData() {
        viewModel.getMoviesResponse.addObserver {result->
            when(result){
                is MoviesStateVM.GotMovies ->{

                }
                is MoviesStateVM.Error -> {
                    router.routeToCriticalErrorScreen()
                }
                MoviesStateVM.Loading -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
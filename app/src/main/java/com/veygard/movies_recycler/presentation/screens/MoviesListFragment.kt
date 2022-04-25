package com.veygard.movies_recycler.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.veygard.movies_recycler.R
import com.veygard.movies_recycler.databinding.FragmentMoviesListScreenBinding
import com.veygard.movies_recycler.presentation.adapters.MovieListAdapter
import com.veygard.movies_recycler.presentation.navigation.MoviesListRouter
import com.veygard.movies_recycler.presentation.navigation.MoviesListRouterImpl
import com.veygard.movies_recycler.presentation.viewmodel.MoviesStateVM
import com.veygard.movies_recycler.presentation.viewmodel.MoviesViewModel
import com.veygard.movies_recycler.util.SpanGridLayoutManager
import com.veygard.movies_recycler.util.extentions.setDivider

class MoviesListFragment : Fragment() {

    private val viewModel: MoviesViewModel by activityViewModels()

    private val router: MoviesListRouter by lazy {
        MoviesListRouterImpl(this)
    }

    private var binding: FragmentMoviesListScreenBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesListScreenBinding.inflate(inflater, container, false)
        observeData()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() {
        viewModel.getMoviesResponse.addObserver {result->
            when(result){
                is MoviesStateVM.GotMovies ->{
                    val adapter = MovieListAdapter(movieList = result.result.results ?: emptyList())
                    binding?.movieRecyclerHolder?.adapter = adapter
                    val gridLayoutManager= SpanGridLayoutManager(activity?.baseContext,500)
                    binding?.movieRecyclerHolder?.layoutManager= gridLayoutManager
                }
                is MoviesStateVM.Error -> {
                    router.routeToCriticalErrorScreen()
                }
                MoviesStateVM.Loading -> {}
            }
        }
    }
}

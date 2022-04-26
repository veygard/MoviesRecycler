package com.veygard.movies_recycler.presentation.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.veygard.movies_recycler.data.remote.model.Movie
import com.veygard.movies_recycler.databinding.FragmentMoviesListScreenBinding
import com.veygard.movies_recycler.presentation.adapters.MovieListAdapter
import com.veygard.movies_recycler.presentation.adapters.MovieViewHolder
import com.veygard.movies_recycler.presentation.navigation.MoviesListRouter
import com.veygard.movies_recycler.presentation.navigation.MoviesListRouterImpl
import com.veygard.movies_recycler.presentation.viewmodel.MoviesStateVM
import com.veygard.movies_recycler.presentation.viewmodel.MoviesViewModel
import com.veygard.movies_recycler.util.SpanGridLayoutManager

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
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setNestedScrollListener()
    }

    private fun observeData() {
        viewModel.getMoviesResponse.addObserver { result ->
            when (result) {
                is MoviesStateVM.GotMovies -> {
                    setupMovieRecycler(result.list)
                }
                is MoviesStateVM.Error -> {
                    router.routeToCriticalErrorScreen()
                }
                MoviesStateVM.Loading -> {}
            }
        }
    }

    private fun setupMovieRecycler(results: List<Movie>?) {
        val adapter = MovieListAdapter(movieList = results ?: emptyList())
        binding?.movieRecyclerHolder?.adapter = adapter
        val gridLayoutManager = SpanGridLayoutManager(activity?.baseContext, 500)
        binding?.movieRecyclerHolder?.layoutManager = gridLayoutManager
    }

    private fun setNestedScrollListener() {
        binding?.moviesNestedScroll?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->

            //грузить начинаем как только проходим отметку в 5 айтемов до конца вьюшки
            val loadMoreCoordinates =
                (v.getChildAt(0).measuredHeight - v.measuredHeight) - (MovieViewHolder.movieItemHeight * 5)

            Log.d("nested_coordinates", "scrollY: $scrollY,  measuredHeight: $loadMoreCoordinates")
            if (scrollY > loadMoreCoordinates && viewModel.loadingIsNotBusy.value) {
                Log.d("nested_coordinates", "true")
                Toast.makeText(requireContext(), "new data", Toast.LENGTH_SHORT).show()
                viewModel.loadMore()
            }
        })
    }
}

package com.veygard.movies_recycler.presentation.screens

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.veygard.movies_recycler.R
import com.veygard.movies_recycler.data.remote.model.Movie
import com.veygard.movies_recycler.databinding.FragmentMoviesListScreenBinding
import com.veygard.movies_recycler.presentation.adapters.MovieListAdapter
import com.veygard.movies_recycler.presentation.adapters.PaginationScrollListener
import com.veygard.movies_recycler.presentation.navigation.MoviesListRouter
import com.veygard.movies_recycler.presentation.navigation.MoviesListRouterImpl
import com.veygard.movies_recycler.presentation.viewmodel.MoviesStateVM
import com.veygard.movies_recycler.presentation.viewmodel.MoviesViewModel
import com.veygard.movies_recycler.util.SpanGridLayoutManager
import com.veygard.movies_recycler.util.extentions.onQueryTextChanged
import com.veygard.movies_recycler.util.isInternetConnected
import com.veygard.movies_recycler.util.showToast

class MoviesListFragment : Fragment() {

    private val viewModel: MoviesViewModel by activityViewModels()

    private val router: MoviesListRouter by lazy {
        MoviesListRouterImpl(this)
    }
    private var adapter: MovieListAdapter? = null

    private var binding: FragmentMoviesListScreenBinding? = null
    private var mHandler = Handler()


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
        searchViewListener()
        cancelButtonListener()
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

                is MoviesStateVM.MoreMovies -> {
                    result.more?.let {
                        adapter?.addNewItems(it)
                    }
                }
                is MoviesStateVM.MoreMoviesError -> adapter?.removeShimmers()

                is MoviesStateVM.SearchMovies -> {
                    setupMovieRecycler(result.list)
                }
            }
        }

        viewModel.showSnackbar.addObserver {
            it?.let {
                showToast(it, activity?.applicationContext)
            }
        }
    }

    private fun setupMovieRecycler(results: List<Movie>?) {
        adapter = MovieListAdapter(movieList = results ?: emptyList())
        binding?.movieRecyclerHolder?.adapter = adapter
        val gridLayoutManager = SpanGridLayoutManager(activity?.baseContext, 500)
        binding?.movieRecyclerHolder?.layoutManager = gridLayoutManager
        setMovieRecyclerListener(gridLayoutManager)
    }

    private fun searchViewListener() {
        binding?.searchBar?.onQueryTextChanged { text ->
            mHandler.removeCallbacksAndMessages(null)

            when {
                text.isNotEmpty() -> {
                    mHandler.postDelayed(Runnable {
                        viewModel.searchMovie(text)
                    }, 600)
                }
                else -> {
                    viewModel.turnOffSearch()
                }
            }

            toggleVisibility(text.isEmpty(), binding?.cancelButton)
            toggleSearchViewIconColor(text.isNotEmpty())
        }
    }

    private fun cancelButtonListener() {
        binding?.cancelButton?.setOnClickListener {
            binding?.searchBar?.setQuery("", false)
            binding?.searchBar?.clearFocus()
        }
    }

    private fun setMovieRecyclerListener(gridLayoutManager: SpanGridLayoutManager) {
        binding?.movieRecyclerHolder?.addOnScrollListener(object :
            PaginationScrollListener(gridLayoutManager) {
            override fun loadMoreItems() {
                viewModel.loadMore()
                adapter?.addShimmers()
            }

            override val isLastPage: Boolean
                get() = false
            override val isInternetAvailable: Boolean
                get() = isInternetConnected(activity?.applicationContext)
            override val isReadyToLoad: Boolean
                get() = viewModel.loadingIsNotBusy.value
            override val hasMoreMovies: Boolean
                get() = viewModel.hasMoreMovies.value
        })
    }

    private fun toggleVisibility(gone: Boolean, view: View?) {
        if (gone) view?.visibility = View.GONE
        else view?.visibility = View.VISIBLE
    }

    private fun toggleSearchViewIconColor(isNotEmpty: Boolean) {
        val icon = binding?.searchIcon

        if (isNotEmpty) icon?.setColorFilter(
            context?.getColor(R.color.blue) ?: Color.BLACK
        )
        else icon?.setColorFilter(
            context?.getColor(R.color.grey) ?: Color.LTGRAY
        )
    }
}

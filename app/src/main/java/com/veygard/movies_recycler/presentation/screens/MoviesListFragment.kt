package com.veygard.movies_recycler.presentation.screens

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.veygard.movies_recycler.R
import com.veygard.movies_recycler.databinding.FragmentMoviesListScreenBinding
import com.veygard.movies_recycler.presentation.adapters.MovieListAdapter
import com.veygard.movies_recycler.presentation.adapters.PaginationScrollListener
import com.veygard.movies_recycler.presentation.navigation.MoviesListRouter
import com.veygard.movies_recycler.presentation.navigation.MoviesListRouterImpl
import com.veygard.movies_recycler.presentation.viewmodel.MoviesStateVM
import com.veygard.movies_recycler.presentation.viewmodel.MoviesViewModel
import com.veygard.movies_recycler.util.SpanGridLayoutManager
import com.veygard.movies_recycler.util.isInternetConnected
import com.veygard.movies_recycler.util.showToast


class MoviesListFragment : Fragment() {

    private val viewModel: MoviesViewModel by activityViewModels()

    private val router: MoviesListRouter by lazy {
        MoviesListRouterImpl(this)
    }
    private var adapter: MovieListAdapter? = null

    private var binding: FragmentMoviesListScreenBinding? = null

    private var searchCountDownTimer: CountDownTimer? = null
    private val searchWaitingTime = 1000L
    private var searchTextValue = ""

    private  lateinit var  gridLayoutManager: SpanGridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesListScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeData()
        searchViewListener()
        cancelButtonListener()
    }

    private fun setUpRecyclerView() {
        adapter = MovieListAdapter()
        binding?.movieRecyclerHolder?.adapter = adapter
        gridLayoutManager = SpanGridLayoutManager(activity?.baseContext, 500)
        binding?.movieRecyclerHolder?.layoutManager = gridLayoutManager
        setMovieRecyclerListener(gridLayoutManager)
    }

    private fun observeData() {
        viewModel.getMoviesResponse.addObserver { result ->
            when (result) {
                is MoviesStateVM.GotMovies, is MoviesStateVM.SearchMovies  -> {
                    adapter?.submitList(result.movieList?.toMutableList())
                }
                is MoviesStateVM.Loading -> {
                    adapter?.submitList(result.movieList?.toMutableList())
                    //после добавления шимера скролим вниз, чтобы показать что он есть
                    binding?.movieRecyclerHolder?.let { gridLayoutManager.smoothScrollToPosition(it, RecyclerView.State(), gridLayoutManager.findLastCompletelyVisibleItemPosition() +2) }
                }
                is MoviesStateVM.Error -> {
                    router.routeToCriticalErrorScreen()
                }
                is MoviesStateVM.MoreMoviesError -> {}
                is MoviesStateVM.ClearMovieList -> {
                    adapter?.submitList(null)
                }
            }
        }

        viewModel.showSnackbar.addObserver {
            it?.let {
                showToast(it, activity?.applicationContext)
            }
        }

        viewModel.showLoadingBar.addObserver {result ->
            when(result){
                true -> toggleVisibility(false, binding?.loadingBar)
                false -> toggleVisibility(true, binding?.loadingBar)
            }
        }
    }


    private fun searchViewListener() {
        binding?.searchBar?.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(text: String?): Boolean {
                searchTextValue = text ?: ""
                when {
                    searchTextValue.isNotEmpty() -> {
                        searchCountDownTimer?.cancel()
                        searchCountDownTimer = object : CountDownTimer(searchWaitingTime, 500) {
                            override fun onTick(p0: Long) {}
                            override fun onFinish() {
                                Log.d("search_test", "countDownTimer: onFinish, text: $text")
                                if(viewModel.requestIsRdy.value)viewModel.searchMovie(searchTextValue)
                            }
                        }
                        searchCountDownTimer?.start()
                    }

                    else -> {
                        viewModel.turnOffSearch()
                    }
                }
                toggleVisibility( searchTextValue.isEmpty(), binding?.cancelButton)
                toggleSearchViewIconColor(searchTextValue.isNotEmpty())
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("search_test", "search enter event, text: $searchTextValue, query: $query")
                if(viewModel.requestIsRdy.value)viewModel.searchMovie(searchTextValue)
                return false
            }
        })
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
            }

            override val isReadyToLoad: Boolean
                get() = viewModel.requestIsRdy.value
            override val listSize: Int
                get() = viewModel.listSize.value
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

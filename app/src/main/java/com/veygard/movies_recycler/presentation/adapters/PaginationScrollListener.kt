package com.veygard.movies_recycler.presentation.adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.veygard.movies_recycler.util.SpanGridLayoutManager

abstract class PaginationScrollListener(private val layoutManager: SpanGridLayoutManager) :
    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//        Log.d("pagination_test","visibleItemCount: $visibleItemCount, totalItemCount:$totalItemCount, firstVisibleItemPosition:$firstVisibleItemPosition")

        if (isReadyToLoad && !isLastPage && isInternetAvailable && hasMoreMovies) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                Log.d("pagination_test","loading")
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract val isLastPage: Boolean
    abstract val isInternetAvailable: Boolean
    abstract val isReadyToLoad: Boolean
    abstract val hasMoreMovies: Boolean

}
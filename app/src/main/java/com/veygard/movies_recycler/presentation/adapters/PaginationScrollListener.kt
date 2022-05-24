package com.veygard.movies_recycler.presentation.adapters

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veygard.movies_recycler.util.SpanGridLayoutManager

private var isScrollingActiveNow = false

abstract class PaginationScrollListener(private val layoutManager: SpanGridLayoutManager) :
    RecyclerView.OnScrollListener() {

    private var lastPosition = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        lastPosition=  layoutManager.findLastVisibleItemPosition()
        if (isReadyToLoad && !isScrollingActiveNow) {
            isScrollingActiveNow = true
            if (lastPosition+1 == listSize
            ) {
                Log.d("pagination_test", "onScrolled loading")
                loadMoreItems()
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            isScrollingActiveNow = false
            lastPosition=  layoutManager.findLastVisibleItemPosition()

            if (isReadyToLoad && !isScrollingActiveNow) {
                Log.e("scrolling_state", "onScrollStateChanged last: $lastPosition, size: $listSize")
                isScrollingActiveNow = true
                if (lastPosition+1 == listSize
                ) {
                    Log.d("pagination_test", "onScrollStateChanged loading")
                    loadMoreItems()
                }
            }

        }
    }

    protected abstract fun loadMoreItems()
    abstract val isReadyToLoad: Boolean
    abstract val listSize: Int
}
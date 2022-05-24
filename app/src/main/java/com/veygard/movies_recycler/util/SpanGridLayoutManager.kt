package com.veygard.movies_recycler.util

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import kotlin.math.max


class SpanGridLayoutManager(context: Context?, private var columnWidth: Int) :
    GridLayoutManager(context, 1) {

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        if (columnWidth > 0) {
            val totalSpace: Int = if (orientation == VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }
            val spanCount = max(1, totalSpace / columnWidth)

            setSpanCount(spanCount)
        }
        super.onLayoutChildren(recycler, state)
    }

}


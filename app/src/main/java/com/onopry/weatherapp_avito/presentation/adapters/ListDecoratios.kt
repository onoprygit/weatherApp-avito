package com.onopry.weatherapp_avito.presentation.adapters

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DailyListDecoration: RecyclerView.ItemDecoration(

){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = 200
        outRect.bottom = 200
        outRect.left  = 200
        outRect.right = 200
        super.getItemOffsets(outRect, view, parent, state)
    }

}

class HourlyListDecoration: RecyclerView.ItemDecoration(){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left  = 200
        outRect.right = 200
        super.getItemOffsets(outRect, view, parent, state)
    }

}
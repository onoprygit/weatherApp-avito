package com.onopry.weatherapp_avito.presentation.adapters

import androidx.recyclerview.widget.DiffUtil

abstract class BaseDiffCallback<T>(
    private val oldList: List<T>,
    private val newList: List<T>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size
}
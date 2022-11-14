package com.onopry.weatherapp_avito.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.shortToast(message: String){
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(message: String){
    Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
}

fun View.show() = this.apply { if (visibility != View.VISIBLE) visibility = View.VISIBLE }

fun View.hide() = this.apply { if (visibility != View.INVISIBLE) visibility = View.INVISIBLE }

fun View.gone() = this.apply { if (visibility != View.GONE) visibility = View.GONE }

inline fun View.showIfConditionOrHide(condition: () -> Boolean) = this.apply {
    visibility = if (condition() && visibility != View.VISIBLE) View.VISIBLE else View.INVISIBLE
}

inline fun View.showIfConditionOrGone(condition: () -> Boolean) = this.apply {
    visibility = if (condition() && visibility != View.VISIBLE) View.VISIBLE else View.GONE
}
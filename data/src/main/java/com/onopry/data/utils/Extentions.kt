package com.onopry.data.utils

import android.util.Log

fun Any.debugLog(msg: String) {
    Log.d("DEV_LOG_TAG_" + this.javaClass.simpleName, msg)
}

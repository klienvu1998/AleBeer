package com.hyvu.alebeer.utils

import android.content.Context

fun pxToDb(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.density
}

fun dpToPx(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}
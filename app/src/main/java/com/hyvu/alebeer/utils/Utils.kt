package com.hyvu.alebeer.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


fun pxToDb(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.density
}

fun dpToPx(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}

fun hideSoftKeyboard(view: View) {
    val imm =
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
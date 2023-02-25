package com.hyvu.alebeer.utils

import java.text.SimpleDateFormat

object TimeUtils {

    fun formatDateHHmmSSddMM(timestamp: Long): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss dd-MM")
        return simpleDateFormat.format(timestamp)
    }

}
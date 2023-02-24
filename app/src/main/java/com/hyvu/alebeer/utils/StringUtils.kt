package com.hyvu.alebeer.utils

object StringUtils {

    fun getExtensionOfUrl(url: String): String {
        val splits= url.split(".")
        return if (splits.isEmpty()) "" else splits.last()
    }

}
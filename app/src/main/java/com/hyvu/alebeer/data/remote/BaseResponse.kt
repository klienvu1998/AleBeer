package com.hyvu.alebeer.data.remote

sealed class BaseResponse<out T> {
    class Success<T>(val data: T): BaseResponse<T>()
    class Error(e: Exception): BaseResponse<Nothing>()
}
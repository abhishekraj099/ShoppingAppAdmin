package com.example.shoppingappadmin.common

import com.example.shoppingappadmin.R

sealed class ResultState<out T> {
    data class Success<T>(val data: T) : ResultState<T>()
    object Loading : ResultState<Nothing>()
    data class Error(val message: String) : ResultState<Nothing>()

}
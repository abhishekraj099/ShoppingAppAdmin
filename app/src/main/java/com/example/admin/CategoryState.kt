package com.example.admin

data class CategoryState(
    val data: String = "",
    val isLoading: Boolean = false,
    val error: String = "",
    val isSuccess: Boolean = false
)

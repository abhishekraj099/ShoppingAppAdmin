package com.example.shoppingappadmin.domain.model

data class CategoryModel (
    var name: String = "",
    val date: Long = System.currentTimeMillis(),
    val createdBy: String = "",
)
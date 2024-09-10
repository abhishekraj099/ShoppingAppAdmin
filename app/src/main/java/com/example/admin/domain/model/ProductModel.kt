package com.example.admin.domain.model

data class ProductModel(
    var name: String = "",
    var price: String = "",
    var image: String = "",
    var category: String = "",
    var description: String = "",
    val date: Long = System.currentTimeMillis(),
    val createdBy: String = "",
    var finalPrice: String = ""
)
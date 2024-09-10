package com.example.admin.domain.model

data class CategoryModel(
    val name: String = "",
    val brandName: String = "",
    val createdBy: String = "",
    val bannerPhotoUrl: String? = null,
    val categoryPhotoUrl: String? = null // Make sure this property exists
)


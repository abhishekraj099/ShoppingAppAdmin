package com.example.admin.domain.repository

import android.net.Uri
import com.example.admin.common.ResultState
import com.example.admin.domain.model.BannerModel
import com.example.admin.domain.model.CategoryModel
import com.example.admin.domain.model.ProductModel
import kotlinx.coroutines.flow.Flow







interface ShoppingAppRepo {
    // Method to add a category, with optional banner URI
    suspend fun addCategory(category: CategoryModel, bannerUri: Uri?): Flow<ResultState<String>>

    // Method to get a list of categories
    suspend fun getCategories(): Flow<ResultState<List<CategoryModel>>>

    // Method to add a product, with optional product URI
    suspend fun addProduct(product: ProductModel, productUri: Uri?): Flow<ResultState<String>>

    // Method to upload a banner image
    suspend fun uploadBannerImage(bannerUri: Uri): Flow<ResultState<String>>

    // Method to upload a category image
    suspend fun uploadCategoryImage(categoryUri: Uri): Flow<ResultState<String>>
}



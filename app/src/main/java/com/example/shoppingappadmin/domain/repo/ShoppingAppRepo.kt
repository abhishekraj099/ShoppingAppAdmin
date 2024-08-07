package com.example.shoppingappadmin.domain.repo

import android.net.Uri
import com.example.shoppingappadmin.common.ResultState
import com.example.shoppingappadmin.domain.model.CategoryModel
import com.example.shoppingappadmin.domain.model.ProductModel
import kotlinx.coroutines.flow.Flow

interface ShoppingAppRepo {
    suspend fun addCategory(category: CategoryModel): Flow<ResultState<String>>
    suspend fun getCategories(): Flow<ResultState<List<CategoryModel>>>
    suspend fun addImage(uri: Uri): Flow<ResultState<String>>
    suspend fun addProduct(product: ProductModel): Flow<ResultState<String>>
}
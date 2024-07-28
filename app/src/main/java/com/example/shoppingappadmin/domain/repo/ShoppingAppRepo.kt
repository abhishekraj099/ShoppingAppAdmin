package com.example.shoppingappadmin.domain.repo

import androidx.compose.runtime.State
import com.example.shoppingappadmin.common.ResultState
import com.example.shoppingappadmin.domain.model.CategoryModel
import kotlinx.coroutines.flow.Flow


interface ShoppingAppRepo {

    suspend fun addCategory(category: CategoryModel): Flow<ResultState<String>>

    suspend fun getCategories(): Flow<ResultState<List<CategoryModel>>>

}
package com.example.shoppingappadmin.addcategory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingappadmin.common.ResultState
import com.example.shoppingappadmin.domain.model.CategoryModel
import com.example.shoppingappadmin.domain.repo.ShoppingAppRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddCategoryViewModel @Inject constructor(private val shoppingAppRepo: ShoppingAppRepo) : ViewModel() {

    var category by mutableStateOf(CategoryModel())
        private set

    private val _categoryState = MutableStateFlow(CategoryState())
    val categoryState: StateFlow<CategoryState> = _categoryState

    fun updateCategory(newCategory: String) {
        category = category.copy(name = newCategory, createdBy = "Abhishek")
    }

    fun addCategory() {
        viewModelScope.launch {
            shoppingAppRepo.addCategory(category = category).collect {
                when (it) {
                    is ResultState.Success -> {
                        _categoryState.value = CategoryState(data = it.data)
                        category = CategoryModel()
                    }

                    is ResultState.Error -> {
                        _categoryState.value = CategoryState(error = it.message)
                        category = CategoryModel()
                    }

                    is ResultState.Loading -> {
                        _categoryState.value = CategoryState(isLoading = true)
                        category = CategoryModel()
                    }
                }
            }
        }
    }

}


data class CategoryState(
    val data: String = "",
    val isLoading: Boolean = false,
    val error: String = ""
)
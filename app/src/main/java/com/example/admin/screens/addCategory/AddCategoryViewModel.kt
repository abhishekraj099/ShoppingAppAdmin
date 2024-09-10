package com.example.admin.screens.addCategory

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.admin.CategoryState
import com.example.admin.common.ResultState
import com.example.admin.domain.model.CategoryModel
import com.example.admin.domain.repository.ShoppingAppRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class AddCategoryViewModel @Inject constructor(private val shoppingAppRepo: ShoppingAppRepo) : ViewModel() {

    var category by mutableStateOf(CategoryModel())
        private set

    private val _categoryState = MutableStateFlow(CategoryState())
    val categoryState: StateFlow<CategoryState> = _categoryState

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState

    fun updateCategory(newCategory: String, brandName: String) {
        category = category.copy(name = newCategory, createdBy = "Shreyansh", brandName = brandName)
    }

    fun updateBannerPhotoUrl(bannerUrl: String) {
        category = category.copy(bannerPhotoUrl = bannerUrl)
    }

    fun updateCategoryPhotoUrl(categoryUrl: String) {
        category = category.copy(categoryPhotoUrl = categoryUrl)
    }

    fun addCategory() {
        viewModelScope.launch {
            shoppingAppRepo.addCategory(category = category, bannerUri = null).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _categoryState.value = CategoryState(data = result.data, isSuccess = true)
                        _uploadState.value = UploadState.Success("Category added successfully")
                        category = CategoryModel() // Clear category after success
                    }
                    is ResultState.Error -> {
                        _categoryState.value = CategoryState(error = result.message, isSuccess = false)
                        _uploadState.value = UploadState.Error(result.message)
                    }
                    is ResultState.Loading -> {
                        _categoryState.value = CategoryState(isLoading = true, isSuccess = false)
                        _uploadState.value = UploadState.Loading
                    }
                }
            }
        }
    }

    fun uploadCategoryImage(uri: Uri) {
        viewModelScope.launch {
            shoppingAppRepo.uploadCategoryImage(categoryUri = uri).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        updateCategoryPhotoUrl(result.data)
                        _uploadState.value = UploadState.Success("Category image uploaded successfully")
                    }
                    is ResultState.Error -> {
                        _uploadState.value = UploadState.Error(result.message)
                    }
                    is ResultState.Loading -> {
                        _uploadState.value = UploadState.Loading
                    }
                }
            }
        }
    }

    fun uploadBannerImage(uri: Uri) {
        viewModelScope.launch {
            shoppingAppRepo.uploadBannerImage(bannerUri = uri).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        updateBannerPhotoUrl(result.data)
                        _uploadState.value = UploadState.Success("Banner image uploaded successfully")
                    }
                    is ResultState.Error -> {
                        _uploadState.value = UploadState.Error(result.message)
                    }
                    is ResultState.Loading -> {
                        _uploadState.value = UploadState.Loading
                    }
                }
            }
        }
    }
}

sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    data class Success(val message: String) : UploadState()
    data class Error(val message: String) : UploadState()
}

data class CategoryState(
    val data: CategoryModel? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String = ""
)

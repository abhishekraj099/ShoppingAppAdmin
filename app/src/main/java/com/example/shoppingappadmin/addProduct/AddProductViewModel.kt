package com.example.shoppingappadmin.addProduct

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingappadmin.addcategory.CategoryState
import com.example.shoppingappadmin.common.ResultState
import com.example.shoppingappadmin.domain.model.CategoryModel
import com.example.shoppingappadmin.domain.repo.ShoppingAppRepo
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddProductViewModel @Inject constructor(private val shoppingAppRepo: ShoppingAppRepo): ViewModel() {
    private val _allCategories = MutableStateFlow<ResultState<List<CategoryModel>>>(ResultState.Loading)
    val allCategories = _allCategories.asStateFlow()

    init {
        viewModelScope.launch {
            shoppingAppRepo.getCategories().collect {
                _allCategories.value = it
            }
        }
    }

    fun addImage(uri: Uri) {
        viewModelScope.launch {
            shoppingAppRepo.addImage(uri).collect {
                Log.d("AddImage", it.toString())
            }
        }
    }

}




package com.example.admin.screens.addCategory

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.admin.domain.model.BannerModel
import com.example.admin.domain.model.CategoryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage

@Composable
fun CategoryScreen(viewModel: AddCategoryViewModel = hiltViewModel()) {
    val categoryState by viewModel.categoryState.collectAsState()
    val context = LocalContext.current
    var categoryImageUri by remember { mutableStateOf<Uri?>(null) }
    var bannerImageUri by remember { mutableStateOf<Uri?>(null) }
    var bannerName by remember { mutableStateOf("") }

    val categoryImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            categoryImageUri = uri
        }
    }

    val bannerImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            bannerImageUri = uri
        }
    }

    LaunchedEffect(categoryState) {
        when {
            categoryState.isLoading -> {
                // You might want to show a loading indicator here
            }
            categoryState.error.isNotEmpty() -> {
                Toast.makeText(context, categoryState.error, Toast.LENGTH_SHORT).show()
            }
            categoryState.isSuccess -> {
                Toast.makeText(context, "Category added successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        CategorySection(
            categoryImageUri = categoryImageUri,
            onCategoryImageSelect = { categoryImageLauncher.launch("image/*") },
            categoryName = viewModel.category.name,
            onCategoryNameChange = { viewModel.updateCategory(it, viewModel.category.brandName) },
            brandName = viewModel.category.brandName,
            onBrandNameChange = { viewModel.updateCategory(viewModel.category.name, it) },
            onAddCategory = {
                if (categoryImageUri != null) {
                    viewModel.addCategory()
                } else {
                    Toast.makeText(context, "Please select a category image", Toast.LENGTH_SHORT).show()
                }
            }
        )

        BannerSection(
            bannerImageUri = bannerImageUri,
            onBannerImageSelect = { bannerImageLauncher.launch("image/*") },
            bannerName = bannerName,
            onBannerNameChange = { bannerName = it },
            onAddBanner = {
                if (bannerImageUri != null && bannerName.isNotEmpty()) {
                    viewModel.uploadBannerImage(bannerImageUri!!)
                } else {
                    Toast.makeText(context, "Please select a banner image and enter a name", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

@Composable
fun CategorySection(
    categoryImageUri: Uri?,
    onCategoryImageSelect: () -> Unit,
    categoryName: String,
    onCategoryNameChange: (String) -> Unit,
    brandName: String,
    onBrandNameChange: (String) -> Unit,
    onAddCategory: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Add Category",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            ImageSelector(
                imageUri = categoryImageUri,
                onImageSelect = onCategoryImageSelect,
                placeholderText = "Select Category Image"
            )

            OutlinedTextField(
                value = categoryName,
                onValueChange = onCategoryNameChange,
                label = { Text("Category Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = brandName,
                onValueChange = onBrandNameChange,
                label = { Text("Brand Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onAddCategory,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add Category")
            }
        }
    }
}

@Composable
fun BannerSection(
    bannerImageUri: Uri?,
    onBannerImageSelect: () -> Unit,
    bannerName: String,
    onBannerNameChange: (String) -> Unit,
    onAddBanner: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Add Banner",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            ImageSelector(
                imageUri = bannerImageUri,
                onImageSelect = onBannerImageSelect,
                placeholderText = "Select Banner Image"
            )

            OutlinedTextField(
                value = bannerName,
                onValueChange = onBannerNameChange,
                label = { Text("Banner Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onAddBanner,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add Banner")
            }
        }
    }
}

@Composable
fun ImageSelector(
    imageUri: Uri?,
    onImageSelect: () -> Unit,
    placeholderText: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onImageSelect),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = placeholderText,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
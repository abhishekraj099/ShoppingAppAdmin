package com.example.admin.screens.addProduct

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.admin.common.ResultState
import com.example.admin.domain.model.CategoryModel
import com.example.admin.domain.model.ProductModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductsScreen(viewModel: AddProductViewModel = hiltViewModel()) {
    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productFinalPrice by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Select Category") }
    var createdBy by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var img by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        img = it
    }

    val context = LocalContext.current
    val categories by viewModel.allCategories.collectAsState()
    val uiState by viewModel.productAdded.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Text(
                text = "Add New Product",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            ImageSection(img, onSelectImage = { galleryLauncher.launch("image/*") })

            Spacer(modifier = Modifier.height(16.dp))

            ProductInfoSection(
                productName = productName,
                productDescription = productDescription,
                productPrice = productPrice,
                productFinalPrice = productFinalPrice,
                onProductNameChange = { productName = it },
                onProductDescriptionChange = { productDescription = it },
                onProductPriceChange = { productPrice = it },
                onProductFinalPriceChange = { productFinalPrice = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CategorySection(
                selectedCategory = selectedCategory,
                expanded = expanded,
                categories = categories,
                onExpandedChange = { expanded = !expanded },
                onCategorySelected = { category ->
                    selectedCategory = category
                    expanded = false
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = createdBy,
                onValueChange = { createdBy = it },
                label = { Text("Created By") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    img?.let { uri ->
                        val newProduct = ProductModel(
                            name = productName,
                            price = productPrice,
                            finalPrice = productFinalPrice,
                            description = productDescription,
                            category = selectedCategory,
                            createdBy = createdBy,
                            image = uri.toString()
                        )
                        viewModel.addProduct(newProduct, uri)
                    } ?: run {
                        Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Add Product", style = MaterialTheme.typography.titleMedium)
            }
        }

        // Loading and error states
        when {
            uiState.isLoading -> LoadingOverlay()
            !uiState.error.isNullOrBlank() -> ErrorMessage(uiState.error ?: "Unknown error occurred")
            !uiState.success.isNullOrBlank() -> {
                LaunchedEffect(Unit) {
                    Toast.makeText(context, "Product Added", Toast.LENGTH_SHORT).show()
                    // Reset form fields
                    productName = ""
                    productDescription = ""
                    productPrice = ""
                    productFinalPrice = ""
                    selectedCategory = "Select Category"
                    createdBy = ""
                    expanded = false
                    img = null
                }
            }
        }
    }
}

@Composable
fun ImageSection(img: Uri?, onSelectImage: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(onClick = onSelectImage),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (img != null) {
                AsyncImage(
                    model = img,
                    contentDescription = "Selected product image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Add photo",
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Select Product Image")
                }
            }
        }
    }
}

@Composable
fun ProductInfoSection(
    productName: String,
    productDescription: String,
    productPrice: String,
    productFinalPrice: String,
    onProductNameChange: (String) -> Unit,
    onProductDescriptionChange: (String) -> Unit,
    onProductPriceChange: (String) -> Unit,
    onProductFinalPriceChange: (String) -> Unit
) {
    OutlinedTextField(
        value = productName,
        onValueChange = onProductNameChange,
        label = { Text("Product Name") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
        value = productDescription,
        onValueChange = onProductDescriptionChange,
        label = { Text("Product Description") },
        modifier = Modifier.fillMaxWidth(),
        minLines = 3
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = productPrice,
            onValueChange = onProductPriceChange,
            label = { Text("Price") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedTextField(
            value = productFinalPrice,
            onValueChange = onProductFinalPriceChange,
            label = { Text("Final Price") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySection(
    selectedCategory: String,
    expanded: Boolean,
    categories: ResultState<List<CategoryModel>>,
    onExpandedChange: () -> Unit,
    onCategorySelected: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() },
    ) {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = { },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange() }
        ) {
            when (categories) {
                is ResultState.Loading -> {
                    DropdownMenuItem(
                        text = { Text("Loading...") },
                        onClick = { }
                    )
                }
                is ResultState.Success -> {
                    categories.data.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = { onCategorySelected(category.name) }
                        )
                    }
                }
                is ResultState.Error -> {
                    DropdownMenuItem(
                        text = { Text("Error loading categories") },
                        onClick = { }
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun ErrorMessage(error: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}
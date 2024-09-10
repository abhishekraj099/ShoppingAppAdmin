package com.example.admin.data.repository

import android.net.Uri
import com.example.admin.common.ResultState
import com.example.admin.domain.model.CategoryModel
import com.example.admin.domain.model.ProductModel
import com.example.admin.domain.repository.ShoppingAppRepo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import com.example.admin.domain.model.BannerModel
import java.util.UUID


class ShoppingAppRepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : ShoppingAppRepo {

    override suspend fun addCategory(category: CategoryModel, bannerUri: Uri?): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        if (bannerUri != null) {
            val bannerRef = firebaseStorage.reference.child("categoryBanners/${UUID.randomUUID()}.jpg")
            bannerRef.putFile(bannerUri)
                .addOnSuccessListener {
                    bannerRef.downloadUrl.addOnSuccessListener { bannerUrl ->
                        val updatedCategory = category.copy(bannerPhotoUrl = bannerUrl.toString())
                        firebaseFirestore.collection("categories").add(updatedCategory)
                            .addOnSuccessListener {
                                trySend(ResultState.Success("Category Added with Banner"))
                            }
                            .addOnFailureListener { exception ->
                                trySend(ResultState.Error("Failed to add category: ${exception.message}"))
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    trySend(ResultState.Error("Failed to upload banner: ${exception.message}"))
                }
        } else {
            firebaseFirestore.collection("categories").add(category)
                .addOnSuccessListener {
                    trySend(ResultState.Success("Category Added"))
                }
                .addOnFailureListener { exception ->
                    trySend(ResultState.Error("Failed to add category: ${exception.message}"))
                }
        }

        awaitClose { close() }
    }

    override suspend fun getCategories(): Flow<ResultState<List<CategoryModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        val listenerRegistration = firebaseFirestore.collection("categories")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ResultState.Error("Failed to fetch categories: ${error.message}"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val categories = snapshot.toObjects(CategoryModel::class.java)
                    trySend(ResultState.Success(categories))
                } else {
                    trySend(ResultState.Error("No categories found"))
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun addProduct(product: ProductModel, productUri: Uri?): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection("products").add(product)
            .addOnSuccessListener { documentReference ->
                if (productUri != null) {
                    val productImageRef = firebaseStorage.reference.child("productImages/${documentReference.id}.jpg")
                    productImageRef.putFile(productUri)
                        .addOnCompleteListener { uploadTask ->
                            if (uploadTask.isSuccessful) {
                                productImageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                                    firebaseFirestore.collection("products").document(documentReference.id)
                                        .update("imageUrl", imageUrl.toString())
                                        .addOnSuccessListener {
                                            trySend(ResultState.Success("Product Added with Image"))
                                        }
                                        .addOnFailureListener { exception ->
                                            trySend(ResultState.Error("Failed to update product with image: ${exception.message}"))
                                        }
                                }
                            } else {
                                trySend(ResultState.Error("Failed to upload product image: ${uploadTask.exception?.message}"))
                            }
                        }
                } else {
                    trySend(ResultState.Success("Product Added without Image"))
                }
            }
            .addOnFailureListener { exception ->
                trySend(ResultState.Error("Failed to add product: ${exception.message}"))
            }

        awaitClose { close() }
    }

    override suspend fun uploadBannerImage(bannerUri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val bannerRef = firebaseStorage.reference.child("categoryBanners/${UUID.randomUUID()}.jpg")
        bannerRef.putFile(bannerUri)
            .addOnSuccessListener {
                bannerRef.downloadUrl.addOnSuccessListener { bannerUrl ->
                    trySend(ResultState.Success(bannerUrl.toString()))
                }
            }
            .addOnFailureListener { exception ->
                trySend(ResultState.Error("Failed to upload banner image: ${exception.message}"))
            }

        awaitClose { close() }
    }

    // Add this method to handle the category image upload if needed
    override suspend fun uploadCategoryImage(categoryUri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val categoryRef = firebaseStorage.reference.child("categoryImages/${UUID.randomUUID()}.jpg")
        categoryRef.putFile(categoryUri)
            .addOnSuccessListener {
                categoryRef.downloadUrl.addOnSuccessListener { categoryUrl ->
                    trySend(ResultState.Success(categoryUrl.toString()))
                }
            }
            .addOnFailureListener { exception ->
                trySend(ResultState.Error("Failed to upload category image: ${exception.message}"))
            }

        awaitClose { close() }
    }
}

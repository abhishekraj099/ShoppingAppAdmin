package com.example.admin.presentation.di

import com.example.admin.data.repository.ShoppingAppRepoImpl
import com.example.admin.domain.repository.ShoppingAppRepo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UiModule {
    @Provides
    fun provideRepo(firestore: FirebaseFirestore, firebaseStorage: FirebaseStorage): ShoppingAppRepo {
        return ShoppingAppRepoImpl(firestore, firebaseStorage)
    }
}
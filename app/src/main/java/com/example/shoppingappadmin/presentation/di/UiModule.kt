package com.example.shoppingappadmin.presentation.di

import com.example.shoppingappadmin.data.repo.ShoppingAppRepoImpl
import com.example.shoppingappadmin.domain.repo.ShoppingAppRepo
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object UiModule {
    @Provides
    fun provideRepo(firestore: FirebaseFirestore): ShoppingAppRepo{
        return ShoppingAppRepoImpl(firestore)
    }
}
package com.example.shoppingappadmin.presentation.Navigation

import kotlinx.serialization.Serializable

sealed class Routes{
    @Serializable
    object Dashboard

    @Serializable
    object AddProducts

    @Serializable
    object Notification

    @Serializable
    object Category

    @Serializable
    object Order
}
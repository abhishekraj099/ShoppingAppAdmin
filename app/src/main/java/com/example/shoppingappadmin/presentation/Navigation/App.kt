package com.example.shoppingappadmin.presentation.Navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppingappadmin.addcategory.CategoryScreen
import com.example.shoppingappadmin.addProduct.AddProductsScreen
import com.example.shoppingappadmin.presentation.Screens.DashboardScreen
import com.example.shoppingappadmin.presentation.Screens.NotificationScreen
import com.example.shoppingappadmin.presentation.Screens.OrderScreen

@Composable
fun App() {
    val navController = rememberNavController()
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = Dashboard) {
            composable<Dashboard> {
                DashboardScreen()
            }

            composable<AddProducts> {
                AddProductsScreen()
            }

            composable<Notification> {
                NotificationScreen()
            }

            composable<Category> {
                CategoryScreen()
            }

            composable<Order> {
                OrderScreen()
            }
        }
    }
}
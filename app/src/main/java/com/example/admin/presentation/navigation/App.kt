package com.example.admin.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.rememberNavController
import com.example.admin.screens.DashboardScreen
import com.example.admin.screens.NotificationScreen
import com.example.admin.screens.OrderScreen
import com.example.admin.screens.addCategory.CategoryScreen
import com.example.admin.screens.addProduct.AddProductsScreen

@Composable
fun App() {
    val navController = rememberNavController()
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    val bottomBarItems = listOf(
        BottomBarItem("Dashboard", Icons.Default.Home),
        BottomBarItem("Add Products", Icons.Default.Add),
        BottomBarItem("Notification", Icons.Default.Notifications),
        BottomBarItem("Category", Icons.Default.Category),
        BottomBarItem("Order", Icons.Default.ShoppingCart)
    )

    Scaffold(bottomBar = {
        NavigationBar {
            bottomBarItems.forEachIndexed { index, bottomBarItem ->
                NavigationBarItem(
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    icon = {
                        Image(
                            imageVector = bottomBarItem.icon,
                            contentDescription = bottomBarItem.name
                        )
                    },
                    label = {
                        if (selectedIndex == index) {
                            Text(text = bottomBarItem.name)
                        }
                    }
                )
            }
        }
    }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedIndex) {
                0 -> DashboardScreen()
                1 -> AddProductsScreen()
                2 -> NotificationScreen()
                3 -> CategoryScreen()
                4 -> OrderScreen()
            }
        }
    }
}


data class BottomBarItem(val name: String, val icon: ImageVector)
package com.example.shoppingappadmin.presentation.Navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.rememberNavController
import com.example.shoppingappadmin.addProduct.AddProductsScreen
import com.example.shoppingappadmin.addcategory.CategoryScreen
import com.example.shoppingappadmin.presentation.Screens.OrderScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(modifier: Modifier) {
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    val bottomBarItems = listOf(
        BottomBarIcon("Dashboard", Icons.Default.Home),
        BottomBarIcon("Add Products", Icons.Default.Add),
        BottomBarIcon("Notification", Icons.Default.Notifications),
        BottomBarIcon("Category", Icons.Default.Category),
        BottomBarIcon("Order", Icons.Default.ShoppingCart)
    )

    val navController = rememberNavController()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(modifier=Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar {
                    bottomBarItems.forEachIndexed { index, bottomBarIcon ->
                        NavigationBarItem(
                            selectedIndex == index,
                            onClick = { selectedIndex = index },
                            icon = { Image(imageVector = bottomBarIcon.icon, contentDescription = null)  }, label = {
                                Text(text = bottomBarIcon.name)
                            })

                    }
                }
            }
        ) {
            when (selectedIndex) {
                0 -> Text(text = "Dashboard")
                1 -> AddProductsScreen()
                2 -> Text(text = "Notification")
                3 -> CategoryScreen()
                4 -> OrderScreen()
            }
        }


    }

}

data class BottomBarIcon(val name: String, val icon: ImageVector)
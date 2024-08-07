package com.example.shoppingappadmin.presentation.Navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.shoppingappadmin.addProduct.AddProductsScreen
import com.example.shoppingappadmin.addcategory.CategoryScreen
import com.example.shoppingappadmin.presentation.Screens.OrderScreen

@Composable
fun App(modifier: Modifier) {
    var selectedIndex by remember { mutableStateOf(0) }
    val bottomBarItems = listOf(
        BottomBarIcon("Dashboard", Icons.Rounded.Home),
        BottomBarIcon("Products", Icons.Rounded.Add),
        BottomBarIcon("Notifications", Icons.Rounded.Notifications),
        BottomBarIcon("Categories", Icons.Rounded.Category),
        BottomBarIcon("Orders", Icons.Rounded.ShoppingCart)
    )

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                bottomBarItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            AnimatedIcon(
                                icon = item.icon,
                                isSelected = selectedIndex == index
                            )
                        },
                        label = {
                            AnimatedLabel(
                                text = item.name,
                                isSelected = selectedIndex == index
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedIndex) {
                0 -> DashboardScreen()
                1 -> AddProductsScreen()
                2 -> NotificationsScreen()
                3 -> CategoryScreen()
                4 -> OrderScreen()
            }
        }
    }
}

@Composable
fun AnimatedIcon(icon: ImageVector, isSelected: Boolean) {
    val transition = updateTransition(isSelected, label = "Icon Transition")
    val scale by transition.animateFloat(
        label = "Icon Scale",
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
    ) { selected -> if (selected) 1.2f else 1f }

    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier.scale(scale)
    )
}

@Composable
fun AnimatedLabel(text: String, isSelected: Boolean) {
    val transition = updateTransition(isSelected, label = "Label Transition")
    val alpha by transition.animateFloat(
        label = "Label Alpha",
        transitionSpec = { tween(durationMillis = 300) }
    ) { selected -> if (selected) 1f else 0.6f }

    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.alpha(alpha)
    )
}

data class BottomBarIcon(val name: String, val icon: ImageVector)

// Placeholder screens
@Composable
fun DashboardScreen() {
    // Implement your dashboard screen here
}

@Composable
fun NotificationsScreen() {
    // Implement your notifications screen here
}
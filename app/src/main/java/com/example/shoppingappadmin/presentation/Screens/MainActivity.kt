package com.example.shoppingappadmin.presentation.Screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.shoppingappadmin.presentation.Navigation.App
import com.example.shoppingappadmin.ui.theme.ShoppingAppAdminTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingAppAdminTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                     App(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }



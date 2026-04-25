package com.example.feedbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.feedbook.core.navigation.AppNavigation
import com.example.feedbook.core.ui.theme.FeedBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FeedBookTheme {
                AppNavigation()
            }
        }
    }
}

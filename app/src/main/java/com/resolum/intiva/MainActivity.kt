package com.resolum.intiva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.resolum.intiva.core.root.AppRoot
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity serves as the entry point of the application. It sets up the content view using Jetpack Compose and enables edge-to-edge display for a more immersive user experience.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppRoot()
        }
    }
}
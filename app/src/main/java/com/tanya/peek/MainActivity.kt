package com.tanya.peek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.tanya.core_ui.theme.PeekTheme
import com.tanya.peek.ui.PeekApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Turn of the decor view to manually handle insets for an edge to edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            PeekApp()
        }
    }
}
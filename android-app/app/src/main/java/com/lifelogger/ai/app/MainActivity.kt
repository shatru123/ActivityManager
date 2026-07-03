package com.lifelogger.ai.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelogger.ai.app.navigation.LifeLoggerNavHost
import com.lifelogger.ai.core.ui.theme.LifeLoggerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LifeLoggerTheme {
                val rootViewModel: RootViewModel = hiltViewModel()
                LifeLoggerNavHost(rootViewModel = rootViewModel)
            }
        }
    }
}

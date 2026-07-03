package com.lifelogger.ai.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lifelogger.ai.app.RootViewModel
import com.lifelogger.ai.feature.auth.AuthRoute
import com.lifelogger.ai.feature.timeline.TimelineRoute

@Composable
fun LifeLoggerNavHost(
    rootViewModel: RootViewModel,
) {
    val navController = rememberNavController()
    val session = rootViewModel.session.collectAsStateWithLifecycle().value

    LaunchedEffect(session != null) {
        navController.navigate(
            if (session == null) Destinations.AUTH else Destinations.TIMELINE,
        ) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (session == null) Destinations.AUTH else Destinations.TIMELINE,
    ) {
        composable(Destinations.AUTH) {
            AuthRoute()
        }
        composable(Destinations.TIMELINE) {
            TimelineRoute()
        }
    }
}

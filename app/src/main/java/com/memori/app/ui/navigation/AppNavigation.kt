package com.memori.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.memori.app.ui.screens.auth.SplashScreen
import com.memori.app.ui.screens.auth.LoginScreen
import com.memori.app.ui.screens.auth.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onSplashFinished = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.HomeMap.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToCadastro = {
                    navController.navigate(Screen.Cadastro.route)
                }
            )
        }
        composable(Screen.Cadastro.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Cadastro.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.HomeMap.route) {
                        popUpTo(Screen.Cadastro.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.HomeMap.route) {
            com.memori.app.ui.screens.home.MainScreen(
                onNavigateToTrailStart = {
                    navController.navigate(Screen.Splash.route + "_trail")
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.HomeMap.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Splash.route + "_trail") {
            com.memori.app.ui.screens.trail.TrailStartScreen(
                onStartAR = {
                    navController.navigate(Screen.ARScanner.route)
                }
            )
        }
        composable(Screen.ARScanner.route) {
            com.memori.app.ui.screens.trail.ARScannerScreen(
                onFinishedAR = {
                    navController.navigate(Screen.Puzzle.route)
                }
            )
        }
        composable(Screen.Puzzle.route) {
            com.memori.app.ui.screens.trail.PuzzleScreen(
                onPuzzleCompleted = { time ->
                    navController.navigate("final_story/$time")
                },
                onExit = {
                    navController.navigate(Screen.HomeMap.route) {
                        popUpTo(Screen.HomeMap.route) { inclusive = true }
                    }
                }
            )
        }
        composable("final_story/{time}") { backStackEntry ->
            val time = backStackEntry.arguments?.getString("time")?.toIntOrNull() ?: 0
            com.memori.app.ui.screens.trail.FinalStoryScreen(
                timeElapsed = time,
                onNavigateToRanking = {
                    navController.navigate(Screen.Ranking.route)
                }
            )
        }
        composable(Screen.Ranking.route) {
            com.memori.app.ui.screens.trail.RankingScreen(
                onBackToHome = {
                    navController.navigate(Screen.HomeMap.route) {
                        popUpTo(Screen.HomeMap.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

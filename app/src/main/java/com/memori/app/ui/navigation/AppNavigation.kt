package com.memori.app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.memori.app.ui.screens.auth.SplashScreen
import com.memori.app.ui.screens.auth.LoginScreen
import com.memori.app.ui.screens.auth.RegisterScreen
import com.memori.app.ui.screens.trail.StoryScreen
import com.memori.app.ui.screens.trail.dialogosIntro
import com.memori.app.ui.screens.trail.dialogosPostAR

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
                    navController.navigate("story_intro")
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.HomeMap.route) { inclusive = true }
                    }
                }
            )
        }

        composable("story_intro") {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                com.memori.app.ui.screens.home.HomeMapScreen(
                    onNavigateToProfile = {}
                )
                // Story por cima
                StoryScreen(
                    dialogos = dialogosIntro,
                    onFinished = {
                        navController.navigate(Screen.Splash.route + "_trail")
                    }
                )
            }
        }

        composable(Screen.ARScanner.route) {
            com.memori.app.ui.screens.trail.ARScannerScreen(
                onFinishedAR = {
                    navController.navigate("story_post_ar")
                }
            )
        }

        composable("story_post_ar") {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                com.memori.app.ui.screens.home.HomeMapScreen(
                    onNavigateToProfile = {}
                )
                StoryScreen(
                    dialogos = dialogosPostAR,
                    onFinished = {
                        navController.navigate(Screen.Puzzle.route)
                    }
                )
            }
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
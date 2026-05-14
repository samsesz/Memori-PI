package com.memori.app.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Cadastro : Screen("cadastro")
    object Confirmacao : Screen("confirmacao")
    object HomeMap : Screen("home_map")
    object Profile : Screen("profile")
    object ARScanner : Screen("ar_scanner")
    object Puzzle : Screen("puzzle")
    object Ranking : Screen("ranking")
}

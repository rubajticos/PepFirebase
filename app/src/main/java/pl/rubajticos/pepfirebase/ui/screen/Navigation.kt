package pl.rubajticos.pepfirebase.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

const val PROJECT_URI = "https://pepexample.pl"
const val BOOK_DETAILS_URI = "$PROJECT_URI/bookId="

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(
            route = Screen.BookDetailScreen.route + "/{bookId}",
            arguments = listOf(
                navArgument("bookId") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            ),
            deepLinks = listOf(navDeepLink { uriPattern = "$BOOK_DETAILS_URI{bookId}" })
        ) { entry ->
            BookDetailScreen(bookId = entry.arguments?.getString("bookId"))
        }
    }
}
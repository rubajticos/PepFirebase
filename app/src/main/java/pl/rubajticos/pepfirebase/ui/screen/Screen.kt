package pl.rubajticos.pepfirebase.ui.screen

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object BookDetailScreen : Screen("book_details_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}

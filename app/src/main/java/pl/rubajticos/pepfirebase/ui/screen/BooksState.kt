package pl.rubajticos.pepfirebase.ui.screen

import com.google.firebase.auth.FirebaseUser
import pl.rubajticos.pepfirebase.model.Book

data class BooksState(
    val books: List<Book> = emptyList(),
    val user: FirebaseUser? = null,
    val beginLoginFlow: Boolean = false
)

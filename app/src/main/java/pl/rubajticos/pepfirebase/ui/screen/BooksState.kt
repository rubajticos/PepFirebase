package pl.rubajticos.pepfirebase.ui.screen

import pl.rubajticos.pepfirebase.model.Book

data class BooksState(
    val books: List<Book> = emptyList()
)

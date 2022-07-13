package pl.rubajticos.pepfirebase

import pl.rubajticos.pepfirebase.model.Book

data class BooksState(
    val books: List<Book> = emptyList()
)

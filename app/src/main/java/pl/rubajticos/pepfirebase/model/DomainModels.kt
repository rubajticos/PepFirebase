package pl.rubajticos.pepfirebase.model

import java.util.*

data class Person(
    val id: String,
    val firstName: String,
    val lastName: String,
    val books: List<Book> = emptyList()
)

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val year: String,
    val borrowStatus: BorrowStatus
)

data class BorrowStatus(
    val isBorrowed: Boolean,
    val borrowedBy: Person?,
    val borrowedTo: Date?
)

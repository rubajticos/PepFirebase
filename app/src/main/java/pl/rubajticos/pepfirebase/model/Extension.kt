package pl.rubajticos.pepfirebase.model

import java.text.SimpleDateFormat
import java.util.*

fun PersonEntity.toPerson(): Person = Person(
    id = this.id ?: "",
    firstName = this.firstName ?: "",
    lastName = this.lastName ?: "",
    books = emptyList()
)

fun BookEntity.toBook(): Book = Book(
    id = this.id ?: "",
    title = this.title ?: "",
    author = this.author ?: "",
    year = this.year?.toLongOrNull() ?: -1L,
    borrowStatus = BorrowStatus(
        isBorrowed = this.status.isBorrowed?.lowercase()?.toBooleanStrictOrNull() ?: false,
        borrowedBy = null,
        borrowedById = this.status.borrowedBy,
        borrowedTo = this.status.borrowedTo?.toDate()
    )

)

fun String.toDate(): Date? {
    if (this.isBlank()) return null

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return dateFormat.parse(this)
}

fun Date.toDateString(): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy 'r.'", Locale.getDefault())
    return dateFormat.format(this)
}
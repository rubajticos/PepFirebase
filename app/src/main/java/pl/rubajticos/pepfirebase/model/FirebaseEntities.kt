package pl.rubajticos.pepfirebase.model

data class PersonEntity(
    val id: String,
    val firstName: String,
    val lastName: String
)

data class BookEntity(
    val id: String,
    val title: String,
    val author: String,
    val year: String,
    val status: Status
)

data class Status(
    val isBorrowed: Boolean,
    val borrowedBy: String?,
    val borrowedTo: String?
)
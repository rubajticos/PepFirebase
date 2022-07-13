package pl.rubajticos.pepfirebase.model

data class PersonEntity(
    var id: String = "",
    var firstName: String = "",
    var lastName: String = ""
)

data class BookEntity(
    var id: String = "",
    var title: String = "",
    var author: String = "",
    var year: Long = -1L,
    var status: Status = Status(false, null, null)
)

data class Status(
    var isBorrowed: Boolean = false,
    var borrowedBy: String? = null,
    var borrowedTo: String? = null
)
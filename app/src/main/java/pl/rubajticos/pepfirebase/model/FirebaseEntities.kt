package pl.rubajticos.pepfirebase.model

data class PersonEntity(
    var id: String? = null,
    var firstName: String? = null,
    var lastName: String? = null
)

data class BookEntity(
    var id: String? = null,
    var title: String? = null,
    var author: String? = null,
    var year: String? = null,
    var status: Status = Status(null, null, null)
)

data class Status(
    @field:JvmField
    var isBorrowed: String? = null,
    var borrowedBy: String? = null,
    var borrowedTo: String? = null
)
package pl.rubajticos.pepfirebase.ui.screen

data class BookDetailsState(
    val title: String = "",
    val author: String = "",
    val year: Long = -1,
    val isBorrowed: Boolean = false,
    val borrowedBy: String? = null,
    val borrowedByFullName: String? = null,
    val borrowedTo: String? = null,
    val error: String? = null
)

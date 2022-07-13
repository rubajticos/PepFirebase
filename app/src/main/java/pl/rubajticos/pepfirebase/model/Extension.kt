package pl.rubajticos.pepfirebase.model

fun PersonEntity.toPerson(): Person = Person(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    books = emptyList()
)
package pl.rubajticos.pepfirebase.data

import kotlinx.coroutines.flow.Flow
import pl.rubajticos.pepfirebase.model.Book

interface BookRepository {

    suspend fun allBooks(): Flow<List<Book>>

}
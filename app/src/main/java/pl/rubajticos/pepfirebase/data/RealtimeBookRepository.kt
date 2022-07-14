package pl.rubajticos.pepfirebase.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import pl.rubajticos.pepfirebase.model.Book
import pl.rubajticos.pepfirebase.model.BookEntity
import pl.rubajticos.pepfirebase.model.toBook
import javax.inject.Inject

class RealtimeBookRepository @Inject constructor(
    private val database: FirebaseDatabase
) : BookRepository {
    override suspend fun allBooks(): Flow<Result<List<Book>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val books = snapshot.children.mapNotNull {
                        it.getValue(BookEntity::class.java)?.toBook()
                    }
                    this@callbackFlow.trySendBlocking(Result.success(books))
                } else {
                    this@callbackFlow.trySendBlocking(Result.success(emptyList()))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }

        database.getReference(BOOKS_REFERENCE)
            .addValueEventListener(listener)

        awaitClose {
            database.getReference(BOOKS_REFERENCE)
                .removeEventListener(listener)
        }
    }

    override suspend fun findBookById(id: String): Flow<Result<Book>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.getValue(BookEntity::class.java)?.toBook()?.let {
                        this@callbackFlow.trySendBlocking(Result.success(it))
                    } ?: run {
                        this@callbackFlow.trySendBlocking(Result.failure(Exception("Person with ID=$id not found")))
                    }
                } else {
                    this@callbackFlow.trySendBlocking(Result.failure(Exception("Person with ID=$id not found")))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }
        database.getReference(BOOK_REFERENCE + id)
            .addValueEventListener(listener)

        awaitClose {
            database.getReference(BOOK_REFERENCE + id).removeEventListener(listener)
        }
    }

    companion object {
        private const val MAIN_REFERENCE = "1BTAE3GTAtnKEporJLcBK7WquSX33bW1EvI1pm3Z9wMw"
        private const val BOOKS_REFERENCE = "$MAIN_REFERENCE/Books"
        private const val BOOK_REFERENCE = "$MAIN_REFERENCE/Books/"
    }
}
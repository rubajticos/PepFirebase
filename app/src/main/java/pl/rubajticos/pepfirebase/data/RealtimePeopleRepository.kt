package pl.rubajticos.pepfirebase.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import pl.rubajticos.pepfirebase.model.Person
import pl.rubajticos.pepfirebase.model.PersonEntity
import pl.rubajticos.pepfirebase.model.toPerson
import javax.inject.Inject

class RealtimePeopleRepository @Inject constructor(
    private val database: FirebaseDatabase
) : PeopleRepository {


    override suspend fun allPeople(): Flow<Result<List<Person>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val people = snapshot.children.mapNotNull {
                        it.getValue(PersonEntity::class.java)?.toPerson()
                    }
                    this@callbackFlow.trySendBlocking(Result.success(people))
                } else {
                    this@callbackFlow.trySendBlocking(Result.success(emptyList()))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }

        database.getReference(PEOPLE_REFERENCE)
            .addValueEventListener(listener)

        awaitClose {
            database.getReference(PEOPLE_REFERENCE)
                .removeEventListener(listener)
        }
    }

    override suspend fun findById(id: String): Result<Person> {
        TODO("Not yet implemented")
    }

    companion object {
        private const val MAIN_REFERENCE = "1BTAE3GTAtnKEporJLcBK7WquSX33bW1EvI1pm3Z9wMw"
        private const val PEOPLE_REFERENCE = "$MAIN_REFERENCE/People"
    }
}
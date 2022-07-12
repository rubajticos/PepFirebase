package pl.rubajticos.pepfirebase.data

import android.util.Log
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
import javax.inject.Inject

class RealtimePeopleRepository @Inject constructor(
    private val database: FirebaseDatabase
) : PeopleRepository {


    override suspend fun allPeople(): Flow<Result<List<Person>>> = callbackFlow {
        database.reference.child("People")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.value
                        snapshot.getValue(PersonEntity::class.java)
                        Log.d("MRMR", "Something here")

                    } else {
                        Log.d("MRMR", "Empty list")
                        this@callbackFlow.trySendBlocking(Result.success(emptyList()))
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("MRMR", "${error.toException().localizedMessage ?: "unknonwn error"}")
                    this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
                }

            })
        awaitClose {}
    }

    override suspend fun findById(id: String): Result<Person> {
        TODO("Not yet implemented")
    }
}
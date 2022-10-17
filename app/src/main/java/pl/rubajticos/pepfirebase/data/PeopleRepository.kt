package pl.rubajticos.pepfirebase.data

import kotlinx.coroutines.flow.Flow
import pl.rubajticos.pepfirebase.model.Person

interface PeopleRepository {

    suspend fun allPeople(): Flow<Result<List<Person>>>

    suspend fun findById(id: String): Result<Person>

}
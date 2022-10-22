package pl.rubajticos.pepfirebase.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.lang.Byte
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.rubajticos.pepfirebase.data.PeopleRepository
import pl.rubajticos.pepfirebase.data.RealtimeBookRepository
import timber.log.Timber

@HiltViewModel
class MainViewModel @Inject constructor(
    private val peopleRepository: PeopleRepository,
    private val booksRepository: RealtimeBookRepository,
    private val database: FirebaseDatabase
) : ViewModel() {

    var state by mutableStateOf(BooksState())

    init {
        viewModelScope.launch {
            peopleRepository.allPeople()
                .collect {
                    it.onSuccess { people -> Timber.d("MRMR ${people.joinToString(";")}") }
                        .onFailure { fail ->
                            Timber.d(" MRMR ${fail.localizedMessage ?: "Unknown error"}")
                        }
                }
        }
        observeBooks()
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.d("MRMR Retrieve token failed because: ${task.exception?.localizedMessage ?: ""}")
                return@addOnCompleteListener
            }

            val token = task.result
            Timber.d("MRMR FCM token $token")
        }

        trySendUpdate()
    }

    private fun trySendUpdate() {
        viewModelScope.launch(Dispatchers.IO) {
            val ref = database.reference
            val update: Map<String, Any> = mapOf(
                "test/1/name" to "Michael",
                "test/1/lastname" to "Scott",
                "error/1/" to listOf(Byte.valueOf(1))
            )

            runCatching {
                ref.updateChildren(update)
                    .addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Timber.d("MRMR Update success")
                        } else {
                            Timber.e("MRMR ${updateTask.exception?.localizedMessage ?: "Error"}")
                        }
                    }
            }
                .onFailure { Timber.e("MRMR error -> ${it.localizedMessage}") }
        }
    }

    private fun observeBooks() = viewModelScope.launch {
        booksRepository.allBooks()
            .collect {
                it.onSuccess { newBooks ->
                    state = state.copy(books = newBooks)
                }
            }
    }

}
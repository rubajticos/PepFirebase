package pl.rubajticos.pepfirebase.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import pl.rubajticos.pepfirebase.data.PeopleRepository
import pl.rubajticos.pepfirebase.data.RealtimeBookRepository
import timber.log.Timber

@HiltViewModel
class MainViewModel @Inject constructor(
    private val peopleRepository: PeopleRepository,
    private val booksRepository: RealtimeBookRepository
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
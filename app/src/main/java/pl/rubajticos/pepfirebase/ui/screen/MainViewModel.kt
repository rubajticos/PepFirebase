package pl.rubajticos.pepfirebase.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Byte
import javax.inject.Inject
import kotlin.Any
import kotlin.String
import kotlin.onFailure
import kotlin.onSuccess
import kotlin.runCatching
import kotlin.to
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
    private val database: FirebaseDatabase,
) : ViewModel() {

    var state by mutableStateOf(BooksState())

    init {
        checkLoggedUser()
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

    private fun checkLoggedUser() = viewModelScope.launch {
        val firebaseAuth = FirebaseAuth.getInstance()
        handleUserLogin(firebaseAuth.currentUser)
    }

    fun loginWithMicrosoft() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val pendingResult = FirebaseAuth.getInstance().pendingAuthResult
            if (pendingResult != null) {
                Timber.d("MRMR There is some pending result!")
                pendingResult.addOnSuccessListener {
                    handleUserLogin(it.user)
                }.addOnFailureListener {
                    Timber.e("MRMR Login error: ${it.localizedMessage}")
                }
            } else {
                Timber.d("MRMR No pending result. Begin login flow.")
                state = state.copy(beginLoginFlow = true)
            }
        }
    }

    fun loginFlowBegin() {
        state = state.copy(beginLoginFlow = false)
    }

    fun handleUserLogin(currentUser: FirebaseUser?) = viewModelScope.launch {
        Timber.d("MRMR Current user: ${currentUser?.email}")
        state = state.copy(user = currentUser)
    }

    fun signOut() = viewModelScope.launch {
        Timber.d("MRMR Signing out...")
        FirebaseAuth.getInstance().signOut()
        handleUserLogin(null)
    }

}
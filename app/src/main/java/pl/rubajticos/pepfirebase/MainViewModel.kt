package pl.rubajticos.pepfirebase

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.rubajticos.pepfirebase.data.PeopleRepository
import pl.rubajticos.pepfirebase.data.RealtimeBookRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val peopleRepository: PeopleRepository,
    private val booksRepository: RealtimeBookRepository
) : ViewModel() {

    var state by mutableStateOf(BooksState())

    init {
        Log.d("MRMR", "VM init")
        viewModelScope.launch {
            peopleRepository.allPeople()
                .collect {
                    it.onSuccess { people -> Log.d("MRMR", people.joinToString("; ")) }
                        .onFailure { fail ->
                            Log.d(
                                "MRMR",
                                fail.localizedMessage ?: "Unknown error"
                            )
                        }
                }
        }
        observeBooks()
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
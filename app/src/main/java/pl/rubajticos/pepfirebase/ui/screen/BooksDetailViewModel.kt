package pl.rubajticos.pepfirebase.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.rubajticos.pepfirebase.data.PeopleRepository
import pl.rubajticos.pepfirebase.data.RealtimeBookRepository
import pl.rubajticos.pepfirebase.model.toDateString
import javax.inject.Inject

@HiltViewModel
class BooksDetailViewModel @Inject constructor(
    private val bookRepository: RealtimeBookRepository,
    private val peopleRepository: PeopleRepository
) : ViewModel() {

    private var bookId: String? = null

    var state by mutableStateOf(BookDetailsState())

    fun init(bookId: String?) {
        this.bookId = bookId
        viewModelScope.launch(Dispatchers.IO) {
            getBookDetails()
        }
    }

    private suspend fun getBookDetails() {
        bookId?.let { id ->
            bookRepository.findBookById(id)
                .collect {
                    it.onSuccess { book ->
                        state = state.copy(
                            title = book.title,
                            author = book.author,
                            year = book.year,
                            isBorrowed = book.borrowStatus.isBorrowed,
                            borrowedBy = book.borrowStatus.borrowedById,
                            borrowedTo = book.borrowStatus.borrowedTo?.toDateString()
                        )

                        if (book.borrowStatus.isBorrowed && state.borrowedByFullName == null) {
                            fillFullBorrowedPersonData(book.borrowStatus.borrowedById)
                        }
                    }
                        .onFailure { error ->
                            state = state.copy(error = error.localizedMessage ?: "Error!")
                            Log.d(
                                "MRMR",
                                "Some error ${error.localizedMessage}"
                            )
                        }
                }
        }
    }

    private suspend fun fillFullBorrowedPersonData(personId: String?) {
        personId?.let { id ->
            delay(2000)
            peopleRepository.findById(id)
                .onSuccess { person ->
                    state =
                        state.copy(borrowedByFullName = "${person.firstName} ${person.lastName}")
                }
                .onFailure {
                    Log.d(
                        "MRMR",
                        "Person data fetch error -> ${it.localizedMessage ?: ""}"
                    )
                }
        }
    }
}

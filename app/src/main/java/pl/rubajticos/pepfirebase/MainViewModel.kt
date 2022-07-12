package pl.rubajticos.pepfirebase

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.rubajticos.pepfirebase.data.PeopleRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val peopleRepository: PeopleRepository
) : ViewModel() {

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
    }

    fun sayHello() = Log.d("MRMR", "Hello !")

}
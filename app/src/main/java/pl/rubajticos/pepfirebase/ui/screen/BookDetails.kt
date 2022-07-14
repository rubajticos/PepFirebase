package pl.rubajticos.pepfirebase.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BookDetailScreen(bookId: String?, viewModel: BooksDetailViewModel = hiltViewModel()) {
    val state = viewModel.state
    viewModel.init(bookId)
    Log.d("MRMR", "State = $state")

    Column {
        Text(text = "Details screen for book with ID = $bookId")
    }
}
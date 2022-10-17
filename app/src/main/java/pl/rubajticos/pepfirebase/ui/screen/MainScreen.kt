package pl.rubajticos.pepfirebase.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pl.rubajticos.pepfirebase.model.Book

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val state = viewModel.state
    BooksList(state.books, navController)
}

@Composable
fun BooksList(books: List<Book>, navController: NavController) {
    LazyColumn() {
        items(books) { item ->
            BookRow(book = item) { book ->
                navController.navigate(Screen.BookDetailScreen.withArgs(book.id))
            }
        }
    }
}

@Composable
fun BookRow(book: Book, onClick: (Book) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(5.dp)
            .border(2.dp, Color.Blue, RoundedCornerShape(5.dp))
            .padding(4.dp)
            .fillMaxSize()
            .clickable { onClick(book) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .defaultMinSize(30.dp)
        ) {
            if (book.borrowStatus.isBorrowed) {
                Indicator(color = Color.Red)
            } else {
                Indicator(color = Color.Green)
            }
        }
        Column {
            BookTitle(book.title)
            BookAuthor(book.author)
        }
    }
}

@Composable
private fun BookTitle(title: String) {
    Text(
        modifier = Modifier.padding(5.dp),
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun BookAuthor(author: String) {
    Text(
        modifier = Modifier.padding(5.dp, 2.dp, 5.dp, 5.dp),
        text = author,
        fontSize = 14.sp,
        fontStyle = FontStyle.Italic
    )
}

@Composable
fun Indicator(color: Color) {
    Canvas(modifier = Modifier.size(20.dp), onDraw = {
        drawCircle(color = color)
    })
}
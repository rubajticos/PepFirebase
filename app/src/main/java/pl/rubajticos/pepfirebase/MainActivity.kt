package pl.rubajticos.pepfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import pl.rubajticos.pepfirebase.model.Book
import pl.rubajticos.pepfirebase.model.BorrowStatus
import pl.rubajticos.pepfirebase.ui.theme.PepFirebaseTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PepFirebaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    val viewModel = viewModel<MainViewModel>()
                    val state = viewModel.state
                    BooksList(state.books)
                }
            }
        }
    }
}

@Composable
fun BooksList(books: List<Book>) {
    LazyColumn() {
        items(books) { item ->
            BookRow(book = item)
        }
    }
}

@Composable
fun BookRow(book: Book) {
    Row(
        modifier = Modifier
            .background(Color.Magenta)
            .padding(5.dp)
            .border(2.dp, Color.Blue)
            .padding(4.dp)
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.background(Color.Yellow)) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = book.title,
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier.padding(5.dp, 2.dp, 5.dp, 5.dp),
                text = book.author,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.background(Color.Gray)
        ) {
            if (book.borrowStatus.isBorrowed) {
                Indicator(color = Color.Red)
            } else {
                Indicator(color = Color.Green)
            }
        }
    }
}

@Preview
@Composable
fun BookRowPreview() {
    val book = Book(
        id = "book",
        title = "Some title",
        author = "Some author",
        year = 1980,
        borrowStatus = BorrowStatus(false, null, null, null)
    )
    BookRow(book)
}

@Composable
fun Indicator(color: Color) {
    Canvas(modifier = Modifier.size(30.dp), onDraw = {
        drawCircle(color = color)
    })
}

package pl.rubajticos.pepfirebase.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BookDetailScreen(bookId: String?, viewModel: BooksDetailViewModel = hiltViewModel()) {
    val state = viewModel.state
    viewModel.init(bookId)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = state.title,
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            color = Color.Blue,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.size(15.dp))
        Text(
            text = state.author,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = Color.DarkGray,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.size(5.dp))
        Text(
            text = state.year.toString(),
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        if (state.isBorrowed) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .border(2.dp, Color.Blue, RoundedCornerShape(CornerSize(5.dp)))
                    .padding(10.dp)
            ) {
                Column {
                    Text(
                        text = "Wypożyczona przez",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = state.borrowedByFullName ?: run {
                            state.borrowedBy ?: "?"
                        },
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(

                        text = "do ${state.borrowedTo ?: "?"}",
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
package pl.rubajticos.pepfirebase.ui.screen

import android.app.Activity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.channels.trySendBlocking
import pl.rubajticos.pepfirebase.model.Book
import timber.log.Timber

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val state = viewModel.state
    val activity = LocalContext.current as Activity
    val firebaseAuth = FirebaseAuth.getInstance()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserPanel(activity, firebaseAuth, state, viewModel)
        BooksList(state.books, navController)
    }
}

@Composable
fun UserPanel(
    activity: Activity,
    firebaseAuth: FirebaseAuth,
    state: BooksState,
    viewModel: MainViewModel,
) {
    if (state.beginLoginFlow) {
        val provider = OAuthProvider.newBuilder("microsoft.com")
        firebaseAuth.startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener {
                viewModel.handleUserLogin(it.user)
            }
            .addOnFailureListener {
                Timber.d("MRMR Error now = ${it.localizedMessage}")
            }
        viewModel.loginFlowBegin()
    } else {
        if (state.user == null) {
            LoginButton() {
                viewModel.loginWithMicrosoft()
            }
        } else {
            SignOutButton() {
                viewModel.signOut()
            }
            UserDetails(state.user)
        }
    }
}

@Composable
fun UserDetails(user: FirebaseUser?) {
    Text(text = user?.displayName ?: "Name")
    Text(text = user?.email ?: "E-mail")
    Text(text = user?.phoneNumber ?: "Phone")
}

@Composable
fun SignOutButton(onClick: () -> Unit) {
    Button(onClick = {
        onClick.invoke()
    }) {
        Text(text = "Sign out")
    }
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    Button(onClick = {
        onClick.invoke()
    }) {
        Text(text = "Sign In with Microsoft")
    }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
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
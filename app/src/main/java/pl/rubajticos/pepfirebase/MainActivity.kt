package pl.rubajticos.pepfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import pl.rubajticos.pepfirebase.ui.theme.PepFirebaseTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PepFirebaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    Greeting("Android", viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, viewModel: MainViewModel) {
    Text(
        text = "Hello $name!",
    )
    Button(onClick = { viewModel.sayHello() }) {
        Text("BTN")
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    PepFirebaseTheme {
//        Greeting("Android")
//    }
//}
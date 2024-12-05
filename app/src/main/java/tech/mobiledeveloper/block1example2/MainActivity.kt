package tech.mobiledeveloper.block1example2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import tech.mobiledeveloper.block1example2.screens.LoggedInScreen
import tech.mobiledeveloper.block1example2.screens.LoginScreen
import tech.mobiledeveloper.block1example2.ui.theme.Block1Example2Theme

enum class Navigation {
    Login, LoggedIn
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Block1Example2Theme {
                Box(modifier = Modifier.fillMaxSize()) {
                    var currentScreen by remember { mutableStateOf(Navigation.Login) }

                    when (currentScreen) {
                        Navigation.Login -> LoginScreen {
                            currentScreen = Navigation.LoggedIn
                        }

                        Navigation.LoggedIn -> LoggedInScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Block1Example2Theme {
        Greeting("Android")
    }
}
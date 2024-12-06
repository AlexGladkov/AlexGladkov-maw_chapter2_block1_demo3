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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import tech.mobiledeveloper.auth.AuthorizationHandlerImpl
import tech.mobiledeveloper.auth.TokenManagerImpl
import tech.mobiledeveloper.auth_api.AuthStatus
import tech.mobiledeveloper.auth_api.AuthorizationHandler
import tech.mobiledeveloper.block1example2.screens.LoggedInScreen
import tech.mobiledeveloper.block1example2.screens.LoginScreen
import tech.mobiledeveloper.block1example2.ui.theme.Block1Example2Theme
import tech.mobiledeveloper.core.KtorClient
import tech.mobiledeveloper.core.setupMockWebServer

enum class Navigation {
    Login, LoggedIn
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val tokenManager = TokenManagerImpl()
        val authorizationHandler = AuthorizationHandlerImpl()
        val mockWebServer = setupMockWebServer()
        val ktorClient = KtorClient(tokenManager, mockWebServer, authorizationHandler)

        setContent {
            val authState by authorizationHandler.isAuth.collectAsState()

            CompositionLocalProvider(
                LocalHttpClient provides ktorClient
            ) {
                Block1Example2Theme {
                    Box(modifier = Modifier.fillMaxSize()) {
                        when (authState) {
                            is AuthStatus.Login -> {
                                LoginScreen {
                                    authorizationHandler.login((authState as AuthStatus.Login).withMerge)
                                }
                            }

                            AuthStatus.LoggedIn -> LoggedInScreen()
                        }
                    }
                }
            }
        }
    }
}

val LocalHttpClient = staticCompositionLocalOf<KtorClient> { error("No default implementation") }
package tech.mobiledeveloper.block1example2.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.mobiledeveloper.block1example2.LocalHttpClient

@Composable
fun LoggedInScreen() {
    val coroutineScope = rememberCoroutineScope()
    val ktorClient = LocalHttpClient.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(12.dp),
                text = "You're logged in",
                fontSize = 20.sp
            )

            Button(onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    ktorClient.makeExpiredRequest()
                }
            }) {
                Text("Make expired token request")
            }
        }
    }
}
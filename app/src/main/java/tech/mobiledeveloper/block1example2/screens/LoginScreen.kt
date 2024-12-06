package tech.mobiledeveloper.block1example2.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import tech.mobiledeveloper.block1example2.LocalHttpClient

@Composable
fun LoginScreen(next: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(12.dp),
                text = "Hello, World",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                modifier = Modifier.width(300.dp),
                onClick = {
                    next.invoke()
                }) {
                Text("Login")
            }
        }
    }
}
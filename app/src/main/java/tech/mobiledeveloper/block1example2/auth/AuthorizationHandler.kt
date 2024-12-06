package tech.mobiledeveloper.block1example2.auth

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthorizationHandler {
    private val _isAuth = MutableStateFlow(true)
    val isAuth: StateFlow<Boolean> = _isAuth

    fun logout() {
        _isAuth.value = false
    }

    fun login() {
        _isAuth.value = true
    }
}
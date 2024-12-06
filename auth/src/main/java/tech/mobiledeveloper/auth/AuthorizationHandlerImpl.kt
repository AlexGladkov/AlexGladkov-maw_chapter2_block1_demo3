package tech.mobiledeveloper.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tech.mobiledeveloper.auth_api.AuthStatus
import tech.mobiledeveloper.auth_api.AuthorizationHandler

class AuthorizationHandlerImpl: AuthorizationHandler {
    private val _isAuth = MutableStateFlow<AuthStatus>(AuthStatus.Login(false))
    var isAuth: StateFlow<AuthStatus> = _isAuth

    override fun logout() {
        _isAuth.value = AuthStatus.Login(withMerge = false)
    }

    override fun login(withMerge: Boolean) {
        _isAuth.value = AuthStatus.LoggedIn
    }
}
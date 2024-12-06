package tech.mobiledeveloper.auth_api

sealed class AuthStatus {
    data object LoggedIn : AuthStatus()
    class Login(val withMerge: Boolean) : AuthStatus()
}

interface AuthorizationHandler {
    fun logout()
    fun login(withMerge: Boolean)
}
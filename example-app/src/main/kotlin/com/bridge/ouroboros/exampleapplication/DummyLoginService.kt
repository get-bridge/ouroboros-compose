package com.bridge.ouroboros.exampleapplication

import android.util.Log
import kotlinx.coroutines.delay

class DummyLoginService {

    suspend fun login(username: String, password: String) {
        Log.d("LoginService", "Logging in using $username and ${"*".repeat(password.length)}")
        delay(3000)
    }

}
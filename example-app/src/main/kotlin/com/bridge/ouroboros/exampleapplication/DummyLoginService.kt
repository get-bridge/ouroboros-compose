package com.bridge.ouroboros.exampleapplication

import android.util.Log
import kotlinx.coroutines.delay

interface LoginService {
    suspend fun login(username: String, password: String)
}

class DummyLoginService : LoginService {

    override suspend fun login(username: String, password: String) {
        Log.d("LoginService", "Logging in using $username and ${"*".repeat(password.length)}")

        delay(3000)

        if (username == "willfail") {
            throw RuntimeException("Oh my!")
        }
    }

}
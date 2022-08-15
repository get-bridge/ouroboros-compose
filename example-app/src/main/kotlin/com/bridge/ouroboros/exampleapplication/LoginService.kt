package com.bridge.ouroboros.exampleapplication

interface LoginService {
    suspend fun login(username: String, password: String)
}
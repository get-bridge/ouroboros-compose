package com.bridge.ouroboros.exampleapplication.examples.login

interface LoginService {
    suspend fun login(username: String, password: String)
}
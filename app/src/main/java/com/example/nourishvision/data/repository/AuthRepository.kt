// data/repository/AuthRepository.kt
package com.example.nourishvision.data.repository

import com.example.nourishvision.data.model.RegisterRequest
import com.example.nourishvision.data.model.UserResponse
import com.example.nourishvision.data.remote.RetrofitInstance

class AuthRepository {
    suspend fun registerUser(name: String, email: String, password: String): UserResponse {
        return RetrofitInstance.api.registerUser(
            RegisterRequest(name = name, email = email, password = password)
        )
    }

    suspend fun getUser(userId: Int): UserResponse {
        return RetrofitInstance.api.getUser(userId)
    }

    suspend fun getAllUsers(): List<UserResponse> {
        return RetrofitInstance.api.getAllUsers()
    }

    suspend fun login(email: String, password: String): UserResponse {
        val allUsers = RetrofitInstance.api.getAllUsers()
        val matchedUser = allUsers.find { user ->
            user.email.equals(email, ignoreCase = true) && user.password == password
        }
        return matchedUser ?: throw Exception("Email atau password salah")
    }

}
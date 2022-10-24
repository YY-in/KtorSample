package com.example.data.request

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val username: String,
    val avatarUrl: String,
    val verified: Boolean,
    val email: String?,
    val phone: String?,
    val password: String,
)
@Serializable
data class AuthRequest(
    val email: String?,
    val phone: String?,
    val password: String,
)
package com.example.security.token

/**
 * @param issuer who issued this token (usually the server)
 * @param audience who is the audience of this token (usually the client),and verify the audience(like normal user or admin)
 * @param expiresIn how long this token is valid
 * @param secret the secret key to sign the token
 */
data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val secret: String
)

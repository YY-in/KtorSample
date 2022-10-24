package com.example.security.token

/**
 * key-value pair to store information in the token
 */
data class TokenClaim(
    val name :String,
    val value:String
)

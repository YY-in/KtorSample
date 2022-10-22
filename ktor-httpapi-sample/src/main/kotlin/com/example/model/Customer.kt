package com.example.model

import kotlinx.serialization.Serializable

/**
 * Together with its Ktor integration,
 * this will allow us to generate the JSON representation we need for our API responses automatically
 * – as we will see in just a bit.
 */
@Serializable
data class Customer(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String
)

val customerStorage = mutableListOf<Customer>()
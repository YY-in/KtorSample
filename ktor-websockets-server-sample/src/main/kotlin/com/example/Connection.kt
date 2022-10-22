package com.example

import io.ktor.websocket.*
import java.util.concurrent.atomic.*

/**
 * we can simplify the problem of assigning usernames,
 * and just give each participant an auto-generated username based on a counter.
 */
class Connection(val session: DefaultWebSocketSession) {
    companion object {
        //using AtomicInteger as a thread-safe data structure for the counter
        //This ensures that two users will never receive the same ID for their username
        val lastId = AtomicInteger(0)
    }
    val name = "user${lastId.getAndIncrement()}"
}
package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*

fun main() {
    // the embeddedServer function is used to configure server parameters and run an application
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        //  is an extension function that defines routing.
        //  This function is declared in a separate plugins package (the Routing.kt file)
        configureRouting()
    }.start(wait = true)
}

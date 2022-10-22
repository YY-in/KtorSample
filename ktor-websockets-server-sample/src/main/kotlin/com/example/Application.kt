package com.example

import io.ktor.server.application.*
import com.example.plugins.*
import configureSockets

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureSockets()
    configureRouting()
}

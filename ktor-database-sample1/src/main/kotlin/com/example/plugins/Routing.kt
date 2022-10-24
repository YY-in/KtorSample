package com.example.plugins

import com.example.authenticate
import com.example.data.user.MongoUserDataSource
import com.example.getSecretInfo
import com.example.security.hashing.HashingService
import com.example.security.token.JwtTokenService
import com.example.security.token.TokenConfig
import com.example.signIn
import com.example.signUp
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureRouting(
    userDataSource: MongoUserDataSource,
    hashingService: HashingService,
    tokenConfig: TokenConfig,
    tokenService: JwtTokenService
) {

    routing {
        signIn(hashingService, tokenService, userDataSource, tokenConfig)
        signUp(hashingService,userDataSource)
        authenticate()
        getSecretInfo()
    }
}

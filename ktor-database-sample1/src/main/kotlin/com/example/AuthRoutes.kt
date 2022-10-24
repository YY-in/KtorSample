package com.example

import com.example.data.request.AuthRequest
import com.example.data.request.SignInRequest
import com.example.data.response.AuthResponse
import com.example.data.user.MongoUserDataSource
import com.example.data.user.UserPrivate
import com.example.security.hashing.HashingService
import com.example.security.hashing.SaltedHash
import com.example.security.token.JwtTokenService
import com.example.security.token.TokenClaim
import com.example.security.token.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: MongoUserDataSource
) {
    post("signup") {
        // check if the request is nullable
        val request = call.receiveNullable<SignInRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        // check if the data is in correct format
        val areFieldBlank = request.username.isBlank() || request.password.isBlank()
        val isPwTooShort = request.password.length < 8
        if (areFieldBlank || isPwTooShort) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }
        // salt and hash the password
        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = UserPrivate(
            password = saltedHash.hash,
            salt = saltedHash.salt,
            bot = false,
            bio = null,
            username = request.username,
            avatarUrl = request.avatarUrl,
            email = request.email,
            phone = request.phone,
            verified = request.verified
        )
        val wasAcknowledged = userDataSource.insertUser(user)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.signIn(
    hashingService: HashingService,
    tokenService: JwtTokenService,
    userDataSource: MongoUserDataSource,
    tokenConfig: TokenConfig
) {
    post("signin") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userDataSource.getUserByEmail(request.email)
            ?: userDataSource.getUserByPhone(request.phone)

        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "未查找到用户")
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, "密码错误")
            return@post
        }

        // create a token to save the user's id
        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )
        // we want to attach the token to the response
        // and ktor will then automatically pass that to json and send back to the client
        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token,
            )
        )
    }
}

// to this authenticate route will just check if the token is saved in preference by user
fun Route.authenticate(
) {
    // the check already handled by this function
   authenticate {
       get("authenticate") {
           // it will make sure that our default authentication mechanism is used to verify
           // if the request is authenticated
           call.respond(HttpStatusCode.OK)
           //if the client is not authenticated anymore,they need to direct the user back to log in
           //or save credentials of the user in some kind of encrypted format and automatically re-log in and get the token
       }
   }
}

//you can get this user id from the user who is authenticated
fun Route.getSecretInfo(){
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId",String::class)

            call.respond(HttpStatusCode.OK, "You userId is $userId")
        }
    }
}
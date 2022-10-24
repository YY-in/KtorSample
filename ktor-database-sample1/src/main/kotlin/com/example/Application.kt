package com.example

import com.example.dada.user.MongoUserDataSource
import com.example.dada.user.UserPrivate
import io.ktor.server.application.*
import com.example.plugins.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@OptIn(DelicateCoroutinesApi::class)
@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val mongoPw = System.getenv("MONGO_PW")
    val dbName = "ktor-auth"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://dadapenguin:$mongoPw@cluster0.lgrwfo9.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase(dbName)

    val userDataSource = MongoUserDataSource(db)

    GlobalScope.launch {
        val user = UserPrivate(
            username = "Yiin",
            password = "123456789",
            discriminator = "0001",
            avatarUrl = "https://qiniu.yyin.top/yuji.png",
            bot = false,
            bio = "hello world",
            verified = true,
            email = "bruneed237@gmail.com",
            phone = "123456789",
            locale = "zh-CN",
            salt = "salt"
        )
        userDataSource.insertUser(user)

    }

    configureSecurity()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}

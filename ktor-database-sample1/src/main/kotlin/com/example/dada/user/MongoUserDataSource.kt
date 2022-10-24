package com.example.dada.user

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserDataSource(
    db: CoroutineDatabase
) : UserDataSource {
    private val users = db.getCollection<UserPrivate>()

    override suspend fun getUserByUsername(username: String): UserPrivate? {
        return users.findOne(UserPrivate::username eq username)
    }

    override suspend fun getUserByEmail(email: String): UserPrivate? {
      return users.findOne(UserPrivate::email eq email)
    }

    override suspend fun getUserByPhone(phone: String): UserPrivate? {
        return users.findOne(UserPrivate::phone eq phone)
    }

    override suspend fun insertUser(user: UserPrivate): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

}
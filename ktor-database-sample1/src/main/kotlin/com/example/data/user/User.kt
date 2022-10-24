package com.example.data.user

import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

sealed class User{

    abstract val username: String
    abstract val discriminator: String
    abstract val avatarUrl: String
    abstract val bot: Boolean
    abstract val bio: String?

    private val formattedDiscriminator: String
        get() = "#$discriminator"

    val tag: String
        get() = "$username$formattedDiscriminator"
}

data class UserPublic(
    override val username: String,
    override val discriminator: String,
    override val avatarUrl: String,
    override val bot: Boolean,
    override val bio: String?,
    val pronouns: String?,
) : User()

data class UserPrivate(
    @BsonId val id: ObjectId = ObjectId(),
    override val username: String,
    override val discriminator: String = UUIDGenerator().generateId(id).toString().substring(0, 4),
    override val avatarUrl: String,
    override val bot: Boolean,
    override val bio: String?,
    val verified: Boolean,
    val email: String?,
    val phone: String?,
    val password: String,
    val salt: String,
) : User()

data class UserReadyEvent(
    override val username: String,
    override val discriminator: String,
    override val avatarUrl: String,
    override val bot: Boolean,
    override val bio: String?,
    val mfaEnabled: Boolean,
    val verified: Boolean,
    val premium: Boolean,
    val purchasedFlags: Int, // TODO: implement bitfield
) : User()

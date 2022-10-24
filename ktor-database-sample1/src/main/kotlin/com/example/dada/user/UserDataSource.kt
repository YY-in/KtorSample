package com.example.dada.user

interface UserDataSource {
    // 查询并返回用户信息
    suspend fun getUserByUsername(username: String): UserPrivate?
    suspend fun getUserByEmail(email: String): UserPrivate?
    suspend fun getUserByPhone(phone: String): UserPrivate?

    // 创建用户
    suspend fun insertUser(user: UserPrivate): Boolean

    
}
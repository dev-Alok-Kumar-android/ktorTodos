package com.alokkumar.data.dao

import com.alokkumar.data.User

interface UserDao {

    suspend fun createUser(
        email:String,
        name:String,
        password:String
    ): User?

    suspend fun findUser(
        userId:Int
    ):User?

    suspend fun findUserByEmail(
        email:String
    ):User?

    suspend fun deleteUser(userId: Int):Int?

    suspend fun updateAllData(
        id:Int,
        name:String,
        email: String,
        password: String
    ):Int?

    suspend fun updateAnyData(
        id: Int,
        name: String,
        email: String,
        password: String
    ):Int?
}
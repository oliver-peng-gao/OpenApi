package com.olivergao.openapi.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import com.olivergao.openapi.models.Account
import com.olivergao.openapi.models.AuthToken

@Database(
    entities = [Account::class, AuthToken::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAuthTokenDao(): AuthTokenDao
    abstract fun getAccountDao(): AccountDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }
}
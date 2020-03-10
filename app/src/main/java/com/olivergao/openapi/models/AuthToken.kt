package com.olivergao.openapi.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "auth_token",
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = ["pk"],
        childColumns = ["account_pk"],
        onDelete = CASCADE
    )]
)
data class AuthToken(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "account_pk")
    var account_pk: Int? = -1,

    @ColumnInfo(name = "token")
    var token: String? = null
)
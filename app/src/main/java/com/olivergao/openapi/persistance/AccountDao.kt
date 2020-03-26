package com.olivergao.openapi.persistance

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.olivergao.openapi.models.Account

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAndIgnore(account: Account): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAndReplace(account: Account): Long

    @Query("SELECT * FROM account WHERE email=:email")
    fun searchByEmail(email: String): Account?

    @Query("SELECT * FROM account WHERE pk=:pk")
    fun searchByPk(pk: Int): LiveData<Account>

    @Query("UPDATE account SET email=:email, username=:username WHERE pk=:pk")
    fun updateAccount(pk: Int, email: String, username: String)
}

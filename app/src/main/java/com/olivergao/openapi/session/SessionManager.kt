package com.olivergao.openapi.session

import android.app.Application
import com.olivergao.openapi.persistance.AuthTokenDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
)
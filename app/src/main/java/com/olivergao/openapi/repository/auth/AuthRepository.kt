package com.olivergao.openapi.repository.auth

import com.olivergao.openapi.api.auth.OpenApiAuthService
import com.olivergao.openapi.persistance.AccountDao
import com.olivergao.openapi.persistance.AuthTokenDao
import com.olivergao.openapi.session.SessionManager
import javax.inject.Inject

class AuthRepository
@Inject constructor(
    val authTokenDao: AuthTokenDao,
    val accountDao: AccountDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager
)
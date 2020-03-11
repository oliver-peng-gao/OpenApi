package com.olivergao.openapi.repository.auth

import androidx.lifecycle.LiveData
import com.olivergao.openapi.api.auth.OpenApiAuthService
import com.olivergao.openapi.api.auth.netweorkResponses.LoginResponse
import com.olivergao.openapi.api.auth.netweorkResponses.RegistrationResponse
import com.olivergao.openapi.persistance.AccountDao
import com.olivergao.openapi.persistance.AuthTokenDao
import com.olivergao.openapi.session.SessionManager
import com.olivergao.openapi.util.GenericApiResponse
import javax.inject.Inject

class AuthRepository
@Inject constructor(
    val authTokenDao: AuthTokenDao,
    val accountDao: AccountDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager
) {
    fun testLoginRequest(
        email: String,
        password: String
    ): LiveData<GenericApiResponse<LoginResponse>> {
        return openApiAuthService.login(email, password)
    }

    fun testRegistrationRequest(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<GenericApiResponse<RegistrationResponse>> {
        return openApiAuthService.register(email, username, password, confirmPassword)
    }
}
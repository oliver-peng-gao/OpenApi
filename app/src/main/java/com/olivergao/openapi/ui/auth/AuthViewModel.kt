package com.olivergao.openapi.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.olivergao.openapi.api.auth.netweorkResponses.LoginResponse
import com.olivergao.openapi.api.auth.netweorkResponses.RegistrationResponse
import com.olivergao.openapi.repository.auth.AuthRepository
import com.olivergao.openapi.util.GenericApiResponse
import javax.inject.Inject

class AuthViewModel
@Inject constructor(val authRepository: AuthRepository) : ViewModel() {

    fun testLogin(): LiveData<GenericApiResponse<LoginResponse>> {
        return authRepository.testLoginRequest(
            "cyanolive@gmail.com",
            "8X@jEZ3h"
        )
    }

    fun testRegister(): LiveData<GenericApiResponse<RegistrationResponse>> {
        return authRepository.testRegistrationRequest(
            "cyanolive@gmail.com",
            "cyanolive",
            "123456",
            "12356"
        )
    }
}
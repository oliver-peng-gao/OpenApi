package com.olivergao.openapi.ui.auth

import androidx.lifecycle.ViewModel
import com.olivergao.openapi.repository.auth.AuthRepository
import javax.inject.Inject

class AuthViewModel
@Inject constructor(val authRepository: AuthRepository) : ViewModel() {

}
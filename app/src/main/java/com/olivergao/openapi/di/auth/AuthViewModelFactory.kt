package com.olivergao.openapi.di.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.olivergao.openapi.repository.auth.AuthRepository
import com.olivergao.openapi.ui.auth.AuthViewModel
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory @Inject constructor(private val authRepository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(authRepository) as T
    }
}

package com.olivergao.openapi.di.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.olivergao.openapi.repository.main.AccountRepository
import com.olivergao.openapi.session.SessionManager
import com.olivergao.openapi.ui.main.AccountViewModel
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class AccountViewModelFactory @Inject constructor(
    private val sessionManager: SessionManager,
    private val accountRepository: AccountRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountViewModel(sessionManager, accountRepository) as T
    }
}
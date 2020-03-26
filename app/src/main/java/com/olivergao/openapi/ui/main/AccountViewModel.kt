package com.olivergao.openapi.ui.main

import androidx.lifecycle.LiveData
import com.olivergao.openapi.models.Account
import com.olivergao.openapi.repository.main.AccountRepository
import com.olivergao.openapi.session.SessionManager
import com.olivergao.openapi.ui.BaseViewModel
import com.olivergao.openapi.ui.DataState
import com.olivergao.openapi.ui.main.account.state.AccountStateEvent
import com.olivergao.openapi.ui.main.account.state.AccountStateEvent.*
import com.olivergao.openapi.ui.main.account.state.AccountViewState
import com.olivergao.openapi.util.AbsentLiveData
import javax.inject.Inject

class AccountViewModel
@Inject constructor(
    private val sessionManager: SessionManager,
    private val accountRepository: AccountRepository
) : BaseViewModel<AccountStateEvent, AccountViewState>() {

    fun logout() {
        sessionManager.logout()
    }

    fun setAccountData(account: Account) {
        val update = getCurrentViewStateOrNew()
        if (update.account == account) return
        update.account = account
        _viewState.value = update
    }

    override fun handleStateEvent(stateEvent: AccountStateEvent): LiveData<DataState<AccountViewState>> {
        return when (stateEvent) {
            is GetAccountEvent -> {
                AbsentLiveData.create()
            }
            is ChangePasswordEvent -> {
                AbsentLiveData.create()
            }
            is UpdateAccountEvent -> {
                AbsentLiveData.create()
            }
            is None -> {
                AbsentLiveData.create()
            }
        }
    }

    override fun initNewViewState(): AccountViewState {
        return AccountViewState()
    }
}
package com.olivergao.openapi.repository.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.olivergao.openapi.api.GenericResponse
import com.olivergao.openapi.api.main.OpenApiMainService
import com.olivergao.openapi.models.Account
import com.olivergao.openapi.models.AuthToken
import com.olivergao.openapi.persistance.AccountDao
import com.olivergao.openapi.repository.NetworkBoundResource
import com.olivergao.openapi.session.SessionManager
import com.olivergao.openapi.ui.DataState
import com.olivergao.openapi.ui.Response
import com.olivergao.openapi.ui.ResponseType
import com.olivergao.openapi.ui.main.account.state.AccountViewState
import com.olivergao.openapi.util.AbsentLiveData
import com.olivergao.openapi.util.ApiSuccessResponse
import com.olivergao.openapi.util.GenericApiResponse
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AccountRepository
@Inject constructor(
    val openApiMainService: OpenApiMainService,
    val accountDao: AccountDao,
    val sessionManager: SessionManager
) {
    private val TAG = "AppDebug"

    private var repositoryJob: Job? = null

    fun getAccount(authToken: AuthToken): LiveData<DataState<AccountViewState>> {
        return object : NetworkBoundResource<Account, Account, AccountViewState>(
            isNetworkAvailable = sessionManager.isConnectedToInternet(),
            isNetworkRequest = true,
            shouldLoadFromCache = true,
            shouldCancelIfNoNetwork = false
        ) {
            override suspend fun createCall(): LiveData<GenericApiResponse<Account>> {
                return openApiMainService.getAccount("Token ${authToken.token}")
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<Account>) {
                updateLocalDb(response.body)
                createCacheRequestAndReturn()
            }

            override suspend fun createCacheRequestAndReturn() {
                GlobalScope.launch(Main) {
                    result.addSource(loadFromCache()) { accountViewState ->
                        onCompleteJob(DataState.success(data = accountViewState, response = null))
                    }
                }
            }

            override fun loadFromCache(): LiveData<AccountViewState> {
                return accountDao.searchByPk(authToken.account_pk!!).switchMap {
                    object : LiveData<AccountViewState>() {
                        override fun onActive() {
                            super.onActive()
                            value = AccountViewState(it)
                        }
                    }
                }
            }

            override suspend fun updateLocalDb(cacheObject: Account?) {
                if (cacheObject != null) {
                    accountDao.updateAccount(
                        cacheObject.pk,
                        cacheObject.email,
                        cacheObject.username
                    )
                }
            }
        }.asLiveData()
    }

    fun updateAccount(
        authToken: AuthToken,
        account: Account
    ): LiveData<DataState<AccountViewState>> {
        return object : NetworkBoundResource<GenericResponse, Any, AccountViewState>(
            isNetworkAvailable = sessionManager.isConnectedToInternet(),
            isNetworkRequest = true,
            shouldLoadFromCache = false,
            shouldCancelIfNoNetwork = true
        ) {
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                return openApiMainService.saveAccountProperties(
                    authorization = "Token ${authToken.token}",
                    email = account.email,
                    username = account.username
                )
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<GenericResponse>) {
                updateLocalDb(null)
                onCompleteJob(
                    DataState.success(
                        data = null,
                        response = Response(response.body.response, ResponseType.Toast)
                    )
                )
            }

            override fun loadFromCache(): LiveData<AccountViewState> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

            override suspend fun updateLocalDb(cacheObject: Any?) {
                accountDao.updateAccount(account.pk, account.email, account.username)
            }
        }.asLiveData()
    }
}

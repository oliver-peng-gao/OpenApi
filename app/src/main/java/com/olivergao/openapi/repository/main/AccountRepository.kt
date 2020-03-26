package com.olivergao.openapi.repository.main

import android.util.Log
import com.olivergao.openapi.api.main.OpenApiMainService
import com.olivergao.openapi.persistance.AccountDao
import com.olivergao.openapi.session.SessionManager
import javax.inject.Inject
import kotlinx.coroutines.Job

class AccountRepository
@Inject constructor(
    val openApiMainService: OpenApiMainService,
    val accountDao: AccountDao,
    val sessionManager: SessionManager
) {
    private val TAG = "AppDebug"

    private var repositoryJob: Job? = null

    fun cancelActiveJobs() {
        Log.d(TAG, "cancelActiveJobs: MainRepo")
        repositoryJob?.cancel()
    }
}

package com.olivergao.openapi.repository.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.olivergao.openapi.api.auth.OpenApiAuthService
import com.olivergao.openapi.models.AuthToken
import com.olivergao.openapi.persistance.AccountDao
import com.olivergao.openapi.persistance.AuthTokenDao
import com.olivergao.openapi.session.SessionManager
import com.olivergao.openapi.ui.DataState
import com.olivergao.openapi.ui.Response
import com.olivergao.openapi.ui.ResponseType
import com.olivergao.openapi.ui.auth.state.AuthViewState
import com.olivergao.openapi.util.ApiEmptyResponse
import com.olivergao.openapi.util.ApiErrorResponse
import com.olivergao.openapi.util.ApiSuccessResponse
import com.olivergao.openapi.util.ErrorHandling.Companion.ERROR_UNKNOWN
import javax.inject.Inject

class AuthRepository
@Inject constructor(
    val authTokenDao: AuthTokenDao,
    val accountDao: AccountDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager
) {

    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>> {
        return openApiAuthService.login(email, password)
            .switchMap { response ->
                object : LiveData<DataState<AuthViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        when (response) {
                            is ApiSuccessResponse -> {
                                value = DataState.success(
                                    data = AuthViewState(
                                        authToken = AuthToken(
                                            response.body.pk,
                                            response.body.token
                                        )
                                    ),
                                    response = null
                                )
                            }
                            is ApiErrorResponse -> {
                                value = DataState.error(
                                    response = Response(
                                        message = response.errorMessage,
                                        responseType = ResponseType.Dialog
                                    )
                                )
                            }
                            is ApiEmptyResponse -> {
                                value = DataState.error(
                                    response = Response(
                                        message = ERROR_UNKNOWN,
                                        responseType = ResponseType.Dialog
                                    )
                                )
                            }
                        }
                    }
                }
            }
    }

    fun attemptRegistration(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>> {
        return openApiAuthService.register(email, username, password, confirmPassword)
            .switchMap { response ->
                object : LiveData<DataState<AuthViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        when (response) {
                            is ApiSuccessResponse -> {
                                value = DataState.success(
                                    data = AuthViewState(
                                        authToken = AuthToken(
                                            response.body.pk,
                                            response.body.token
                                        )
                                    ),
                                    response = null
                                )
                            }
                            is ApiErrorResponse -> {
                                value = DataState.error(
                                    response = Response(
                                        message = response.errorMessage,
                                        responseType = ResponseType.Dialog
                                    )
                                )
                            }
                            is ApiEmptyResponse -> {
                                value = DataState.error(
                                    response = Response(
                                        message = ERROR_UNKNOWN,
                                        responseType = ResponseType.Dialog
                                    )
                                )
                            }
                        }
                    }
                }
            }
    }
}
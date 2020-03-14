package com.olivergao.openapi.repository.auth

import android.util.Log
import androidx.lifecycle.LiveData
import com.olivergao.openapi.api.auth.OpenApiAuthService
import com.olivergao.openapi.api.auth.netweorkResponses.LoginResponse
import com.olivergao.openapi.api.auth.netweorkResponses.RegistrationResponse
import com.olivergao.openapi.models.AuthToken
import com.olivergao.openapi.persistance.AccountDao
import com.olivergao.openapi.persistance.AuthTokenDao
import com.olivergao.openapi.repository.NetworkBoundResource
import com.olivergao.openapi.session.SessionManager
import com.olivergao.openapi.ui.DataState
import com.olivergao.openapi.ui.Response
import com.olivergao.openapi.ui.ResponseType
import com.olivergao.openapi.ui.auth.state.AuthViewState
import com.olivergao.openapi.ui.auth.state.LoginFields
import com.olivergao.openapi.ui.auth.state.RegistrationFields
import com.olivergao.openapi.util.ApiSuccessResponse
import com.olivergao.openapi.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import com.olivergao.openapi.util.GenericApiResponse
import kotlinx.coroutines.Job
import javax.inject.Inject

class AuthRepository
@Inject constructor(
    val authTokenDao: AuthTokenDao,
    val accountDao: AccountDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager
) {
    private val TAG = "AppDebug"

    private var repositoryJob: Job? = null

    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>> {
        val loginFieldsErrors = LoginFields(email, password).isValidForLogin()
        if (loginFieldsErrors != LoginFields.LoginError.none()) {
            return returnErrorResponse(loginFieldsErrors, ResponseType.Dialog)
        }

        return object :
            NetworkBoundResource<LoginResponse, AuthViewState>(sessionManager.isConnectedToInternet()) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
                Log.d(TAG, "handleLoginApiSuccessResponse: $response")

                if (response.body.response == GENERIC_AUTH_ERROR) {
                    return onErrorReturn(
                        response.body.errorMessage,
                        shouldUseDialog = true,
                        shouldUseToast = false
                    )
                }

                onCompleteJob(
                    DataState.success(
                        AuthViewState(
                            authToken = AuthToken(
                                response.body.pk, response.body.token
                            )
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
                return openApiAuthService.login(email, password)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun attemptRegistration(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>> {
        val registrationFieldsErrors =
            RegistrationFields(email, username, password, confirmPassword).isValidForRegistration()
        if (registrationFieldsErrors != RegistrationFields.RegistrationError.none()) {
            return returnErrorResponse(registrationFieldsErrors, ResponseType.Dialog)
        }
        return object :
            NetworkBoundResource<RegistrationResponse, AuthViewState>(sessionManager.isConnectedToInternet()) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<RegistrationResponse>) {
                Log.d(TAG, "handleRegistrationApiSuccessResponse: $response")

                if (response.body.response == GENERIC_AUTH_ERROR) {
                    return onErrorReturn(
                        response.body.errorMessage,
                        shouldUseDialog = true,
                        shouldUseToast = false
                    )
                }

                onCompleteJob(
                    DataState.success(
                        AuthViewState(
                            authToken = AuthToken(
                                response.body.pk, response.body.token
                            )
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<RegistrationResponse>> {
                return openApiAuthService.register(email, username, password, confirmPassword)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun cancelActiveJobs() {
        Log.d(TAG, "cancelActiveJobs: AuthRepo")
        repositoryJob?.cancel()
    }

    private fun returnErrorResponse(
        loginFieldsErrors: String,
        responseType: ResponseType
    ): LiveData<DataState<AuthViewState>> {
        Log.d(TAG, "returnErrorResponse: error:$loginFieldsErrors type:$responseType")
        return object : LiveData<DataState<AuthViewState>>() {
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    Response(loginFieldsErrors, responseType)
                )
            }
        }
    }
}
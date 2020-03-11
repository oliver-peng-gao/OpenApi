package com.olivergao.openapi.ui.auth

import androidx.lifecycle.LiveData
import com.olivergao.openapi.api.auth.netweorkResponses.LoginResponse
import com.olivergao.openapi.api.auth.netweorkResponses.RegistrationResponse
import com.olivergao.openapi.models.AuthToken
import com.olivergao.openapi.repository.auth.AuthRepository
import com.olivergao.openapi.ui.BaseViewModel
import com.olivergao.openapi.ui.DataState
import com.olivergao.openapi.ui.auth.state.AuthStateEvent
import com.olivergao.openapi.ui.auth.state.AuthViewState
import com.olivergao.openapi.ui.auth.state.LoginFields
import com.olivergao.openapi.ui.auth.state.RegistrationFields
import com.olivergao.openapi.util.AbsentLiveData
import com.olivergao.openapi.util.GenericApiResponse
import javax.inject.Inject

class AuthViewModel
@Inject constructor(val authRepository: AuthRepository) :
    BaseViewModel<AuthStateEvent, AuthViewState>() {

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

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        return when (stateEvent) {
            is AuthStateEvent.LoginAttemptEvent -> {
                AbsentLiveData.create()
            }
            is AuthStateEvent.RegisterAttemptEvent -> {
                AbsentLiveData.create()
            }
            is AuthStateEvent.CheckPreviousAuthEvent -> {
                AbsentLiveData.create()
            }
        }
    }

    fun setRegistrationFields(registrationFields: RegistrationFields) {
        val update = getCurrentViewStateOrNew()
        if (update.registrationFields == registrationFields) return
        update.registrationFields = registrationFields
        _viewState.value = update
    }

    fun setLoginFields(loginFields: LoginFields) {
        val update = getCurrentViewStateOrNew()
        if (update.loginFields == loginFields) return
        update.loginFields = loginFields
        _viewState.value = update
    }

    fun setAuthToken(authToken: AuthToken) {
        val update = getCurrentViewStateOrNew()
        if (update.authToken == authToken) return
        update.authToken = authToken
        _viewState.value = update
    }
}
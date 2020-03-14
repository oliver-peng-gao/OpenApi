package com.olivergao.openapi.ui.auth

import androidx.lifecycle.LiveData
import com.olivergao.openapi.models.AuthToken
import com.olivergao.openapi.repository.auth.AuthRepository
import com.olivergao.openapi.ui.BaseViewModel
import com.olivergao.openapi.ui.DataState
import com.olivergao.openapi.ui.auth.state.AuthStateEvent
import com.olivergao.openapi.ui.auth.state.AuthViewState
import com.olivergao.openapi.ui.auth.state.LoginFields
import com.olivergao.openapi.ui.auth.state.RegistrationFields
import com.olivergao.openapi.util.AbsentLiveData
import javax.inject.Inject

class AuthViewModel
@Inject constructor(val authRepository: AuthRepository) :
    BaseViewModel<AuthStateEvent, AuthViewState>() {

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        return when (stateEvent) {
            is AuthStateEvent.LoginAttemptEvent -> {
                authRepository.attemptLogin(stateEvent.email, stateEvent.password)
            }
            is AuthStateEvent.RegisterAttemptEvent -> {
                authRepository.attemptRegistration(
                    stateEvent.email,
                    stateEvent.username,
                    stateEvent.password,
                    stateEvent.confirmPassword
                )
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

    fun cancelActiveJobs() {
        authRepository.cancelActiveJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}
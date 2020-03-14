package com.olivergao.openapi.ui

import androidx.lifecycle.*

abstract class BaseViewModel<StateEvent, ViewState> : ViewModel() {
    private val TAG: String = "AppDebug"

    protected val _stateEvent: MutableLiveData<StateEvent> = MutableLiveData()
    protected val _viewState: MutableLiveData<ViewState> = MutableLiveData()

    val viewState: LiveData<ViewState>
        get() = _viewState

    val dataState: LiveData<DataState<ViewState>> =
        _stateEvent.switchMap { stateEvent ->
            handleStateEvent(stateEvent)
        }

    fun setStateEvent(event: StateEvent) {
        _stateEvent.value = event
    }

    fun getCurrentViewStateOrNew(): ViewState {
        return viewState.value ?: initNewViewState()
    }

    abstract fun initNewViewState(): ViewState

    abstract fun handleStateEvent(stateEvent: StateEvent): LiveData<DataState<ViewState>>
}
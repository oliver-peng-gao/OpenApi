package com.olivergao.openapi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<StateEvent, ViewState> : ViewModel() {
    private val TAG: String = "AppDebug"

    protected val _stateEvent: MutableLiveData<StateEvent> = MutableLiveData()
    protected val _viewState: MutableLiveData<ViewState> = MutableLiveData()

    val viewState: LiveData<ViewState>
        get() = _viewState

    val dataState: LiveData<DataState<ViewState>> =
        Transformations.switchMap(_stateEvent) { stateEvent ->
            stateEvent?.let {
                handleStateEvent(it)
            }
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
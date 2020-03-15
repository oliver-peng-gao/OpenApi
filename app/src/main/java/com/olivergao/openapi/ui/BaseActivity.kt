package com.olivergao.openapi.ui

import android.util.Log
import com.olivergao.openapi.session.SessionManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(), DataStateChangedListener {
    val TAG: String = "BaseActivity"

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onDataStateChanged(dataState: DataState<*>?) {
        dataState?.let {
            GlobalScope.launch(Main) {
                displayProgressBar(it.loading.isLoading)
                it.error?.let { errorEvent ->
                    handleStateError(errorEvent)
                }
                it.data?.let {
                    it.response?.let { responseEvent ->
                        handleStateResponse(responseEvent)
                    }
                }
            }
        }
    }

    private fun handleStateResponse(responseEvent: Event<Response>) {
        responseEvent.getContentIfNotHandled()?.let {
            when (it.responseType) {
                is ResponseType.Dialog -> {
                    it.message?.let { message ->
                        showErrorDialog(message)
                    }
                }
                is ResponseType.Toast -> {
                    it.message?.let { message ->
                        displayToast(message)
                    }
                }
                is ResponseType.None -> {
                    Log.e(TAG, "handleStateResponse: None Type Error: ${it.message}")
                }
            }
        }
    }

    private fun handleStateError(errorEvent: Event<StateError>) {
        errorEvent.getContentIfNotHandled()?.let {
            when (it.response.responseType) {
                is ResponseType.Dialog -> {
                    it.response.message?.let { message ->
                        showErrorDialog(message)
                    }
                }
                is ResponseType.Toast -> {
                    it.response.message?.let { message ->
                        displayToast(message)
                    }
                }
                is ResponseType.None -> {
                    Log.e(TAG, "handleStateError: None Type Error: ${it.response.message}")
                }
            }
        }
    }

    abstract fun displayProgressBar(display: Boolean)
}
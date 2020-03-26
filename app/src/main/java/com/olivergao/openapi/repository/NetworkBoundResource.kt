package com.olivergao.openapi.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.olivergao.openapi.ui.DataState
import com.olivergao.openapi.ui.Response
import com.olivergao.openapi.ui.ResponseType
import com.olivergao.openapi.util.*
import com.olivergao.openapi.util.Constants.Companion.NETWORK_TIMEOUT
import com.olivergao.openapi.util.Constants.Companion.TESTING_CACHE_DELAY
import com.olivergao.openapi.util.Constants.Companion.TESTING_NETWORK_DELAY
import com.olivergao.openapi.util.ErrorHandling.Companion.ERROR_CHECK_NETWORK_CONNECTION
import com.olivergao.openapi.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.olivergao.openapi.util.ErrorHandling.Companion.UNABLE_TO_RESOLVE_HOST
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

abstract class NetworkBoundResource<ResponseObject, ViewStateType>
    (isNetworkAvailable: Boolean, isNetworkRequest: Boolean) {

    private val TAG = "AppDebug"

    private val result = MediatorLiveData<DataState<ViewStateType>>()
    protected lateinit var job: CompletableJob
    private lateinit var coroutineScope: CoroutineScope

    init {
        @Suppress("LeakingThis")
        setJob(initNewJob())
        setValue(DataState.loading(true, null))

        if (isNetworkRequest) {
            if (isNetworkAvailable) {
                coroutineScope.launch {
                    delay(TESTING_NETWORK_DELAY)
                    withContext(Main) {
                        val apiResponse = createCall()
                        result.addSource(apiResponse) { response ->
                            result.removeSource(apiResponse)
                            coroutineScope.launch {
                                handleNetworkCall(response)
                            }
                        }
                    }
                }
                GlobalScope.launch(IO) {
                    delay(NETWORK_TIMEOUT)
                    if (!job.isCompleted) {
                        Log.e(TAG, "NetworkBoundResource: JOB NETWORK TIMEOUT")
                        job.cancel(CancellationException((UNABLE_TO_RESOLVE_HOST)))
                    }
                }
            } else {
                onErrorReturn(
                    UNABLE_TO_RESOLVE_HOST,
                    shouldUseDialog = true,
                    shouldUseToast = false
                )
            }
        } else {
            coroutineScope.launch {
                delay(TESTING_CACHE_DELAY)
                createCacheRequestAndReturn()
            }
        }
    }

    private suspend fun handleNetworkCall(response: GenericApiResponse<ResponseObject>?) {
        when (response) {
            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }
            is ApiErrorResponse -> {
                Log.e(TAG, "NetworkBoundResource: ${response.errorMessage}")
                onErrorReturn(response.errorMessage, shouldUseDialog = true, shouldUseToast = false)
            }
            is ApiEmptyResponse -> {
                Log.e(TAG, "NetworkBoundResource: Request return NOTHING(HTTP 204)")
                onErrorReturn(
                    "HTTP 204. Returned nothing.",
                    shouldUseDialog = true,
                    shouldUseToast = false
                )
            }
        }
    }

    fun onCompleteJob(dataState: DataState<ViewStateType>) {
        GlobalScope.launch(Main) {
            job.complete()
            setValue(dataState)
        }
    }

    private fun setValue(dataState: DataState<ViewStateType>) {
        result.value = dataState
    }

    fun onErrorReturn(errorMessage: String?, shouldUseDialog: Boolean, shouldUseToast: Boolean) {
        var msg = errorMessage
        var responseType: ResponseType = ResponseType.None
        if (errorMessage == null) {
            msg = ERROR_UNKNOWN
        } else if (ErrorHandling.isNetworkError(errorMessage)) {
            msg = ERROR_CHECK_NETWORK_CONNECTION
        }
        if (shouldUseToast) {
            responseType = ResponseType.Toast
        }
        if (shouldUseDialog) {
            responseType = ResponseType.Dialog
        }
        onCompleteJob(
            DataState.error(Response(msg, responseType))
        )
    }

    @OptIn(InternalCoroutinesApi::class)
    private fun initNewJob(): Job {
        Log.d(TAG, "initNewJob: called...")
        job = Job()
        job.invokeOnCompletion(
            onCancelling = true,
            invokeImmediately = true,
            handler = object : CompletionHandler {
                override fun invoke(cause: Throwable?) {
                    if (job.isCancelled) {
                        Log.e(TAG, "NetworkBoundResource: job is cancelled.")
                        cause?.let {
                            onErrorReturn(
                                it.message,
                                shouldUseDialog = false,
                                shouldUseToast = true
                            )
                        } ?: onErrorReturn(
                            ERROR_UNKNOWN,
                            shouldUseDialog = false,
                            shouldUseToast = true
                        )
                    } else if (job.isCompleted) {
                        Log.e(TAG, "NetworkBoundResource: job is completed.")
                    }
                }
            })
        coroutineScope = CoroutineScope(IO + job)
        return job
    }

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>

    open suspend fun createCacheRequestAndReturn() {}

    open suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>) {}

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    abstract fun setJob(job: Job)
}

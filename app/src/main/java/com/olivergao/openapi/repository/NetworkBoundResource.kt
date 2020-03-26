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

@Suppress("LeakingThis")
abstract class NetworkBoundResource<ResponseObject, CacheObject, ViewStateType>(
    isNetworkAvailable: Boolean,
    isNetworkRequest: Boolean,
    shouldLoadFromCache: Boolean,
    shouldCancelIfNoNetwork: Boolean
) {

    protected val result = MediatorLiveData<DataState<ViewStateType>>()
    private val TAG = "AppDebug"
    protected lateinit var job: CompletableJob
    private lateinit var coroutineScope: CoroutineScope

    init {
        setJob(initNewJob())
        setValue(DataState.loading(true, null))

        if (shouldLoadFromCache) {
            val dbSource = loadFromCache()
            result.addSource(dbSource) {
                result.removeSource(dbSource)
                setValue(DataState.loading(isLoading = true, cachedData = it))
            }
        }

        if (isNetworkRequest) {
            when {
                isNetworkAvailable -> {
                    doNetworkRequest()
                }
                shouldCancelIfNoNetwork -> {
                    onErrorReturn(
                        UNABLE_TO_RESOLVE_HOST,
                        shouldUseDialog = true,
                        shouldUseToast = false
                    )
                }
                else -> {
                    doCacheRequest()
                }
            }
        } else {
            doCacheRequest()
        }
    }

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>
    fun onCompleteJob(dataState: DataState<ViewStateType>) {
        GlobalScope.launch(Main) {
            job.complete()
            setValue(dataState)
        }
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

    abstract suspend fun createCacheRequestAndReturn()
    abstract suspend fun createCall(): LiveData<GenericApiResponse<ResponseObject>>
    abstract suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)
    abstract fun loadFromCache(): LiveData<ViewStateType>
    abstract fun setJob(job: Job)
    abstract suspend fun updateLocalDb(cacheObject: CacheObject?)

    private fun doCacheRequest() {
        coroutineScope.launch {
            delay(TESTING_CACHE_DELAY)
            createCacheRequestAndReturn()
        }
    }

    private fun doNetworkRequest() {
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

    private fun setValue(dataState: DataState<ViewStateType>) {
        result.value = dataState
    }
}

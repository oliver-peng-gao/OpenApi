package com.olivergao.openapi.ui.auth

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.olivergao.openapi.R
import com.olivergao.openapi.ui.DataState
import com.olivergao.openapi.ui.DataStateChangedListener
import com.olivergao.openapi.ui.Response
import com.olivergao.openapi.ui.ResponseType
import com.olivergao.openapi.util.Constants
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment : BaseAuthFragment() {

    lateinit var stateChangedListener: DataStateChangedListener

    private val webInteractionCallback = object : WebAppInterface.OnWebInteractionCallback {
        override fun onSuccess(email: String) {
            Log.d(TAG, "onSuccess")
            onPasswordResetLinkSent()
        }

        override fun onError(errorMessage: String) {
            Log.e(TAG, "onError: $errorMessage")

            val dataState = DataState.error<Any>(
                    Response(errorMessage, ResponseType.Dialog)
            )
            stateChangedListener.onDataStateChanged(dataState)
        }

        override fun onLoading(isLoading: Boolean) {
            stateChangedListener.onDataStateChanged(
                    DataState.loading(isLoading = isLoading, cachedData = null)
            )
        }
    }

    private fun onPasswordResetLinkSent() {
        GlobalScope.launch(Main) {
            parent_view.removeView(webview)
            webview.destroy()

            val animation = TranslateAnimation(password_reset_done_container.width.toFloat(), 0f, 0f, 0f)
            animation.duration = 500
            password_reset_done_container.startAnimation(animation)
            password_reset_done_container.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPasswordResetWebView()
        return_to_launcher_fragment.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    fun loadPasswordResetWebView() {
        stateChangedListener.onDataStateChanged(
                DataState.loading(true, null)
        )
        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                stateChangedListener.onDataStateChanged(
                        DataState.loading(false, null)
                )
            }
        }
        webview.loadUrl(Constants.PASSWORD_RESET_URL)
        webview.settings.javaScriptEnabled = true
        webview.addJavascriptInterface(WebAppInterface(webInteractionCallback), "AndroidTextListener")
    }

    class WebAppInterface(private val callBack: OnWebInteractionCallback) {
        @JavascriptInterface
        fun onSuccess(email: String) {
            callBack.onSuccess(email)
        }

        @JavascriptInterface
        fun onError(errorMessage: String) {
            callBack.onError(errorMessage)
        }

        @JavascriptInterface
        fun onLoading(isLoading: Boolean) {
            callBack.onLoading(isLoading)
        }

        interface OnWebInteractionCallback {
            fun onSuccess(email: String)
            fun onError(errorMessage: String)
            fun onLoading(isLoading: Boolean)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangedListener = context as DataStateChangedListener
        } catch (e: Exception) {
            Log.e(TAG, "onAttach: $context must implement DataStateChangedListener.")
        }
    }
}

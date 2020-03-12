package com.olivergao.openapi.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.olivergao.openapi.R
import com.olivergao.openapi.di.auth.AuthViewModelFactory
import com.olivergao.openapi.ui.BaseActivity
import com.olivergao.openapi.ui.main.MainActivity
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(this, Observer { authViewState ->
            authViewState.authToken?.let { token ->
                sessionManager.login(token)
            }
        })

        sessionManager.cachedToken.observe(this, Observer {
            Log.d(TAG, "AuthActivity subscribeObservers: AuthToken: $it")
            if (it != null && it.account_pk != -1 && it.token != null) {
                navMainActivity()
            }
        })
    }

    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
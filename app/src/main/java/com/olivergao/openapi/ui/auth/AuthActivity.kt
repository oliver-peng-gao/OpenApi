package com.olivergao.openapi.ui.auth

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.olivergao.openapi.R
import com.olivergao.openapi.di.auth.AuthViewModelFactory
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)
    }
}
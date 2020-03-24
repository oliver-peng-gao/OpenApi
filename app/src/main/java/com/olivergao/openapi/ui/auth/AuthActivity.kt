package com.olivergao.openapi.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.olivergao.openapi.R
import com.olivergao.openapi.di.auth.AuthViewModelFactory
import com.olivergao.openapi.ui.BaseActivity
import com.olivergao.openapi.ui.auth.state.AuthStateEvent
import com.olivergao.openapi.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class AuthActivity : BaseActivity(), NavController.OnDestinationChangedListener {

    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory
    lateinit var viewModel: AuthViewModel

    override fun displayProgressBar(display: Boolean) {
        if (display) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.INVISIBLE
        }
    }

    override fun expandAppBar() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)
        findNavController(R.id.fragment_container).addOnDestinationChangedListener(this)
        subscribeObservers()
        checkPrevAuthUser()
    }

    override fun onDestinationChanged(
            controller: NavController,
            destination: NavDestination,
            arguments: Bundle?
    ) {
        viewModel.cancelActiveJobs()
    }

    private fun checkPrevAuthUser() {
        viewModel.setStateEvent(AuthStateEvent.CheckPreviousAuthEvent)
    }

    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            onDataStateChanged(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { authViewState ->
                        authViewState.authToken?.let { authToken ->
                            Log.d(TAG, "AuthActivity, DataState: $authToken")
                            viewModel.setAuthToken(authToken)
                        }
                    }
                }
            }
        })

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
}
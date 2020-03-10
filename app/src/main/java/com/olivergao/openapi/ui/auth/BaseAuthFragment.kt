package com.olivergao.openapi.ui.auth

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.olivergao.openapi.di.auth.AuthViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseAuthFragment : DaggerFragment() {

    @Suppress("PropertyName")
    val TAG: String = "AppDebug"

    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory

    lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = requireActivity().run {
            ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)
        }

    }
}
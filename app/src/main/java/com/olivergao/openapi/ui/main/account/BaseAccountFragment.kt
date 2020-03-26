package com.olivergao.openapi.ui.main.account

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.olivergao.openapi.R
import com.olivergao.openapi.di.main.AccountViewModelFactory
import com.olivergao.openapi.ui.DataStateChangedListener
import com.olivergao.openapi.ui.main.AccountViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseAccountFragment : DaggerFragment() {

    val TAG: String = "AppDebug"

    @Inject
    lateinit var accountViewModelFactory: AccountViewModelFactory
    lateinit var stateChangedListener: DataStateChangedListener
    lateinit var viewModel: AccountViewModel

    private fun setupActionBarWithNavController() {
        val appBarConfig = AppBarConfiguration(setOf(R.id.accountFragment))
        NavigationUI.setupActionBarWithNavController(
            activity as AppCompatActivity,
            findNavController(),
            appBarConfig
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangedListener = context as DataStateChangedListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement DataStateChangeListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController()

        viewModel = requireActivity().run {
            ViewModelProvider(this, accountViewModelFactory).get(AccountViewModel::class.java)
        }
    }
}

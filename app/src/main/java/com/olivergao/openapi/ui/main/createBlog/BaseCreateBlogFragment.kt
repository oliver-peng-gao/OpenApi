package com.olivergao.openapi.ui.main.createBlog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.olivergao.openapi.R
import com.olivergao.openapi.ui.DataStateChangedListener
import dagger.android.support.DaggerFragment

abstract class BaseCreateBlogFragment : DaggerFragment() {

    val TAG: String = "AppDebug"

    private lateinit var stateChangedListener: DataStateChangedListener

    private fun setupActionBarWithNavController() {
        val appBarConfig = AppBarConfiguration(setOf(R.id.createBlogFragment))
        NavigationUI.setupActionBarWithNavController(
            activity as AppCompatActivity,
            findNavController(),
            appBarConfig
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangedListener = context as DataStateChangedListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement DataStateChangeListener")
        }
    }
}

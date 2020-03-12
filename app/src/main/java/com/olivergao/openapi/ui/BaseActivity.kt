package com.olivergao.openapi.ui

import com.olivergao.openapi.session.SessionManager
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {
    val TAG: String = "BaseActivity"

    @Inject
    lateinit var sessionManager: SessionManager
}
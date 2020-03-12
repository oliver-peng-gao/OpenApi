package com.olivergao.openapi.di

import com.olivergao.openapi.di.auth.AuthFragmentBuildersModule
import com.olivergao.openapi.di.auth.AuthModule
import com.olivergao.openapi.di.auth.AuthScope
import com.olivergao.openapi.ui.auth.AuthActivity
import com.olivergao.openapi.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class, AuthFragmentBuildersModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}
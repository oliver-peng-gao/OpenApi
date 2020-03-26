package com.olivergao.openapi.di.auth

import com.olivergao.openapi.ui.auth.ForgotPasswordFragment
import com.olivergao.openapi.ui.auth.LauncherFragment
import com.olivergao.openapi.ui.auth.LoginFragment
import com.olivergao.openapi.ui.auth.RegisterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeLauncherFragment(): LauncherFragment

    @ContributesAndroidInjector()
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector()
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector()
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment
}

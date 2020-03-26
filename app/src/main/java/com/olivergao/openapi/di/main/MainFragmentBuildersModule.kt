package com.olivergao.openapi.di.main

import com.olivergao.openapi.ui.main.account.AccountFragment
import com.olivergao.openapi.ui.main.account.ChangePasswordFragment
import com.olivergao.openapi.ui.main.account.UpdateAccountFragment
import com.olivergao.openapi.ui.main.blog.BlogFragment
import com.olivergao.openapi.ui.main.blog.UpdateBlogFragment
import com.olivergao.openapi.ui.main.blog.ViewBlogFragment
import com.olivergao.openapi.ui.main.createBlog.CreateBlogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeBlogFragment(): BlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeAccountFragment(): AccountFragment

    @ContributesAndroidInjector()
    abstract fun contributeChangePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector()
    abstract fun contributeCreateBlogFragment(): CreateBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeUpdateBlogFragment(): UpdateBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeViewBlogFragment(): ViewBlogFragment

    @ContributesAndroidInjector()
    abstract fun contributeUpdateAccountFragment(): UpdateAccountFragment
}

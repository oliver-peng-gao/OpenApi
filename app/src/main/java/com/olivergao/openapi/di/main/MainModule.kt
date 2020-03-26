package com.olivergao.openapi.di.main

import com.olivergao.openapi.api.main.OpenApiMainService
import com.olivergao.openapi.di.auth.AuthScope
import com.olivergao.openapi.persistance.AccountDao
import com.olivergao.openapi.repository.main.AccountRepository
import com.olivergao.openapi.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideAccountRepository(
        sessionManager: SessionManager,
        accountDao: AccountDao,
        openApiMainService: OpenApiMainService
    ): AccountRepository {
        return AccountRepository(
            openApiMainService,
            accountDao,
            sessionManager
        )
    }

    @MainScope
    @Provides
    fun provideMainApiService(builder: Retrofit.Builder): OpenApiMainService {
        return builder.build().create(OpenApiMainService::class.java)
    }
}
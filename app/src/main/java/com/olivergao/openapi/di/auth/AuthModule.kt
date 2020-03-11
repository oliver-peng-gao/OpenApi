package com.olivergao.openapi.di.auth

import com.olivergao.openapi.api.auth.OpenApiAuthService
import com.olivergao.openapi.persistance.AccountDao
import com.olivergao.openapi.persistance.AuthTokenDao
import com.olivergao.openapi.repository.auth.AuthRepository
import com.olivergao.openapi.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class AuthModule {

    @AuthScope
    @Provides
    fun provideFakeApiService(builder: Retrofit.Builder): OpenApiAuthService {
        return builder.build().create(OpenApiAuthService::class.java)
    }

    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountDao,
        openApiAuthService: OpenApiAuthService
    ): AuthRepository {
        return AuthRepository(
            authTokenDao,
            accountPropertiesDao,
            openApiAuthService,
            sessionManager
        )
    }

}
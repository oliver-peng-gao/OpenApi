package com.olivergao.openapi.di.auth

import android.content.SharedPreferences
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
    fun provideAuthApiService(builder: Retrofit.Builder): OpenApiAuthService {
        return builder.build().create(OpenApiAuthService::class.java)
    }

    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountDao: AccountDao,
        openApiAuthService: OpenApiAuthService,
        sharedPreferences: SharedPreferences
    ): AuthRepository {
        return AuthRepository(
            authTokenDao,
            accountDao,
            openApiAuthService,
            sessionManager,
            sharedPreferences
        )
    }

}
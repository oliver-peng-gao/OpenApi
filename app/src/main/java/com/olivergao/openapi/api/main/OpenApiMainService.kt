package com.olivergao.openapi.api.main

import androidx.lifecycle.LiveData
import com.olivergao.openapi.api.GenericResponse
import com.olivergao.openapi.models.Account
import com.olivergao.openapi.util.GenericApiResponse
import retrofit2.http.*

interface OpenApiMainService {

    @GET("account/properties")
    fun getAccount(
        @Header("Authorization") authorization: String?
    ): LiveData<GenericApiResponse<Account>>

    @PUT("account/properties/update")
    @FormUrlEncoded
    fun saveAccountProperties(
        @Header("Authorization") authorization: String,
        @Field("email") email: String,
        @Field("username") username: String
    ): LiveData<GenericApiResponse<GenericResponse>>
}

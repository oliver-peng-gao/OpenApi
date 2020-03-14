package com.olivergao.openapi.api.auth.netweorkResponses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("response")
    @Expose
    var response: String,

    @SerializedName("error_message")
    @Expose
    var errorMessage: String,

    @SerializedName("token")
    @Expose
    var token: String,

    @SerializedName("pk")
    @Expose
    var pk: Int = -1,

    @SerializedName("email")
    @Expose
    var email: String
)
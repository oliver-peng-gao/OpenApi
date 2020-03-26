package com.olivergao.openapi.ui.main.account.state

sealed class AccountStateEvent {
    object GetAccountEvent : AccountStateEvent()

    data class UpdateAccountEvent(
        val email: String,
        val username: String
    ) : AccountStateEvent()

    data class ChangePasswordEvent(
        val currentPassword: String,
        val newPassword: String,
        val confirmNewPassword: String
    ) : AccountStateEvent()

    object None : AccountStateEvent()
}

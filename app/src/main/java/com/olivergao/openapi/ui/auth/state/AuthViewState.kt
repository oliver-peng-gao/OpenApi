package com.olivergao.openapi.ui.auth.state

import com.olivergao.openapi.models.AuthToken

data class RegistrationFields(
    var email: String? = null,
    var username: String? = null,
    var password: String? = null,
    var confirmPassword: String? = null
) {
    class RegistrationError {
        companion object {
            fun mustFillAllFields(): String {
                return "All fields are required."
            }

            fun passwordsDoNotMatch(): String {
                return "Passwords must match"
            }

            fun none(): String {
                return "None"
            }
        }
    }

    fun isValidForRegistration(): String {
        if (email.isNullOrEmpty()
            || username.isNullOrEmpty()
            || password.isNullOrEmpty()
            || confirmPassword.isNullOrEmpty()
        ) {
            return RegistrationError.mustFillAllFields()
        }

        if (!password.equals(confirmPassword)) {
            return RegistrationError.passwordsDoNotMatch()
        }

        return RegistrationError.none()
    }
}

data class LoginFields(
    var email: String? = null,
    var password: String? = null
) {
    class LoginError {
        companion object {

            fun mustFillAllFields(): String {
                return "You can't login without an email and password."
            }

            fun none(): String {
                return "None"
            }

        }
    }

    fun isValidForLogin(): String {

        if (email.isNullOrEmpty()
            || password.isNullOrEmpty()
        ) {

            return LoginError.mustFillAllFields()
        }
        return LoginError.none()
    }

    override fun toString(): String {
        return "LoginState(email=$email, password=$password)"
    }
}

data class AuthViewState(
    var registrationFields: RegistrationFields? = RegistrationFields(),
    var loginFields: LoginFields? = LoginFields(),
    var authToken: AuthToken? = null
)
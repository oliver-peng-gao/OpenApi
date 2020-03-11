package com.olivergao.openapi.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.olivergao.openapi.R
import com.olivergao.openapi.ui.auth.state.RegistrationFields
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { authViewState ->
            authViewState.registrationFields?.let { registrationFields ->
                registrationFields.email.let { input_email.setText(it) }
                registrationFields.username.let { input_username.setText(it) }
                registrationFields.password.let { input_password.setText(it) }
                registrationFields.confirmPassword.let { input_password_confirm.setText(it) }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRegistrationFields(
            RegistrationFields(
                input_email.text.toString(),
                input_username.text.toString(),
                input_password.text.toString(),
                input_password_confirm.text.toString()
            )
        )
    }
}
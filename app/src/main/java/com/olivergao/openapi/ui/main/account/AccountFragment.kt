package com.olivergao.openapi.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.olivergao.openapi.R
import com.olivergao.openapi.models.Account
import com.olivergao.openapi.ui.main.account.state.AccountStateEvent
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : BaseAccountFragment() {

    private fun setAccountDataFields(account: Account) {
        email.text = account.email
        username.text = account.username
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangedListener.onDataStateChanged(dataState)
            dataState.data?.data?.getContentIfNotHandled()?.account?.let { account ->
                Log.d(TAG, "AccountFragment, DataSate $account")
                viewModel.setAccountData(account)
            }
        })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.account?.let { account ->
                Log.d(TAG, "AccountFragment, ViewState $account")
                setAccountDataFields(account)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_view_menu, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                findNavController().navigate(R.id.action_accountFragment_to_updateAccountFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(AccountStateEvent.GetAccountEvent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
        change_password.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_changePasswordFragment)
        }
        logout_button.setOnClickListener {
            viewModel.logout()
        }
    }
}

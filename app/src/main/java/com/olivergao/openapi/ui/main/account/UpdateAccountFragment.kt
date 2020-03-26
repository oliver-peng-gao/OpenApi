package com.olivergao.openapi.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import com.olivergao.openapi.R
import com.olivergao.openapi.models.Account
import com.olivergao.openapi.ui.main.account.state.AccountStateEvent
import kotlinx.android.synthetic.main.fragment_update_account.*

class UpdateAccountFragment : BaseAccountFragment() {

    private fun setAccountDataFields(account: Account) {
        input_email.setText(account.email)
        input_username.setText(account.username)
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangedListener.onDataStateChanged(dataState)
            Log.d(TAG, "UpdateAccountFragment, DataSate $dataState")
        })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.account?.let { account ->
                Log.d(TAG, "UpdateAccountFragment, ViewState $account")
                setAccountDataFields(account)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_account, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                viewModel.setStateEvent(
                    AccountStateEvent.UpdateAccountEvent(
                        input_email.text.toString(),
                        input_username.text.toString()
                    )
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
    }
}

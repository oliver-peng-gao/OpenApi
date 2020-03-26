package com.olivergao.openapi.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.olivergao.openapi.R
import com.olivergao.openapi.ui.BaseActivity
import com.olivergao.openapi.ui.auth.AuthActivity
import com.olivergao.openapi.ui.main.account.ChangePasswordFragment
import com.olivergao.openapi.ui.main.account.UpdateAccountFragment
import com.olivergao.openapi.ui.main.blog.UpdateBlogFragment
import com.olivergao.openapi.ui.main.blog.ViewBlogFragment
import com.olivergao.openapi.util.BOTTOM_NAV_BACKSTACK_KEY
import com.olivergao.openapi.util.BottomNavController
import com.olivergao.openapi.util.BottomNavController.*
import com.olivergao.openapi.util.setUpNavigation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(),
        NavGraphProvider,
        OnNavigationGraphChanged,
        OnNavigationReselectedListener {

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
                this,
                R.id.main_nav_host_fragment,
                R.id.nav_blog,
                this,
                this
        )
    }

    private fun navAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupActionBar() {
        setSupportActionBar(tool_bar)
    }

    private fun setupBottomNavigationView(savedInstanceState: Bundle?) {
        bottom_navigation_view.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.setupBottomNavigationBackStack(null)
            bottomNavController.onNavigationItemSelected()
        } else {
            (savedInstanceState[BOTTOM_NAV_BACKSTACK_KEY] as IntArray?)?.let { items ->
                val backStack = BackStack()
                backStack.addAll(items.toTypedArray())
                bottomNavController.setupBottomNavigationBackStack(backStack)
            }
        }
    }

    private fun subscribeObservers() {
        sessionManager.cachedToken.observe(this, Observer {
            Log.d(TAG, "MainActivity subscribeObservers: AuthToken: $it")
            if (it == null || it.account_pk == -1 || it.token == null) {
                navAuthActivity()
            }
        })
    }

    override fun displayProgressBar(display: Boolean) {
        if (display) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.INVISIBLE
        }
    }

    override fun expandAppBar() {
        app_bar.setExpanded(true)
    }

    override fun getNavGraphId(itemId: Int) = when (itemId) {
        R.id.nav_blog -> R.navigation.nav_blog
        R.id.nav_account -> R.navigation.nav_account
        R.id.nav_create_blog -> R.navigation.nav_create_blog
        else -> R.navigation.nav_blog
    }

    override fun onBackPressed() = bottomNavController.onBackPressed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
        setupBottomNavigationView(savedInstanceState)
        subscribeObservers()
    }

    override fun onGraphChanged() {
        expandAppBar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onReselectNavItem(navController: NavController, fragment: Fragment) =
            when (fragment) {
                is ViewBlogFragment -> {
                    navController.navigate(R.id.action_viewBlogFragment_to_blogFragment)
                }
                is UpdateBlogFragment -> {
                    navController.navigate(R.id.action_updateBlogFragment_to_blogFragment)
                }
                is UpdateAccountFragment -> {
                    navController.navigate(R.id.action_updateAccountFragment_to_accountFragment)
                }
                is ChangePasswordFragment -> {
                    navController.navigate(R.id.action_changePasswordFragment_to_accountFragment)
                }
                else -> {

                }
            }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putIntArray(
                BOTTOM_NAV_BACKSTACK_KEY,
                bottomNavController.navigationBackStack.toIntArray()
        )
        super.onSaveInstanceState(outState)
    }
}
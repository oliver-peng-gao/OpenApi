package com.olivergao.openapi.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.olivergao.openapi.R
import com.olivergao.openapi.ui.displayToast
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Class credit: Allan Veloso
 * I took the concept from Allan Veloso and made alterations to fit our needs.
 * https://stackoverflow.com/questions/50577356/android-jetpack-navigation-bottomnavigationview-with-youtube-or-instagram-like#_=_
 */

const val BOTTOM_NAV_BACKSTACK_KEY =
    "com.olivergao.openapi.util.BottomNavController.bottom_nav_backstack"

class BottomNavController(
    val context: Context,
    @IdRes val containerId: Int,
    @IdRes val appStartDestinationId: Int,
    private val graphChangedListener: OnNavigationGraphChanged?,
    private val navGraphProvider: NavGraphProvider
) {

    private val TAG: String = "AppDebug"
    private var exitCount = 0
    lateinit var activity: Activity
    lateinit var fragmentManager: FragmentManager
    lateinit var navItemChangedListener: OnNavigationItemChanged
    lateinit var navigationBackStack: BackStack

    init {
        if (context is Activity) {
            activity = context
            fragmentManager = (activity as FragmentActivity).supportFragmentManager
        }
    }

    @SuppressLint("RestrictedApi")
    fun onBackPressed() {
        val navController = fragmentManager.findFragmentById(containerId)!!
            .findNavController()

        when {
            navController.backStack.size > 2 -> {
                navController.popBackStack()
            }
            navigationBackStack.size > 1 -> {
                navigationBackStack.removeLast()
                onNavigationItemSelected()
            }
            navigationBackStack.last() != appStartDestinationId -> {
                navigationBackStack.removeLast()
                navigationBackStack.add(0, appStartDestinationId)
                onNavigationItemSelected()
            }
            exitCount == 0 -> {
                exitCount++
                context.displayToast("Press back again to exit the App")
                GlobalScope.launch(IO) {
                    delay(3000)
                    exitCount = 0
                }
            }
            else -> activity.finish()
        }
    }

    fun onNavigationItemSelected(itemId: Int = navigationBackStack.last()): Boolean {
        val fragment = fragmentManager.findFragmentByTag(itemId.toString())
            ?: NavHostFragment.create(navGraphProvider.getNavGraphId(itemId))
        fragmentManager.commit {
            setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
            )
            replace(containerId, fragment, itemId.toString())
            addToBackStack(null)
        }

        // Add to backstack
        navigationBackStack.moveLast(itemId)

        // Update checked icon
        navItemChangedListener.onItemChanged(itemId)

        //Communicate with Activity
        graphChangedListener?.onGraphChanged()

        return true
    }

    fun setupBottomNavigationBackStack(previousBackStack: BackStack?) {
        navigationBackStack = previousBackStack ?: BackStack.of(appStartDestinationId)
    }

    @Parcelize
    class BackStack : ArrayList<Int>(), Parcelable {

        fun moveLast(item: Int) {
            remove(item)
            add(item)
        }

        fun removeLast() = removeAt(size - 1)

        companion object {
            fun of(vararg elements: Int): BackStack {
                val backStack = BackStack()
                backStack.addAll(elements.toTypedArray())
                return backStack
            }
        }
    }

    interface OnNavigationItemChanged {
        fun onItemChanged(itemId: Int)
    }

    interface NavGraphProvider {
        @NavigationRes
        fun getNavGraphId(itemId: Int): Int
    }

    interface OnNavigationGraphChanged {
        fun onGraphChanged()
    }

    interface OnNavigationReselectedListener {
        fun onReselectNavItem(navController: NavController, fragment: Fragment)
    }

}

fun BottomNavigationView.setUpNavigation(
    bottomNavController: BottomNavController,
    onReselectedListener: BottomNavController.OnNavigationReselectedListener
) {
    setOnNavigationItemSelectedListener {
        bottomNavController.onNavigationItemSelected(it.itemId)
    }

    setOnNavigationItemReselectedListener {
        bottomNavController
            .fragmentManager
            .findFragmentById(bottomNavController.containerId)!!
            .childFragmentManager
            .fragments[0]?.let { fragment ->
            onReselectedListener.onReselectNavItem(
                bottomNavController.activity.findNavController(bottomNavController.containerId),
                fragment
            )
        }
    }

    bottomNavController.navItemChangedListener = object :
        BottomNavController.OnNavigationItemChanged {
        override fun onItemChanged(itemId: Int) {
            menu.findItem(itemId).isChecked = true
        }
    }
}
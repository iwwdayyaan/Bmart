package com.bart.interfaces

import android.support.v4.app.Fragment

/**
 * A host (typically an `Activity`} that can display fragments and knows how to respond to
 * navigation events.
 */
interface NavigationHost {
    /**
     * Trigger a navigation to the specified fragment, optionally adding a transaction to the back
     * stack to make this navigation reversible.
     * for replace false and for add true
     */
    fun navigateTo(fragment: Fragment, addToBackstack: Boolean,addOrReplace: Boolean)
}
interface SelectViewPager {
    fun currentItem(item : Int)
}
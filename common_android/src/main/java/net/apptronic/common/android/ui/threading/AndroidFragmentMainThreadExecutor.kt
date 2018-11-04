package net.apptronic.common.android.ui.threading

import androidx.fragment.app.Fragment

class AndroidFragmentMainThreadExecutor(private val fragment: Fragment) : ThreadExecutor {

    override fun execute(action: () -> Unit) {
        executeOnFragment(fragment, action)
    }

    private fun executeOnFragment(fragment: Fragment?, action: () -> Unit) {
        fragment?.let {
            it.activity?.runOnUiThread(action) ?: executeOnFragment(it.parentFragment, action)
        }
    }

}
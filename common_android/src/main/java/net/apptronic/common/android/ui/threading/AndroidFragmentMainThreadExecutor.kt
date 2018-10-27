package net.apptronic.common.android.ui.threading

import androidx.fragment.app.Fragment

class AndroidFragmentMainThreadExecutor(private val fragment: Fragment) : ThreadExecutor {

    override fun execute(action: () -> Unit) {
        fragment.activity?.runOnUiThread(action)
    }

}
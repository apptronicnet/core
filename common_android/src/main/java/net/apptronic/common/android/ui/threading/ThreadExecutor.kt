package net.apptronic.common.android.ui.threading

interface ThreadExecutor {

    fun execute(action: () -> Unit)

}